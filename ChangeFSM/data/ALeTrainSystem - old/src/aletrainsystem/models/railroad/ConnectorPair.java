package aletrainsystem.models.railroad;

import java.util.Iterator;

import aletrainsystem.enums.PointSwitchConnectorEnum;
import aletrainsystem.models.Pair;

public class ConnectorPair extends Pair<PointSwitchConnector, PointSwitchConnector> implements Iterable<PointSwitchConnector> {

	public ConnectorPair(PointSwitchConnector first, PointSwitchConnector second) {
		super(first, second);
	}
		
	public boolean bothOfType(PointSwitchConnectorEnum connectorType) {
		return first.getConnectorType() == connectorType 
				&& second.getConnectorType() == connectorType;
	}
	
	public boolean hasBothEnds() {
		return first != null && second != null;
	}

	@Override
	public Iterator<PointSwitchConnector> iterator() {
		return new ConnectorPairIterator();
	}

	private class ConnectorPairIterator implements Iterator<PointSwitchConnector> {

		private int index = 0;
		
		@Override
		public boolean hasNext() {
			return index < 2;
		}

		@Override
		public PointSwitchConnector next() {
			return index++ < 1 ? first() : second();
		}
		
	}
}
