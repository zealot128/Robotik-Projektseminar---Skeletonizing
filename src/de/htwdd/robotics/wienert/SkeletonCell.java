package de.htwdd.robotics.wienert;

import java.awt.Point;


public class SkeletonCell {
	public SkeletonCell(int state, int row, int column) {
		status = state;
		this.col = column;
		this.row = row;
		this.isCritical = false;
	}


	public int row;
	public int col;
	public boolean visited=false;
	public double clearance;
	public Point basePoint; // one Basepoint
	public boolean isCritical;

	static final int STATE_FREE = 0;
	static final int STATE_OCCUPIED = 1;
	static final int STATE_THINNED = 0;

	public int special;
	static final int SPECIAL_NORMAL = 0;
	static final int SPECIAL_START = 1;
	
	public int status;
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
