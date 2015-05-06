package de.htw.jschmolling.ai.test;

import static org.junit.Assert.*;

import org.junit.Test;

import de.htw.jschmolling.ai.GameFieldUtils;
import de.htw.jschmolling.ai.Players;

public class PerformanceTest {

	@Test
	public void testBitPerformance() {
		long [] field = GameFieldUtils.createInital();
		int n = 10000000;
		long before = System.nanoTime();
		for (int i = 0; i < n; i++) {
			for (int y = 0; y < GameFieldUtils.DIMENSION; y++) {
				for (int x = 0; x < GameFieldUtils.DIMENSION; x++) {
					if (GameFieldUtils.isSet(field, Players.NORTH.pos, x, y)){
						GameFieldUtils.unset(field, Players.NORTH.pos, x, y);
					} else {
						GameFieldUtils.set(field, Players.NORTH.pos, x, y);
					}
				}
			}
		}
		long time = System.nanoTime() - before;
		System.out.println(time * 1.0 / n);
	}
	
	

}
