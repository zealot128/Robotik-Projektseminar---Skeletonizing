package de.htwdd.robotics.wienert;

import java.util.ArrayList;

import de.htwdd.robotics.map.UniversalGridMap;

public interface GridApproximatable {
	ArrayList<GridLine> lines = new ArrayList<GridLine>();
	public ArrayList<GridLine> approximate(UniversalGridMap<SkeletonCell> thinnedMap);
}
