package de.htwdd.robotics.wienert;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.htwdd.robotics.SingleThreadedProcessor;
import de.htwdd.robotics.map.OccupancyGridMap;

public class ThinningProcessor extends SingleThreadedProcessor {
	private static final Logger log = LoggerFactory.getLogger(ThinningProcessor.class);
	
	public ThinningProcessor(String name, int loopDelay) {
		super(name, loopDelay);
	}
	
	public OccupancyGridMap map;
	@Override
	protected void loop() {
		log.debug(Double.toString(map.getOccupancy(0, 0)));

	}
	public void testme() {
		loop();
		
	}
	public void setGridMap(OccupancyGridMap occupancyGridMap) {
		map=occupancyGridMap;
	}

}
