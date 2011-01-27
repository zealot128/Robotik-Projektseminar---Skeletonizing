package de.htwdd.robotics.wienert;

import java.awt.Point;
import java.util.ArrayList;

public class SkeletonCell {
	public SkeletonCell(int state, int row, int column) {
		status = state;
		this.col = column;
		this.row = row;
	}

	public int clearance;
	public ArrayList<Point> basePoints;

	public int row;
	public int col;

	static final int STATE_FREE = 0;
	static final int STATE_OCCUPIED = 1;
	static final int STATE_THINNED = 2;

	public int status;

	public int getClearance() {
		return clearance;
	}

	public void setClearance(int clearance) {
		this.clearance = clearance;
	}

	public ArrayList<Point> getBasePoints() {
		return basePoints;
	}

	public void setBasePoints(ArrayList<Point> basePoints) {
		this.basePoints = basePoints;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public boolean thinned;

	public boolean thinnableStefan2(SkeletonCell[][] neighbors) {
		if (status != SkeletonCell.STATE_OCCUPIED) {
			return false;
			// Cell is occupied by a wall or similar
		}
		int[] n = this.transformToArray(neighbors);		
		for (int i : n) {
			n[i] = Math.abs(n[i] - 1);
		}		
		boolean[] p = new boolean[8];
		for (int i : n) {
			p[i] = n[i] == 1;
		}
		
		int numberOfBlack = this.numberOfBlack(n);
		int numberOfChanges = this.numberOfChanges(n);
		if (numberOfBlack < 2 || numberOfBlack > 6) {
			return false;
		}
		if (numberOfChanges != 1) {
			return false;
		}
		//STEFAN-1 Conditions are missing
		//if (n[1] * p[3] * p[7] == 0 || 
		return true;
		
	}
	
	public boolean thinnableLuWang(SkeletonCell[][] neighbors, int step) {

		if (status != SkeletonCell.STATE_OCCUPIED) {
			return false;
			// Cell is occupied by a wall or similar
		}
		int[] n = this.transformToArray(neighbors);
		// invert neighborhood --> make Free Cells "black" and occupied cells
		// white
		for (int i : n) {
			n[i] = Math.abs(n[i] - 1);
		}

		int numberOfBlack = this.numberOfBlack(n);
		if (!(numberOfBlack > 2 && numberOfBlack < 7)) {
			return false;
		}
		int numberOfChanges = this.numberOfChanges(n);
		if (numberOfChanges != 1) {
			return false;
		}

		boolean[] p = new boolean[8];
		for (int i : n) {
			p[i] = n[i] == 1;
		}
		if (step == 1) {
			boolean cond = p[3] && p[5] && (p[1] || p[7]);
			if (cond)
				return false;
		} else {
			boolean cond = p[1] && p[7] && (p[3] || p[5]);
			if (cond)
				return false;
		}

		return true;
	}

	public int numberOfBlack(int[] n) {
		int sum = 0;
		for (int i : n) {
			sum += n[i];
		}
		return sum ; // excluding self

	}
	public int numberOfBlack(SkeletonCell[][] neighbors) {
		int sum = 0;
		for (SkeletonCell[] skeletonCells : neighbors) {
			for (SkeletonCell skeletonCell : skeletonCells) {
				sum += skeletonCell.status;
			}
		}
		return sum - status; // excluding self

	}

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

	public int connectivity(SkeletonCell[][] neighbors) {
		int[] n = transformToArray(neighbors);
		return connectivity(n);
	}

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
