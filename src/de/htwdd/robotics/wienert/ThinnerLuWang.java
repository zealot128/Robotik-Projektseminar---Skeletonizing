package de.htwdd.robotics.wienert;

import de.htwdd.robotics.datagrid.ChangingUniversalGridCellOperation;

/**
 * Thinning Operation, 07chapter6.pdf
 * @deprecated use ThinnerSuen instead
 * @author zealot64
 * 
 */
public class ThinnerLuWang extends Thinner {
	/* needed for internal class changingUniversalGridCellOperation */
	private boolean stateChanged;
	private int step;
	
	@Override
	public void thinn() {
		do {
			stateChanged = false;
			for (step = 1; step <= 2; step++) {
				map.traverse(new ChangingUniversalGridCellOperation<SkeletonCell>() {
					@Override
					public SkeletonCell process(int row, int col,
							SkeletonCell cell) {
						if (cell.status == SkeletonCell.STATE_OCCUPIED
								&& cellIsThinnable(row, col, step)) {
							stateChanged = true;
							cell.thinnMe();
						}
						return cell;
					}
				});
			}
		} while (stateChanged);
	}

	/*
	 * 1 2 3 
	 * 8   4 
	 * 7 6 5
	 */
	
	/**
	 * checks if the cell at given position can be thinned acc to algorithm
	 */
	private boolean cellIsThinnable(int row, int col, int step) {
		SkeletonCell currentCell = map.get(row, col);

		if (currentCell.status != SkeletonCell.STATE_OCCUPIED) {
			return false;
			// Cell is occupied by a wall or similar
		}
		// Neighbors of the current cell
		int[] n = this.transformToArray(collectNeighborsFor(row, col));

		int numberOfBlack = this.numberOfBlack(n);
		int numberOfChanges = this.numberOfChanges(n);
		if (numberOfBlack < 2 || numberOfBlack > 6) {
			return false;
		}
		if (numberOfChanges != 1) {
			return false;
		}
		boolean[] p = new boolean[8];
		for (int i = 0; i < n.length; i++) {
			p[i] = n[i] == 1;
		}

		if (step == 1) {
			// c1
			boolean cond1 = p[0] && p[2] && p[4];
			boolean cond2 = p[2] && p[4] && p[6];
			return !cond1 || !cond2;
		} else {
			// c2
			boolean cond1 = p[0] && p[2] && p[6];
			boolean cond2 = p[0] && p[4] && p[6];
			return !cond1 || !cond2;
		}
	}

}
