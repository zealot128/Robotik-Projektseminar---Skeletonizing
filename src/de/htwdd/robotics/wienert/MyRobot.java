package de.htwdd.robotics.wienert;

import java.io.IOException;
import java.nio.ByteOrder;

import de.htwdd.robotics.DefaultScitosConnections;
import de.htwdd.robotics.ScitosRobot;
import de.htwdd.robotics.depth.DepthImage;
import de.htwdd.robotics.depth.DepthImageRelativeConverter;
import de.htwdd.robotics.map.OccupancyGridMap;
import de.htwdd.robotics.map.container.GridMapContainer;
import de.htwdd.robotics.map.container.GridMapContainers;
import de.htwdd.robotics.state.container.StateContainer;
import de.htwdd.robotics.state.container.StateContainers;

/**
 * A custom SCITOS robot.
 */
public class MyRobot extends ScitosRobot {

	private StateContainer<DepthImage> depthContainer;
	
	
	
	private GridMapContainer<OccupancyGridMap> mapContainer;

    public GridMapContainer<OccupancyGridMap> getMapContainer() {
		return mapContainer;
	}

    @Override
    public StateContainer<DepthImage> getDepthImageContainer() {
    	return depthContainer;
    }


	/**
     * Constructs a new custom SCITOS robot that connects to a remote
program to get its sensor data.
     *
     * @param hostname The name or IP address of the remote host.
     * @throws IOException If the properties or the map could not be loaded.
     */
    public MyRobot(String hostname) throws IOException {
        super(new DefaultScitosConnections(hostname),
ByteOrder.BIG_ENDIAN, true, true, true);
        //loadProperties();

        mapContainer = GridMapContainers.newInstance();
        depthContainer = StateContainers.newInstance();

        DepthImageRelativeConverter imgc = new DepthImageRelativeConverter(
        		super.getDepthImageContainer(),
getPtuDeflectionContainer(),depthContainer);

        addProcessor(imgc);

        /// Own Processor


    }
}
