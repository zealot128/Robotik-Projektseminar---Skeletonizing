package de.htwdd.robotics.wienert;

import java.awt.Point;
import java.util.ArrayList;
import de.htwdd.robotics.datagrid.ChangingUniversalGridCellOperation;

public class ThinnerSuen extends Thinner {

	private boolean stateChanged;
	private int step;
	private java.util.List<Point> todelete;

	@Override
	public void thinn() {
		smooth(); // just a try
		smooth();
		smooth();
		todelete = new ArrayList<Point>();
		do {
			stateChanged = false;
			for (step = 1; step <= 2; step++) {
				map.traverse(new ChangingUniversalGridCellOperation<SkeletonCell>() {
					@Override
					public SkeletonCell process(int row, int col, SkeletonCell cell) {
						if (cell.status == SkeletonCell.STATE_OCCUPIED
								&& cellIsThinnable(row, col, step)
								&& !cell.thinned) {
							cell.thinned = true;
							stateChanged = true;
							todelete.add(new Point(row, col));
							// cell.thinnMe();
						}
						return cell;
					}
				});
				carryOutDeletion();
			}

			//return;
		} while (stateChanged);
		System.out.println("Thinning Done");
	}

	/**
	 * Delete all marked cells
	 * According to algorithm deletion is carried
	 * out after each step
	 */
	private void carryOutDeletion() {
		for (Point point : todelete) {
			SkeletonCell cell = map.get(point.x, point.y);
			cell.thinnMe();
		}
		todelete.clear();

	}

	/**
	 * Check if given cell in given step of algorithm 
	 * satisfies all conditions
	 * @param row
	 * @param col
	 * @param step
	 * @return
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
		//   2<= B(n) <= 6
		if (numberOfBlack < 2 || numberOfBlack > 6) {
			return false;
		}
		//   C == 1		
		if (numberOfChanges != 1) {
			return false;
		}
		// Convert to boolean
		boolean[] p = new boolean[8];
		for (int i = 0; i < n.length; i++) {
			p[i] = n[i] == 1;
		}
		if (step == 1) {
			// At least one of P2, P4, P6 are background
			boolean cond1 = (n[0] + n[2] + n[4] < 3);
			// At least one of P4, P6, P8 are background
			boolean cond2 = (n[2] + n[4] + n[6] < 3);
			return cond1 && cond2;
		} else {
			// At least one of P2, P4, P8 are background
			boolean cond1 = (n[0] + n[2] + n[6] < 3);
			// At least one of P2, P6, P8 are background
			boolean cond2 = (n[0] + n[4] + n[6] < 3);
			return cond1 && cond2;			
		}

	}

	/**
	 * Choi have a different set: 0 1 2  
	 * 0 p9 p2 p3 
	 * 1 p8 p1 p4 
	 * 2 p7 p6 p5
	 * 
	 * p1 is not needed
	 * 
	 * @param neighbors
	 * @return
	 */
	public int[] transformToArray(SkeletonCell[][] neighbors) {
		int[] n = new int[8];
//		// n[0] = Math.abs( neighbors[1][1].status ); // Center Cell
//		n[0] = Math.abs(neighbors[0][1].status);
//		n[1] = Math.abs(neighbors[0][2].status);
//		n[2] = Math.abs(neighbors[1][2].status);
//		n[3] = Math.abs(neighbors[2][2].status);
//		n[4] = Math.abs(neighbors[2][1].status);
//		n[5] = Math.abs(neighbors[2][0].status);
//		n[6] = Math.abs(neighbors[1][0].status);
//		n[7] = Math.abs(neighbors[0][0].status);
		// Try new set acc. to http://www.rupj.net/portfolio/docs/skeletonization.pdf
		n[0] = Math.abs(neighbors[1][2].status);
		n[1] = Math.abs(neighbors[0][2].status);
		n[2] = Math.abs(neighbors[0][1].status);
		n[3] = Math.abs(neighbors[0][0].status);
		n[4] = Math.abs(neighbors[1][0].status);
		n[5] = Math.abs(neighbors[2][0].status);
		n[6] = Math.abs(neighbors[2][1].status);
		n[7] = Math.abs(neighbors[2][2].status);		
		return n;
	}

	
	
}
