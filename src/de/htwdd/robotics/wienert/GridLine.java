package de.htwdd.robotics.wienert;

import java.awt.Point;
import java.util.ArrayList;

public class GridLine {
	public ArrayList<Point> points = new ArrayList<Point>();
	
	public void addPoint(int x,int y) {
		points.add(new Point(x, y));
	}
	
}
