package de.htwdd.robotics.wienert;


import junit.framework.Assert;
import junit.framework.*;

import org.junit.Before;

public class ThiningProcessorTestCase extends TestCase{

	@Before
	public void setUp() throws Exception {
	}
	
	public SkeletonCell[][] simpleTransform(int[][] stati) {
		SkeletonCell[][] sgrid = new SkeletonCell[3][3];
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				sgrid[i][j] = new SkeletonCell(stati[i][j], i, j);
			}
		}
		return sgrid;
		
	}
	
	public void test_black_a() {
		int[][] arr = {{0,0,0},
					   {0,1,0},
					   {0,0,0}};
		SkeletonCell[][] neighbors = simpleTransform(arr);
		Assert.assertEquals(0,
				neighbors[1][1].numberOfBlack(neighbors));
	}
	public void test_black_b() {
		int[][] arr = {{1,0,0},
					   {0,1,1},
					   {0,0,1}};
		SkeletonCell[][] neighbors = simpleTransform(arr);
		Assert.assertEquals(3,
				neighbors[1][1].numberOfBlack(neighbors));
		Assert.assertEquals(2,
				neighbors[1][1].numberOfChanges(neighbors));		
	}
	public void test_black_c() {
		int[][] arr = {{1,0,1},
					   {0,1,1},
					   {1,1,1}};
		SkeletonCell[][] neighbors = simpleTransform(arr);
		Assert.assertEquals(6,
				neighbors[1][1].numberOfBlack(neighbors));
		Assert.assertEquals(2,
				neighbors[1][1].numberOfChanges(neighbors));	
		Assert.assertEquals(2,
				neighbors[1][1].connectivity(neighbors));	
		
	}	
}
