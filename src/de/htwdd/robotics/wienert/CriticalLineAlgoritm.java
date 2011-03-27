package de.htwdd.robotics.wienert;

import java.util.ArrayList;

import de.htwdd.robotics.map.UniversalGridMap;

public interface CriticalLineAlgoritm {
	public abstract ArrayList<GridPoint> getCriticalPoints(GridLine line, UniversalGridMap<SkeletonCell> map);  
}
