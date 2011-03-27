package de.htwdd.robotics.wienert;

import java.awt.Point;
import javax.vecmath.Vector2d;

public class GridPoint {
	public int x;
	public int y;
	public double clearance;
	
	// approximate direction of pixel
	private Vector2d direction;
	// 90 degree of the direction
	private Vector2d normal;

	public GridPoint(Point p) {
		this.x = p.x;
		this.y = p.y;
		direction = new Vector2d(0, 0);
		
	}
	public GridPoint(int x, int y) {
		this.x = x;
		this.y = y;
		direction = new Vector2d(0, 0);
	}
	
	/**
	 * Approximate direction calculated and set
	 * Left and Right neigbors are used
	 * @param predecessor
	 * @param successor
	 * @return
	 */
	public Vector2d calcDirection(Point predecessor, Point successor) {
		int x = successor.x - predecessor.x;
		int y = successor.y - predecessor.y;
		direction =  new Vector2d(x, y);
		direction.normalize();
		return direction;
	}
	
	/**
	 * 90 degree vector on direciton
	 * @return
	 */
	public Vector2d calcNormalVector() {
		normal = new Vector2d();
		normal.x = 1;
		normal.y = -direction.x / direction.y;
		normal.normalize();
		return normal;
	}
	
	public Point toPoint() {
		return new Point(x,y);
	}
}
