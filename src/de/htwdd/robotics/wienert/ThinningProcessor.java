package de.htwdd.robotics.wienert;

import java.lang.reflect.Array;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.htwdd.robotics.SingleThreadedProcessor;
import de.htwdd.robotics.map.OccupancyGridMap;
import de.htwdd.robotics.map.UniversalGridMap;
import de.htwdd.robotics.map.container.GridMapContainer;

public class ThinningProcessor extends SingleThreadedProcessor {
	private static final Logger log = LoggerFactory
			.getLogger(ThinningProcessor.class);
	private GridMapContainer<OccupancyGridMap> gridMapContainer;
	private Thinner thinningAlgorithm;
	public OccupancyGridMap map;
	public UniversalGridMap<SkeletonCell> skeletonMap;
	public int rowFrom;
	public int rowTo;
	public int colFrom;
	public int colTo;
	private GridMapContainer<UniversalGridMap<SkeletonCell>> skeletonContainer;

	public ThinningProcessor(
			GridMapContainer<OccupancyGridMap> gridMapContainer,
			GridMapContainer<UniversalGridMap<SkeletonCell>> skeletonContainer, Thinner thinningAlgorithm
	) {
		super("thinning-processor", 10000);
		this.skeletonContainer = skeletonContainer;
		this.gridMapContainer = gridMapContainer;
		this.thinningAlgorithm = thinningAlgorithm;
		initialize();
		this.thinningAlgorithm.map = this.skeletonContainer.get();
		this.thinningAlgorithm.thinn();
	}

	
	@Override
	protected void loop() {
		skeletonContainer.mapChanged(skeletonContainer.get().getBounds());
	}

	public void testme() {
		initialize();
		
		logNeighbors(collectNeighborsFor(50, 50));
		loop();
	}

	/**
	 * Copy given occupancy grid map in our skeleton grid map
	 */
	private void initialize() {
		//this.skeletonContainer = skeletonContainer;
		//this.gridMapContainer = gridMapContainer;
		//this.thinningAlgorithm = thinningAlgorithm;

		map = gridMapContainer.get();
		skeletonContainer.set( new UniversalGridMap<SkeletonCell>(map.getGridSize(),
				map.getFirstRow(), map.getFirstColumn(), map.getLastRow(),
				map.getLastColumn()));
		skeletonMap = skeletonContainer.get();
		for (int i = map.getFirstRow(); i <= map.getLastRow(); i++) {
			for (int j = map.getFirstColumn(); j <= map.getLastColumn(); j++) {
				int status = map.isFree(i, j) ? SkeletonCell.STATE_OCCUPIED
						: SkeletonCell.STATE_FREE;
				SkeletonCell skeletonCell = new SkeletonCell(status, i, j);
				skeletonMap.set(i, j, skeletonCell);
			}
		}
	}

	public void setGridMap(OccupancyGridMap occupancyGridMap) {
		map = occupancyGridMap;
		rowFrom = map.getFirstRow();
		rowTo = map.getLastRow();
		colFrom = map.getFirstColumn();
		colTo = map.getLastColumn();

	}

	public void setSkeletonMap(GridMapContainer<UniversalGridMap<SkeletonCell>> skeletonGrid) {
		skeletonContainer = skeletonGrid;
		
	}

	/* Collect all adjacent neighbors for cell */
	public SkeletonCell[][] collectNeighborsFor(int my_row, int my_col) {
		
		SkeletonCell[][] neighborhood = new SkeletonCell[3][3];
		for (int row = -1; row < 2; row++) {
			for (int col = -1; col < 2; col++) {
				neighborhood[row + 1][col + 1] = skeletonMap.get(row + my_row,
						col + my_col);
			}
		}
		return neighborhood;
	}

	public void logNeighbors(SkeletonCell[][] n) {
		String buf = "\n";
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				buf += n[i][j].status + " ";
			}
			buf += "\n";
		}
		log.debug(buf);
	}

}
