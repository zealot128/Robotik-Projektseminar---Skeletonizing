package de.htwdd.robotics.wienert;

import java.awt.Point;
import java.util.ArrayList;

import de.htwdd.robotics.datagrid.PreservingUniversalGridCellOperation;
import de.htwdd.robotics.map.UniversalGridMap;

/**
 * Collect all connected lines for a thinned map.
 * This algorithm finds a starting point (crossroad). 
 * And collect all pixels and produces lines
 * @author zealot64
 *
 */
public class GridApproximatorRecursive implements GridApproximatable {
	static boolean DEBUG = false;
	
	private Thinner helper;		// using the connectivity methods etc.
	
	private UniversalGridMap<SkeletonCell> map;

	private void log(String st) {
		if (DEBUG)	System.out.println(st);
	}
	@Override
	public ArrayList<GridLine> approximate(
			UniversalGridMap<SkeletonCell> thinnedMap) {
		map = thinnedMap;
		helper = new ThinnerSuen(); // initialize the helper
		helper.map = map;
	
		Point point = findStartPoint();
		//logPoint(point.x, point.y);
		log("StartPunkt: " + point.x + ":" + point.y);
		map.get(point.x, point.y).special = SkeletonCell.SPECIAL_START;
		collectPoints(point, null);
		log("Found " + lines.size() + " Lines");
		return lines;
	}
	
	/**
	 * Recursive search-function
	 * from a point
	 * @param point  the point, from which to start
	 * @param includePoint also include another point in the line, but do not touch it
	 */
	private void collectPoints(Point point, Point includePoint ) {
		
		GridLine currentLine = new GridLine();
		if (includePoint != null) {
			currentLine.addPoint(includePoint);
		}
		Point currentPoint = point;
		while (true) {
			log("Current: " + currentPoint);
			currentLine.addPoint(currentPoint);		
			setVisted(currentPoint);				// mark point
			
			// find all neighbors which are not marked yet
			ArrayList<Point> neighbors = findNeighborsFor(currentPoint);
			int c = connectivity(currentPoint);		// number of connections
			log("connect, neighbor: " + c + "," + neighbors.size());
			if (neighbors.size() == 0 ) {
				// Sackgasse
				lines.add(currentLine);
				return;
			}
			if (c == 2) {
				// choose a next point
				currentPoint = getNextPoint(neighbors,currentPoint);
			} else if (c > 2) {
				// crossroad, finish line and continue with new lines
				if (currentLine.points.size() > 1) {
					lines.add(currentLine);
				}
				for (int i=0; i< neighbors.size(); i++) {
					collectPoints(neighbors.get(i), currentPoint);
				}
				return;
			} else {
				System.err.println("ERROR connectivity < 2?? sollte eigentlich geloescht sein");
				return;
			}
		}
	}
	/**
	 * Returning the directly orthogonal adjacent neighbors first from neighborhood. Otherwise anyone (diagonal)
	 * @param neighbors
	 * @param p
	 * @return
	 */
	private Point getNextPoint(ArrayList<Point> neighbors, Point p) {
		for (Point neighbor : neighbors) {
			if (neighbor.x == p.x || neighbor.y == p.y) {
				return neighbor;
			}
		}
		return neighbors.get(0);

	}


	private void setVisted(Point currentPoint) {
		SkeletonCell cell = map.get(currentPoint.x, currentPoint.y);
		cell.visited = true;
		map.set(currentPoint.x, currentPoint.y, cell);
	}

	/**
	 * Find all neighbors of point, that are occupied and not visited
	 * 
	 * @param point
	 * @return list of neighbors or empty List
	 */
	private ArrayList<Point> findNeighborsFor(Point point) {
		ArrayList<Point> neighborhood = new ArrayList<Point>();
		for (int row = -1; row < 2; row++) {
			for (int col = -1; col < 2; col++) {
				if (row == 0 && col == 0) {
					continue; // Dont take the current point
				}
				int x = row + point.x;
				int y = col + point.y;
				SkeletonCell cell = map.get(x, y);
				if (!cell.visited && cell.status == SkeletonCell.STATE_OCCUPIED) {
					neighborhood.add(new Point(x, y));
				}
			}
		}
		return neighborhood;
	}

	private Point startPoint;

	private int connectivity(Point p) {
		int[] n = helper.transformToArray(helper.collectNeighborsFor(p.x, p.y));
		return  helper.numberOfChanges(n);
	}
	
	/**
	 * Iterate over the map and find a valid startingpoint that is a end Point
	 * of a line Otherwise find the first point that is a crossroad TODO
	 * Termination?
	 * TODO Finding no starting Point for small maps (no intersection)
	 * @return
	 */
	private Point findStartPoint() {

		map.traverse(new PreservingUniversalGridCellOperation<SkeletonCell>() {
			@Override
			public void process(int row, int col, SkeletonCell cell) {
				if (cell.status != SkeletonCell.STATE_OCCUPIED) {
					return;
				}
				if (row == 0 || col == 0 || row == map.getLastRow() - 1
						|| col == map.getLastColumn() - 1) {
					return;
				}

				int c = connectivity(new Point(row, col));
				if (c == 3) {
					log("StartPoint Canidate " + row + "," + col);
					startPoint = new Point(row, col);
				}
			}
		});
		
		return startPoint;
		
	}

	/**
	 * Pretty Print a given point with environment on current this.map
	 * @param x
	 * @param y
	 */
	public void logPoint(int x, int y) {
		int radius = 5;
		SkeletonCell cell = map.get(x,y);
		System.out.println("Logging: " + x + ":" + y + "  - S:" + cell.status);
		String buf = "\n";
		for (int i = -radius; i < radius+1; i++) {
			buf += (y - i) + ": ";
			for (int j = -radius; j < radius+1; j++) {
				buf += map.get( x + j , y - i ).status + " ";
			}
			buf += "\n";
		}
		System.out.println(buf);
	}

}
