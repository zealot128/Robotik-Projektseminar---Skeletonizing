package de.htwdd.robotics.wienert;

import java.awt.Point;
import java.util.ArrayList;

import de.htwdd.robotics.map.OccupancyGridMap;
import de.htwdd.robotics.map.UniversalGridMap;

public class GridLine {
	public ArrayList<GridPoint> points = new ArrayList<GridPoint>();
	public Point lastFoundPoint; 
	private OccupancyGridMap map;
	
	public void addPoint(int x,int y) {
		points.add(new GridPoint(x, y));
	}
	public void addPoint(Point p) {
		points.add(new GridPoint(p.x, p.y));
	}	
	
	 
	/**
	 * 
	 * @param occupancyGridMap  RO
	 * @param thinnedMap   RW
	 */
	public void calcClearance(
			OccupancyGridMap occupancyGridMap,
			UniversalGridMap<SkeletonCell> thinnedMap) {
		this.map = occupancyGridMap;
		for (GridPoint point : points) {
			for (int radius = 1; radius < 30; radius++) {
				double clearance = checkRadiusForPoint(point, radius);
				
				if (clearance > 0) {
					thinnedMap.get(point.x, point.y).clearance = clearance;
					point.clearance = clearance;
					break;
				}
			}
		}
	}
	/**
	 * Check all points in "radius"
	 * e.g. for radius 1 around x
	 * 
	 * 		1	2	3
	 * 		7	x   8
	 * 		4	5	6
	 * @param centerPoint
	 * @param radius
	 * @return
	 */
	private double checkRadiusForPoint(GridPoint centerPoint, int radius) {
		int x,y;
		for (int i = -radius;  i<= radius; i++) {
			y = centerPoint.y + radius;
			x = centerPoint.x + i;
			if (map.isOccupied(x, y)) {
				lastFoundPoint = new Point(x,y);
				return clearance(centerPoint, new Point(x,y));
			}
			y = centerPoint.y + radius;
			if (map.isOccupied(x, y)) {
				lastFoundPoint = new Point(x,y);
				return clearance(centerPoint, new Point(x,y));
			}			
		}
		
		for (int i = -radius + 1 ; i < radius; i++) {
			x = centerPoint.x + radius;
			y = centerPoint.y + i;
			if (map.isOccupied(x, y)) {
				lastFoundPoint = new Point(x,y);
				return clearance(centerPoint, new Point(x,y));
			}
			x = centerPoint.x + radius;
			if (map.isOccupied(x, y)) {
				lastFoundPoint = new Point(x,y);
				return clearance(centerPoint, new Point(x,y));
			}
		}
		return -1.0;
	}
	private double clearance(GridPoint centerPoint, Point other) {
		double x = centerPoint.x - other.x;
		double y = centerPoint.y - other.y;
		return Math.sqrt(x*x + y*y);
	}
	
	public void calcCriticalLines(
			CriticalLineAlgoritm algorithm,
			UniversalGridMap<SkeletonCell> thinnedMap) {
		ArrayList<GridPoint> critical = algorithm.getCriticalPoints(this, thinnedMap);
		for (GridPoint gridPoint : critical) {
			thinnedMap.get(gridPoint.x, gridPoint.y).isCritical = true;
		}

	}
	
	
	
	
}
