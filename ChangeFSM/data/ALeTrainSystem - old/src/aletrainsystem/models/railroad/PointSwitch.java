package aletrainsystem.models.railroad;

import java.util.HashMap;
import java.util.Map;

import aletrainsystem.enums.PointSwitchConnectorEnum;
import aletrainsystem.models.RailComponentId;

public class PointSwitch implements RailComponent {
	private Map<PointSwitchConnectorEnum, PointSwitchConnector> connectors;
	private RailComponentId pointSwitchId;
	
	public PointSwitch(RailComponentId pointSwitchId){
		this.pointSwitchId = pointSwitchId;
		connectors = new HashMap<PointSwitchConnectorEnum, PointSwitchConnector>();
		for (PointSwitchConnectorEnum connectorType : PointSwitchConnectorEnum.values()) {
			addConnector(new PointSwitchConnector(this, connectorType));
		}
	}
	
	public RailComponentId id(){
		return pointSwitchId;
	}
	
	
	public Map<PointSwitchConnectorEnum, PointSwitchConnector> getConnectors() {
		return connectors;
	}
	
	public PointSwitchConnector getConnector(PointSwitchConnectorEnum type){
		return connectors.get(type);
	}

	public void addConnector(PointSwitchConnector connector) {
		connectors.put(connector.getConnectorType(), connector);
	}

	@Override
	public boolean equals(Object other){
		return this.pointSwitchId.equals(((PointSwitch)other).id());
	}
}
