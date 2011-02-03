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
	static final int STATE_THINNED = 0;

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

	public void thinnMe() {
		this.status = SkeletonCell.STATE_THINNED;
		this.thinned = true;		
	}
	
	/*
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
	*/





}
