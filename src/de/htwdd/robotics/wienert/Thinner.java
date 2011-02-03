package de.htwdd.robotics.wienert;

import de.htwdd.robotics.map.UniversalGridMap;

public abstract class Thinner {

	/**
	 * Method, which thinns the current map
	 * Operation is performed directly onto the map
	 */
	abstract public void thinn();
	
	
	protected UniversalGridMap<SkeletonCell> map;

	public void setMap(UniversalGridMap<SkeletonCell> map) {
		this.map = map;
	}
	
	/**
	 * Function for getting al the neighbors of a specific cell
	 * @param my_row
	 * @param my_col
	 * @return
	 */
	public SkeletonCell[][] collectNeighborsFor(int my_row, int my_col) {
		SkeletonCell[][] neighborhood = new SkeletonCell[3][3];
		for (int row = -1; row < 2; row++) {
			for (int col = -1; col < 2; col++) {
				neighborhood[row + 1][col + 1] = map.get(row + my_row,
						col + my_col);
			}
		}
		return neighborhood;
	}	
	
	/**
	 * Transform into a simple 0/1 Array for better processing
	 * @param neighbors
	 * @return
	 */
	public int[] transformToArray(SkeletonCell[][] neighbors) {
		int[] n = new int[8];
		// n[0] = Math.abs( neighbors[1][1].status );
		n[0] = Math.abs(neighbors[0][0].status);
		n[1] = Math.abs(neighbors[0][1].status);
		n[2] = Math.abs(neighbors[0][2].status);
		n[3] = Math.abs(neighbors[1][2].status);
		n[4] = Math.abs(neighbors[2][2].status);
		n[5] = Math.abs(neighbors[2][1].status);
		n[6] = Math.abs(neighbors[2][0].status);
		n[7] = Math.abs(neighbors[1][0].status);
		return n;
	}	
	/**
	 * Number of Black Pixel == B(P)
	 * @param neighbors
	 * @return
	 */
	public int numberOfBlack(SkeletonCell[][] neighbors) {
		int sum = 0;
		for (SkeletonCell[] skeletonCells : neighbors) {
			for (SkeletonCell skeletonCell : skeletonCells) {
				sum += skeletonCell.status;
			}
		}
		return sum - 1; // excluding self

	}	
	
	/**
	 * 
	 * @param n
	 * @return
	 */
	public int numberOfBlack(int[] n) {
		int sum = 0;
		for (int i : n) {
			sum += i;
		}
		return sum - 1; // excluding self

	}	


	/**
	 * A(P) according to 07chapter6.pdf page 3
	 * 
	 * @return
	 */
	public int numberOfChanges(int[] n) {
		int sum = 0;
		for (int i = 0; i < 8; i++) {
			// System.out.println(":" + i + "-" + (i+1) % 8);
			if (n[i] == 0 && n[(i + 1) % 8] == 1) {
				sum += 1;
			}
		}
		return sum;
	}

	public int numberOfChanges(SkeletonCell[][] neighbors) {
		return numberOfChanges(transformToArray(neighbors));
	}
	/**
	 * Connectivity C(P)
	 * @param neighbors
	 * @return
	 */
	public int connectivity(SkeletonCell[][] neighbors) {
		int[] n = transformToArray(neighbors);
		return connectivity(n);
	}
	/**
	 * C(P)
	 * @param n
	 * @return
	 */
	public int connectivity(int[] n) {
		int connectivity = 0;
		for (int i = 1; i < 8; i += 2) {
			int ind2 = (i + 1) % 8;
			int ind3 = (i + 2) % 8;
			if (n[i] == 0 && (n[ind2] + n[ind3] > 0)) {
				connectivity += 1;
			}
		}
		return connectivity;
	}


	
	
}
