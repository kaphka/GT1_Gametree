package de.htw.jschmolling.ai.test;

import static org.junit.Assert.*;

import org.junit.Test;

import de.htw.jschmolling.ai.GameFieldUtils;
import de.htw.jschmolling.ai.Players;

public class PerformanceTest {

	@Test
	public void testBitPerformance() {
		long [] field = GameFieldUtils.createInital();
		int count = 10000000;
		int x = 0,y = 0;
		double average = 0;
		for (int i = 0; i < count; i++) {
			long before = System.nanoTime();
			x = 0;
			y = 0;
			for (int n = 0; n < GameFieldUtils.DIMENSION2; n++) {
				if (GameFieldUtils.isSet(field, Players.NORTH.pos, x, y)){
					GameFieldUtils.unset(field, Players.NORTH.pos, x, y);
				} else {
					GameFieldUtils.set(field, Players.NORTH.pos, x, y);
				}
				
				x = ++x % GameFieldUtils.DIMENSION;
			}
			long time = System.nanoTime() - before;
			average += time * 1.0 / count;
		}
		System.out.println(average);
	}
	
	

}
