package de.htwdd.robotics.wienert;

import java.lang.reflect.Array;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.htwdd.robotics.SingleThreadedProcessor;
import de.htwdd.robotics.map.OccupancyGridMap;
import de.htwdd.robotics.map.UniversalGridMap;

public class ThinningProcessor extends SingleThreadedProcessor {
	private static final Logger log = LoggerFactory
			.getLogger(ThinningProcessor.class);

	public ThinningProcessor(String name, int loopDelay) {
		super(name, loopDelay);
	}

	public OccupancyGridMap map;
	public UniversalGridMap<SkeletonCell> skeletonMap;
	public int rowFrom;
	public int rowTo;
	public int colFrom;
	public int colTo;

	@Override
	protected void loop() {
	}

	public void testme() {
		initialize();

		logNeighbors(collectNeighborsFor(50, 50));
		loop();
	}

	/**
	 * Copy given occupancy grid map in our skeleton grid map
	 */
	public void initialize() {
		skeletonMap = new UniversalGridMap<SkeletonCell>(map.getGridSize(),
				map.getFirstRow(), map.getFirstColumn(), map.getLastRow(),
				map.getLastColumn());
		for (int i = map.getFirstRow(); i < map.getLastRow(); i++) {
			for (int j = map.getFirstColumn(); j < map.getLastColumn(); j++) {
				int status = map.isFree(i, j) ? SkeletonCell.STATE_FREE
						: SkeletonCell.STATE_OCCUPIED;
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

	public void setSkeletonMap(UniversalGridMap<SkeletonCell> skeletonGrid) {
		skeletonMap = skeletonGrid;
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

	public void thinningStep() {
		// circumvene border cases for first/last rows, cause they have no
		// neighbors on either side
		
		for (int row = rowFrom + 1; row < rowTo - 1; row++) {
			for (int col = colFrom + 1; col < colTo - 1; col++) {
			}
		}
	}
}
