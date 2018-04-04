package aletrainsystem.enums;

public enum SleeperColor {
	RED, GREEN, BLUE, YELLOW, WHITE, BLACK, BROWN, NONE;
	
	public static SleeperColor convertFromLejosColor(int lejosInt) {
		switch (lejosInt) {
		case 0:
			return RED;
		case 1:
			return GREEN;
		case 2:
			return BLUE;
		case 3:
			return YELLOW;
		case 6:
			return WHITE;
		case 7:
			return BLACK;
		case 13:
			return BROWN;
		default:
			return NONE;
		}
	}
	
	public static SleeperColor regularSleeper() {
		return SleeperColor.BLACK;
	}
	
	public PointSwitchConnectorEnum convertToConnector() {
		switch (this) {
		
		case RED:
			return PointSwitchConnectorEnum.ENTRY;
		case YELLOW:
			return PointSwitchConnectorEnum.THROUGH;
		case GREEN:
			return PointSwitchConnectorEnum.DIVERT;
		
		default:
			return null;
		}
	}
}