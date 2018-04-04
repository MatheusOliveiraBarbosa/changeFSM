/* 
 * Course and Semester : 18-649 Fall 2013
 * Group No: 16
 * Group Members : Jiangtian Nie(jnie) , Yue Chen(yuechen),
 *                 Sally Stevenson(ststeven) , Sri Harsha Koppaka(skoppaka)
 * Author : Jiangtian Nie
 * AndrewID : jnie
 */

package simulator.elevatorcontrol;

import jSimPack.SimTime;

import java.util.HashMap;

import simulator.elevatormodules.*;
import simulator.framework.*;
import simulator.payloads.*;
import simulator.payloads.DrivePayload.WriteableDrivePayload;
import simulator.payloads.DriveSpeedPayload.ReadableDriveSpeedPayload;

public class DriveControl extends Controller {

    private SimTime period;
    private WriteableDrivePayload localDrive;
    private ReadableDriveSpeedPayload localDriveSpeed;
    private DriveSpeedCanPayloadTranslator mDriveSpeed;
    private DesiredFloorCanPayloadTranslator mDesiredFloor;
    private IntCanPayloadTranslator mCarLevelPosition;
    private HashMap<Integer, BitCanPayloadTranslator> mDoorClosedArray;
    private HashMap<Integer, DoorMotorCommandCanPayloadTranslator> mDoorMotorArray;
    private BitCanPayloadTranslator mSafety;
    private HashMap<Integer, BitCanPayloadTranslator> mHoistwayLimitArray;
    private BitCanPayloadTranslator mLevelSensorArray[];
    private IntCanPayloadTranslator mCarWeight;
    private int cushion = 600;

    Utility.AtFloorArray mAtFloorArray;

    private enum State {
        STATE_STOP,
        STATE_LEVEL_UP,
        STATE_LEVEL_DOWN,
        STATE_SLOW_UP,
        STATE_SLOW_DOWN,
        STATE_FAST_UP,
        STATE_FAST_DOWN,
        STATE_EMERGENCY,	
    }

    private State state = State.STATE_STOP;

