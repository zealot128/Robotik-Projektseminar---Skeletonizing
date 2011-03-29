package de.htwdd.robotics.wienert;

import java.util.ArrayList;

import de.htwdd.robotics.SingleThreadedProcessor;
import de.htwdd.robotics.map.OccupancyGridMap;
import de.htwdd.robotics.map.UniversalGridMap;
import de.htwdd.robotics.map.container.GridMapContainer;
import de.htwdd.robotics.map.filter.FilteredOccupancyGridMap;
import de.htwdd.robotics.map.filter.OccupancyDilationFilter;
import de.htwdd.robotics.map.filter.OccupancyDilationFilter.Shape;

public class ThinningProcessor extends SingleThreadedProcessor {
    private Thinner thinningAlgorithm;
    public OccupancyGridMap map;
    public UniversalGridMap<SkeletonCell> skeletonMap;
    public int rowFrom;
    public int rowTo;
    public int colFrom;
    public int colTo;
    private boolean first = true;
    private GridMapContainer<UniversalGridMap<SkeletonCell>> skeletonContainer;
    private GridApproximatable approximationAlgorithm;
    private CriticalLineAlgoritm criticalLineFindingAlgorithm;
    private OccupancyGridMap filteredMap;
    private GridMapContainer<OccupancyGridMap> gridMapContainer;

    public Thinner getThinningAlgorithm() {
        return thinningAlgorithm;
    }

    public ThinningProcessor(
            GridMapContainer<OccupancyGridMap> gridMapContainer,
            GridMapContainer<UniversalGridMap<SkeletonCell>> skeletonContainer,
            Thinner thinningAlgorithm,
            GridApproximatable approximationAlgorithm) {
        super("thinning-processor", 1000);
        this.skeletonContainer = skeletonContainer;
        this.gridMapContainer = gridMapContainer;
        this.thinningAlgorithm = thinningAlgorithm;
        initialize();
        this.thinningAlgorithm.map = this.skeletonContainer.get();
        this.approximationAlgorithm = approximationAlgorithm;
        this.criticalLineFindingAlgorithm = new CriticalLineMinimum();
    }

    @Override
    protected void loop() {
        // only do once
        if (first) {
            for (int i=0; i<1; i++) {
                this.thinningAlgorithm.thinn();
            }
            ArrayList<GridLine> lines = this.approximationAlgorithm.approximate(thinningAlgorithm.map);
            for (GridLine gridLine : lines) {
                gridLine.calcClearance(/*gridMapContainer.get()*/ filteredMap, skeletonContainer.get());
                gridLine.calcCriticalLines(criticalLineFindingAlgorithm, skeletonContainer.get());
            }
            skeletonContainer.mapChanged(skeletonContainer.get().getBounds());

            first = false;
        }

    }

    /**
     * Copy given occupancy grid map in our skeleton grid map
     */
    private void initialize() {
        map = FilteredOccupancyGridMap.ofDouble(gridMapContainer.get(), 
                new OccupancyDilationFilter(gridMapContainer.get().getGridSize(), 0.3, Shape.CIRCLE));
        filteredMap = map;
//        map = gridMapContainer.get();
        skeletonContainer.set(new UniversalGridMap<SkeletonCell>(map
                .getGridSize(), map.getFirstRow(), map.getFirstColumn(), map
                .getLastRow(), map.getLastColumn()));
        skeletonMap = skeletonContainer.get();
        for (int i = map.getFirstRow(); i <= map.getLastRow(); i++) {
            for (int j = map.getFirstColumn(); j <= map.getLastColumn(); j++) {
                int status = map.isFree(i, j) ? SkeletonCell.STATE_OCCUPIED
                        : SkeletonCell.STATE_FREE;
                SkeletonCell skeletonCell = new SkeletonCell(status, i, j);
                skeletonMap.set(i, j, skeletonCell);
            }
        }
        first = true;
    }

    public void setGridMap(OccupancyGridMap occupancyGridMap) {
        map = occupancyGridMap;
        rowFrom = map.getFirstRow();
        rowTo = map.getLastRow();
        colFrom = map.getFirstColumn();
        colTo = map.getLastColumn();
        first = true;

    }

    public void setSkeletonMap(
            GridMapContainer<UniversalGridMap<SkeletonCell>> skeletonGrid) {
        skeletonContainer = skeletonGrid;
        first = true;
    }

    /* Collect all adjacent neighbors for cell */
    public SkeletonCell[][] collectNeighborsFor(int my_row, int my_col) {

        SkeletonCell[][] neighborhood = new SkeletonCell[3][3];
        for (int row = -1; row < 2; row++) {
            for (int col = -1; col < 2; col++) {
                neighborhood[row + 1][col + 1] = skeletonMap.get(row + my_row,
                        col + my_col);
            }
        }
        return neighborhood;
    }
}