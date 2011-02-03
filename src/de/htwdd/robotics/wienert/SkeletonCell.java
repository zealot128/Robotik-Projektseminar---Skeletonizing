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

}
