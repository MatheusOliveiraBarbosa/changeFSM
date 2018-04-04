package aletrainsystem.calibration;

import lejos.hardware.Button;
import lejos.hardware.Sound;
import no.ntnu.item.arctis.runtime.Block;

public class Calibration extends Block implements Runnable {
	public static final int UP = 0x1;
	public static final int ENTER = 0x2;
	public static final int DOWN = 0x4;
	public static final int RIGHT = 0x8;
	public static final int LEFT = 0x10;
	public static final int ESCAPE = 0x20;
	public int id = 0;
	public Thread calibrationThread = null;

	public void init() {
		Button.LEDPattern(3);
		Sound.beepSequenceUp();
		calibrationThread = new Thread(this);
		calibrationThread.start();
	}

	@Override
	public void run() {
		Thread thisThread = Thread.currentThread();
		System.out.println("Turn on pod battery box");
		System.out.println("Press any key when ready");
		Button.waitForAnyPress();
		while (thisThread == calibrationThread) {
			System.out.println("up/down for big adjustments");
			System.out.println("right/left for small adjustments");
			System.out.println("Press ENTER when finished");
			id = Button.waitForAnyPress();
			if (id == ENTER) {
				stop();
			} else {
				if (id == UP)
					sendToBlock("DEGREES", 15);
				else if (id == DOWN)
					sendToBlock("DEGREES", -15);
				else if (id == RIGHT)
					sendToBlock("DEGREES", 2);
				else if (id == LEFT)
					sendToBlock("DEGREES", -2);
				else
					System.out.println("Invalid button");
			}
		}
	}

	public void stop() {
		Button.LEDPattern(6);
		calibrationThread = null;
		sendToBlock("FINISHED");
	}
}
