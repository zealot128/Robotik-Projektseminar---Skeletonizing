package de.htwdd.robotics.wienert;

import java.awt.Point;
import java.util.ArrayList;

import de.htwdd.robotics.datagrid.PreservingUniversalGridCellOperation;
import de.htwdd.robotics.map.UniversalGridMap;

public class GridApproximatorRecursive implements GridApproximatable {

	private Thinner helper;		// using the connectivity methods etc.
	private UniversalGridMap<SkeletonCell> map;

	@Override
	public ArrayList<GridLine> approximate(
			UniversalGridMap<SkeletonCell> thinnedMap) {
		map = thinnedMap;
		helper = new ThinnerLuWang();
		helper.map = map;
		Point point = findStartPoint();
		logPoint(point.x, point.y);
		System.out.println("StartPunkt: " + point.x + ":" + point.y);
		collectPoints(point, null);
		System.out.println("Found " + lines.size() + " Lines");
		return lines;
	}
	
	private void collectPoints(Point point, Point includePoint ) {
		GridLine currentLine = new GridLine();
		if (includePoint != null) {
			currentLine.points.add(includePoint);
		}
		Point currentPoint = point;
		while (true) {
			System.out.println("Current: " + currentPoint);
			currentLine.points.add(currentPoint);
			setVisted(currentPoint);
			
			ArrayList<Point> neighbors = findNeighborsFor(point);
			int c = connectivity(currentPoint);
			if (neighbors.size() == 0 ) {
				lines.add(currentLine);
				return;
			}
			if (c == 2) {
				currentPoint = getNextPoint(neighbors,currentPoint);
			} else if (c > 2) {
				if (currentLine.points.size() > 1) {
					lines.add(currentLine);
				}
				for (int i=0; i< neighbors.size(); i++) {
					collectPoints(neighbors.get(i), currentPoint);
				}
				return;
			} else {
				System.out.println("ERROR connectivity < 2?? sollte eigentlich geloescht sein");
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
				// System.out.println(x + ":" + y + "-" + cell.status + "/"
				// +cell.visited);
				if (!cell.visited && cell.status == SkeletonCell.STATE_OCCUPIED) {
					neighborhood.add(new Point(x, y));
				}
			}
		}
		return neighborhood;
	}

	private Point startPoint;
	private Point alternateStartPoint;

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

//				ArrayList<Point> neighbors = findNeighborsFor(new Point(row,col));
				int c = connectivity(new Point(row, col));
				if (c == 3) {
					System.out.println("StartPoint? " + row + "," + col);
					startPoint = new Point(row, col);
				}
//				if (neighbors.size() >= 3) {
//					alternateStartPoint = new Point(row, col);
//				}
			}
		});
		
		return startPoint;
		
	}
	
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
		ArrayList<Point> neighbors = findNeighborsFor(new Point(496,
				 143));
	}

}
