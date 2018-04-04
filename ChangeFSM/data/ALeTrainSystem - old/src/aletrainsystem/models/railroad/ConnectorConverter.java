package aletrainsystem.models.railroad;

import java.util.function.Function;

import aletrainsystem.enums.PointSwitchConnectorEnum;
import bluebrick4j.model.BrickType;

public class ConnectorConverter {
	
	public static Function<Integer, PointSwitchConnectorEnum> convert(BrickType type){
		if (type == BrickType.LEFTHANDPOINTSWITCH){
			return ConnectorConverter::leftHandPointSwitchConverter;
		}
		else if (type == BrickType.RIGHTHANDPOINTSWITCH){
			return ConnectorConverter::rightHandPointSwitchConverter;
		}
		return null;
	}

	private static PointSwitchConnectorEnum rightHandPointSwitchConverter(int i) {
		switch (i) {
		case 0:
			return PointSwitchConnectorEnum.ENTRY;
		case 1:
			return PointSwitchConnectorEnum.DIVERT;
		case 2:
			return PointSwitchConnectorEnum.THROUGH;
		default:
			throw new IllegalArgumentException("Cannot recognize integer ".concat(String.valueOf(i)));
		}
	}
	
	private static PointSwitchConnectorEnum leftHandPointSwitchConverter(int i) {
		switch (i) {
		case 0:
			return PointSwitchConnectorEnum.ENTRY;
		case 1:
			return PointSwitchConnectorEnum.THROUGH;
		case 2:
			return PointSwitchConnectorEnum.DIVERT;
		default:
			throw new IllegalArgumentException("Cannot recognize integer ".concat(String.valueOf(i)));
		}
	}
}