    public DriveControl(SimTime period, boolean flag) {
        super("DriveControl", flag);

        this.period = period;

        // Initializing physical interface
        localDrive = DrivePayload.getWriteablePayload();
        physicalInterface.sendTimeTriggered(localDrive, period);
        localDriveSpeed = DriveSpeedPayload.getReadablePayload();
        physicalInterface.registerTimeTriggered(localDriveSpeed);

        // Initializing network messages
        simulator.payloads.CanMailbox.WriteableCanMailbox writeablecanmailbox =
                CanMailbox.getWriteableCanMailbox(
                        MessageDictionary.DRIVE_COMMAND_CAN_ID);
        canInterface.sendTimeTriggered(writeablecanmailbox, period);
        writeablecanmailbox = CanMailbox.getWriteableCanMailbox(
                MessageDictionary.DRIVE_SPEED_CAN_ID);
        mDriveSpeed = new DriveSpeedCanPayloadTranslator(writeablecanmailbox);
        canInterface.sendTimeTriggered(writeablecanmailbox, period);
        simulator.payloads.CanMailbox.ReadableCanMailbox readablecanmailbox =
                CanMailbox.getReadableCanMailbox(
                        MessageDictionary.DESIRED_FLOOR_CAN_ID);
        mDesiredFloor = new DesiredFloorCanPayloadTranslator(readablecanmailbox);
        canInterface.registerTimeTriggered(readablecanmailbox);
        readablecanmailbox = CanMailbox.getReadableCanMailbox(
                MessageDictionary.CAR_LEVEL_POSITION_CAN_ID);
        mCarLevelPosition = new IntCanPayloadTranslator(
                readablecanmailbox);
        canInterface.registerTimeTriggered(readablecanmailbox);
        readablecanmailbox = CanMailbox.getReadableCanMailbox(
                MessageDictionary.CAR_WEIGHT_CAN_ID);
        mCarWeight = new IntCanPayloadTranslator(readablecanmailbox);
        canInterface.registerTimeTriggered(readablecanmailbox);
        readablecanmailbox = CanMailbox.getReadableCanMailbox(
                MessageDictionary.EMERGENCY_BRAKE_CAN_ID);
        mSafety = new BitCanPayloadTranslator(readablecanmailbox);
        canInterface.registerTimeTriggered(readablecanmailbox);

        mDoorClosedArray = 
                new HashMap<Integer, BitCanPayloadTranslator>();
        mDoorMotorArray =
                new HashMap<Integer, DoorMotorCommandCanPayloadTranslator>();

        Object hallways[] = Hallway.replicationValues;
        int hallwayCount = hallways.length;
        Object directions[] = Direction.replicationValues;
        int numDirections = directions.length;
        Side sides[] = Side.values();
        int numSides = sides.length;

        // Setting up mailboxes so we can check the status of each of the doors
        for (int i = 0; i < hallwayCount; i++) {
            Hallway hallway = (Hallway) hallways[i];
            for (int j = 0; j < numSides; j++) {
                Side side = sides[j];
                int doorId = ReplicationComputer.computeReplicationId(hallway,
                        side);
                simulator.payloads.CanMailbox.ReadableCanMailbox
                doorClosedMailbox =
                CanMailbox.getReadableCanMailbox(
                        MessageDictionary.DOOR_CLOSED_SENSOR_BASE_CAN_ID + 
                        doorId);
                mDoorClosedArray.put(doorId, new BitCanPayloadTranslator(
                        doorClosedMailbox));
                canInterface.registerTimeTriggered(doorClosedMailbox);
                simulator.payloads.CanMailbox.ReadableCanMailbox
                doorMotorMailbox = CanMailbox.getReadableCanMailbox(
                        MessageDictionary.DOOR_MOTOR_COMMAND_BASE_CAN_ID +
                        doorId);
                mDoorMotorArray.put(doorId,
                        new DoorMotorCommandCanPayloadTranslator(
                                doorMotorMailbox, hallway, side));
                canInterface.registerTimeTriggered(doorMotorMailbox);
            }

        }

        // Setting up the network messages for the top and bottom hoistway
        // limits
        mHoistwayLimitArray = 
                new HashMap<Integer, BitCanPayloadTranslator>();
        for (int i = 0; i < numDirections; i++) {
            Direction direction = (Direction) directions[i];
            int directionId = ReplicationComputer.computeReplicationId(direction);
            simulator.payloads.CanMailbox.ReadableCanMailbox
            hoistwayLimitMailbox =
            CanMailbox.getReadableCanMailbox(
                    MessageDictionary.HOISTWAY_LIMIT_BASE_CAN_ID +
                    directionId);
            mHoistwayLimitArray.put(directionId,
                    new BitCanPayloadTranslator(
                            hoistwayLimitMailbox));
            canInterface.registerTimeTriggered(hoistwayLimitMailbox);
        }

        // Setting up network messages for the level sensors
        mLevelSensorArray = new BitCanPayloadTranslator[2];
        for (int i = 0; i < numDirections; i++) {
            Direction direction = (Direction) directions[i];
            simulator.payloads.CanMailbox.ReadableCanMailbox
            levelSensorMailbox = CanMailbox.getReadableCanMailbox(
                    MessageDictionary.LEVELING_BASE_CAN_ID +
                    ReplicationComputer.computeReplicationId(direction));
            mLevelSensorArray[ReplicationComputer.computeReplicationId(direction)] =
                    new BitCanPayloadTranslator(levelSensorMailbox);
            canInterface.registerTimeTriggered(levelSensorMailbox);
        }

        mAtFloorArray = new Utility.AtFloorArray(canInterface);
        timer.start(period);
    }

    private boolean isAnyDoorOpen() {
        Hallway hallways[] = Hallway.replicationValues;
        int numHallways = hallways.length;
        for (int i = 0; i < numHallways; i++) {
            Hallway hallway = hallways[i];
            Side sides[] = Side.values();
            int numSides = sides.length;
            for (int j = 0; j < numSides; j++) {
                Side side = sides[j];
                int doorId = ReplicationComputer.computeReplicationId(hallway,
                        side);
                if (!mDoorClosedArray.get(doorId).getValue() ||
                        (mDoorMotorArray.get(doorId).getDoorCommand() == DoorCommand.OPEN))
                    return true;
            }

        }
        return false;
    }

