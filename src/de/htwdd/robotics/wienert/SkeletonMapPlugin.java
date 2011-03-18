package de.htwdd.robotics.wienert;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;
import java.awt.image.BufferedImage;
import de.htwdd.robotics.datagrid.PreservingUniversalGridCellOperation;
import de.htwdd.robotics.gui.environment.EnvironmentPlugin;
import de.htwdd.robotics.map.GridMapObserver;
import de.htwdd.robotics.map.GridMapRegion;
import de.htwdd.robotics.map.UniversalGridMap;
import de.htwdd.robotics.map.container.GridMapContainer;
import de.htwdd.robotics.util.Position;

public class SkeletonMapPlugin extends EnvironmentPlugin implements
		GridMapObserver {

	private BufferedImage paintImage;
	private GridMapContainer<UniversalGridMap<SkeletonCell>> skeletonContainer;
	private BufferedImage finalImage = null;
	private Double currentSize;

	public SkeletonMapPlugin(String name,
			GridMapContainer<UniversalGridMap<SkeletonCell>> container) {
		super(name);
		this.skeletonContainer = container;
	}

	private int bla = 0;
	@Override
	public void mapChanged() {
		runInBackground(new Runnable() {
			@Override
			public void run() {
				finalImage = new BufferedImage(1, 1, BufferedImage.TYPE_4BYTE_ABGR);
				UniversalGridMap<SkeletonCell> map = skeletonContainer.get();
				int width = map.getRowCount(); 
				int height = skeletonContainer.get().getColumnCount();		 
				paintImage = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
				Graphics g = paintImage.getGraphics();
				g.setColor(new Color(0x00000000,true));
				g.fillRect(0, 0, width, height);
				
				skeletonContainer.get().traverse(new PreservingUniversalGridCellOperation<SkeletonCell>() {
					@Override
					public void process(int row, int col, SkeletonCell object) {
						if (object.status == SkeletonCell.STATE_OCCUPIED) {
							//System.out.println("X:" + row + " Y:" + col );
							paintImage.setRGB(row, col, 0xFFFF0000);
						}
					}
				});
				Point paint = new Point(478,86);
				paintImage.setRGB(paint.x , paint.y, 0xFF00FF00);
				paintImage.setRGB(paint.x + 1 , paint.y +1, 0xFF00FF00);
				paintImage.setRGB(paint.x + 1 , paint.y -1, 0xFF00FF00);
				paintImage.setRGB(paint.x - 1 , paint.y +1, 0xFF00FF00);
				paintImage.setRGB(paint.x - 1 , paint.y -1, 0xFF00FF00);
				Position lower = map.getWorldCoordinates(map.getFirstRow(), map.getFirstColumn());
				Position upper = map.getWorldCoordinates(map.getLastRow(), map.getLastColumn());
				double gridSize = map.getGridSize();
				double x = lower.getX() - gridSize / 2 ;
				double y = lower.getY() - gridSize / 2 ;
				double rows = upper.getX() - lower.getX() + gridSize;
				double cols = upper.getY() - lower.getY() + gridSize;
				
				synchronized (finalImage) {
					finalImage = paintImage;
					//double x = 
					currentSize = new Rectangle2D.Double(x,y,rows,cols);
				}
			}
		});
	}

	@Override
	protected void onActivate() {
		super.onActivate();
		skeletonContainer.addObserver(this);
	}

	@Override
	protected void onDeactivate() {
		super.onDeactivate();
		skeletonContainer.removeObserver(this);
	}

	@Override
	public void mapChanged(GridMapRegion affectedRegion) {
		mapChanged(); //TODO
	}
	@Override
	protected void paint(Graphics2D g) {
		if (finalImage != null ) {
			synchronized (finalImage) {
				AffineTransform transform = new AffineTransform();
				transform.translate(currentSize.getMinX(), currentSize.getMinY());
				transform.scale(0.1,0.1);
				g.drawRenderedImage(finalImage, transform);
			}
		}
		super.paint(g);
	}
}
