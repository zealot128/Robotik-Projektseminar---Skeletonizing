package de.htwdd.robotics.wienert;

import java.util.ArrayList;

import de.htwdd.robotics.map.UniversalGridMap;

/**
 * Very easy and uncomplete search
 * Looking for min. clearance in current line
 * @author zealot64
 *
 */
public class CriticalLineMinimum implements CriticalLineAlgoritm {

	@Override
	public ArrayList<GridPoint> getCriticalPoints(GridLine line,
			UniversalGridMap<SkeletonCell> map) {
		
		GridPoint minPoint = new GridPoint(0,0);
		minPoint.clearance = 100000;
		for (GridPoint gridPoint : line.points) {
			if (gridPoint.clearance < minPoint.clearance) {
				minPoint = gridPoint;
			}
		}
		ArrayList<GridPoint> minPoints = new ArrayList<GridPoint>();
		
		// ignore empty lines or false lines
		if (line.points.size() < 2) 
			return minPoints;
			
		minPoints.add(minPoint);
		return minPoints;
	}

}
