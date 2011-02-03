package de.htwdd.robotics.wienert;

import java.io.IOException;
import java.nio.ByteOrder;

import de.htwdd.robotics.DefaultScitosConnections;
import de.htwdd.robotics.ScitosRobot;
import de.htwdd.robotics.depth.DepthImage;
import de.htwdd.robotics.map.OccupancyGridMap;
import de.htwdd.robotics.map.OccupancyGridMapIO;
import de.htwdd.robotics.map.UniversalGridMap;
import de.htwdd.robotics.map.container.GridMapContainer;
import de.htwdd.robotics.map.container.GridMapContainers;
import de.htwdd.robotics.path.Path;
import de.htwdd.robotics.state.container.StateContainer;
import de.htwdd.robotics.state.container.StateProvider;

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

	public StateProvider<Path> pathProvider;

	public UniversalGridMap<SkeletonCell> skeletonGrid;

	public UniversalGridMap<SkeletonCell> getSkeletonGrid() {
		return skeletonGrid;
	}

	public void setSkeletonGrid(UniversalGridMap<SkeletonCell> skeletonGrid) {
		this.skeletonGrid = skeletonGrid;
	}

	public MyRobot(String hostname) throws IOException {
		super(new DefaultScitosConnections(hostname), ByteOrder.BIG_ENDIAN,
				true, true, true);

		mapContainer = GridMapContainers.newInstance();
		String filename = "htwZ2_1.png";
		mapContainer.set(OccupancyGridMapIO.importFromImage(filename, 0, 0, 5));
		// / Own Processor
	}
}
