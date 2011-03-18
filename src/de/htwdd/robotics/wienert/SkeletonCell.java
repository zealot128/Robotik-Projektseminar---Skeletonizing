package de.htwdd.robotics.wienert;


public class SkeletonCell {
	public SkeletonCell(int state, int row, int column) {
		status = state;
		this.col = column;
		this.row = row;
	}


	public int row;
	public int col;
	public boolean visited=false;


	static final int STATE_FREE = 0;
	static final int STATE_OCCUPIED = 1;
	static final int STATE_THINNED = 0;

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
