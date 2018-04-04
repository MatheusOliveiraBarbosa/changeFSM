package aletrainsystem.measuretimemicros;

import no.ntnu.item.arctis.runtime.Block;

public class MeasureTimeMicros extends Block {

	public long startTime;

	public void storeStartTime() {
		startTime = System.nanoTime();
	}

	public long calculateDuration() {
		return (System.nanoTime() - startTime) / 1000;
	}

	public void abort() {
		startTime = 0;
	}

}