    private boolean isSafetyViolation() {
        if (mSafety.getValue())
            return true;
        Direction directions[] = Direction.replicationValues;
        int numDirections = directions.length;
        for (int i = 0; i < numDirections; i++) {
            Direction direction = directions[i];
            if ((mHoistwayLimitArray.get(ReplicationComputer.computeReplicationId(direction))).getValue())
                return true;
        }
        return false;
    }

    public void timerExpired(Object callbackData) {
        State newstate = state;
        double currentSpeed = localDriveSpeed.speed();
        int acc = 1;
        int desiredFloor = mDesiredFloor.getFloor();
	int position = mCarLevelPosition.getValue();
        int currentFloor = mAtFloorArray.getCurrentFloor();
        boolean isOverweight = mCarWeight.getValue() >= Elevator.MaxCarCapacity;
        boolean isLevel = false;
                if(mLevelSensorArray[ReplicationComputer.computeReplicationId(
                        Direction.DOWN)].getValue() &&
                        mLevelSensorArray[ReplicationComputer .computeReplicationId(Direction.UP)].getValue()){
				isLevel = true;
		}		
        
        int commitPointUp = (int)(((5*(desiredFloor - 1)) - ((currentSpeed * currentSpeed)/(2*acc))) * 1000) - cushion;
        int commitPointDown = (int)(((5*(desiredFloor - 1)) + ((currentSpeed * currentSpeed)/(2*acc))) * 1000) + cushion;
        switch (state) {
        case STATE_STOP:
            //State Actions
            localDrive.set(Speed.STOP, Direction.STOP);

            //#transition 'T6.5.1'
            if (isSafetyViolation()) {
                newstate = State.STATE_EMERGENCY;
                break;
            }

            if (isOverweight) {
                if (mLevelSensorArray[ReplicationComputer.computeReplicationId(Direction.DOWN)].getValue() &&
                        !mLevelSensorArray[ReplicationComputer.computeReplicationId(Direction.UP)].getValue()) {
                    //#transition 'T6.8'
                    newstate = State.STATE_LEVEL_UP;
                    break;
                }
                //#transition 'T6.12'
                newstate=State.STATE_STOP;                               
                break;
            }

            if ((desiredFloor != currentFloor) && !isAnyDoorOpen() && !isOverweight) {
                if ((desiredFloor - currentFloor > 0) && (localDriveSpeed.speed() == 0)) {
                    //#transition 'T6.1'
                    newstate = State.STATE_SLOW_UP;
                }
                else if ((desiredFloor - currentFloor < 0) && (localDriveSpeed.speed() == 0)){
                    //#transition 'T6.4'
                    newstate = State.STATE_SLOW_DOWN;
                }
                break;
            }

            if (isLevel || !isAnyDoorOpen()) {
                break;
            }

            if (mLevelSensorArray[ReplicationComputer.computeReplicationId(Direction.DOWN)].getValue() &&
                    !mLevelSensorArray[ReplicationComputer.computeReplicationId(Direction.UP)].getValue()) {
                //#transition 'T6.8'
                newstate = State.STATE_LEVEL_UP;
            }

            else if (mLevelSensorArray[ReplicationComputer.computeReplicationId(Direction.UP)].getValue()&&
                    !mLevelSensorArray[ReplicationComputer.computeReplicationId(Direction.DOWN)].getValue()) {
                //#transition 'T6.9'
                newstate = State.STATE_LEVEL_DOWN;
            }
            break;
        case STATE_LEVEL_UP:
            //State Actions
            localDrive.set(Speed.LEVEL, Direction.UP);

            if (isSafetyViolation()) {
                //#transition 'T6.5.*'
                newstate = State.STATE_EMERGENCY;
            }
            else if (isLevel) {
                //#transition 'T6.7'
                newstate = State.STATE_STOP;
            }
            break;
        case STATE_LEVEL_DOWN:
            //State Actions
            localDrive.set(Speed.LEVEL, Direction.DOWN);

            if (isSafetyViolation()) {
                //#transition 'T6.5.*'
                newstate = State.STATE_EMERGENCY;
            }
            else if (isLevel) {
                //#transition 'T6.10'
                newstate = State.STATE_STOP;
            }
            break;
        case STATE_SLOW_UP:
            //State Actions
            localDrive.set(Speed.SLOW, Direction.UP);

            if((localDriveSpeed.speed() == 0.25) && (position < commitPointUp)){
                //#transition 'T6.14'
                newstate = State.STATE_FAST_UP;
            }
            if (isSafetyViolation()) {
                //#transition 'T6.5.2'
                newstate = State.STATE_EMERGENCY;
            }
            else if (currentFloor == desiredFloor){
                if (isLevel){
                    //#transition 'T6.3'
                    newstate = State.STATE_STOP;
                }
                else if (mLevelSensorArray[ReplicationComputer.computeReplicationId(Direction.DOWN)].getValue() &&
                        !mLevelSensorArray[ReplicationComputer.computeReplicationId(Direction.UP)].getValue()) {
                    //#transition 'T6.8'
                    newstate = State.STATE_LEVEL_UP;
                }

                else if (mLevelSensorArray[ReplicationComputer.computeReplicationId(Direction.UP)].getValue()&&
                        !mLevelSensorArray[ReplicationComputer.computeReplicationId(Direction.DOWN)].getValue()) {
                    //#transition 'T6.9'
                    newstate = State.STATE_STOP;
                }
            }
            break;
        case STATE_SLOW_DOWN:
            //State Actions
            localDrive.set(Speed.SLOW, Direction.DOWN);

            if((localDriveSpeed.speed() == 0.25) && (position > commitPointDown)){
                //#transition 'T6.15'
                newstate = State.STATE_FAST_DOWN;
            }

            if (isSafetyViolation()) {
                //#transition 'T6.5.3'
                newstate = State.STATE_EMERGENCY;
                break;
            }
            else if (currentFloor == desiredFloor){
                if (isLevel) {
                    //#transition 'T6.2'
                    newstate = State.STATE_STOP;
                }
                else if (mLevelSensorArray[ReplicationComputer.computeReplicationId(Direction.DOWN)].getValue() &&
                        !mLevelSensorArray[ReplicationComputer.computeReplicationId(Direction.UP)].getValue()) {
                    //#transition 'T6.8'
                    newstate = State.STATE_STOP;
                }

                else if (mLevelSensorArray[ReplicationComputer.computeReplicationId(Direction.UP)].getValue()&&
                        !mLevelSensorArray[ReplicationComputer.computeReplicationId(Direction.DOWN)].getValue()) {
                    //#transition 'T6.9'
                    newstate = State.STATE_LEVEL_DOWN;
                }
                break;
            }
            break;

        case STATE_FAST_UP:

            localDrive.set(Speed.FAST, Direction.UP);

            if(position > commitPointUp) {
                //#transition 'T6.13'
                newstate = State.STATE_SLOW_UP;            }
            break;
        case STATE_FAST_DOWN:

            localDrive.set(Speed.FAST, Direction.DOWN);

            if(position < commitPointDown) {
                //#transition 'T6.16'
                newstate = State.STATE_SLOW_DOWN;
            }
            break;
        case STATE_EMERGENCY:
            //State Actions
            localDrive.set(Speed.STOP, Direction.STOP);

            break;
        default:
            throw new RuntimeException("State " + state + " was not recognized.");
        }

        //To Convert Speed from double values to enumerated values 
        Speed targetSpeed;
        int newlocalDriveSpeed = (int)(localDriveSpeed.speed()*100);

        if(newlocalDriveSpeed == 0)
            targetSpeed = Speed.STOP;
        else if(newlocalDriveSpeed <=5)
            targetSpeed = Speed.LEVEL;
        else if(newlocalDriveSpeed <=25)
            targetSpeed = Speed.SLOW;
        else if(newlocalDriveSpeed <=500)
            targetSpeed = Speed.FAST;
        else
            throw new RuntimeException("Unknown Speed");

        //Set the network Messages
        mDriveSpeed.set(targetSpeed,localDriveSpeed.direction());

        if (state != newstate)
            log(new Object[] { "Transition from ", state, " to ", newstate });

        setState("STATE", state.toString());
        state = newstate;

        // Schedule the next iteration of the controller
        // You must do this at the end of the timer callback in order to
        // restart the timer
        timer.start(period);
    }
}


