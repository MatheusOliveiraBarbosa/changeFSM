package aletrainsystem.mapcontroller;

import aletrainsystem.models.Navigation.Position;
import aletrainsystem.models.railroad.RailComponent;
import no.ntnu.item.arctis.runtime.Block;

public class MapController extends Block {

	public aletrainsystem.models.railroad.Railroad map;
	public aletrainsystem.models.Navigation.Position position;

	public void initPosition(int sizeOfParentObject) {
		position = new Position(new RailComponent[] {}, sizeOfParentObject); // ADD ENTRYPOINT HERE
	}

}
