package de.htw.jschmolling.ai.test;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import de.htw.jschmolling.ai.GameFieldUtils;
import de.htw.jschmolling.ai.Players;
import de.htw.jschmolling.ai.Search;
import de.htw.jschmolling.performance.Zobrist;

public class SearchTest {

	@Test
	public void testSimpleSearch() {
		
		Runnable informer = new Runnable() {
			
			@Override
			public void run() {
				System.out.println("fieldStates: " + Search.fieldStateCounter);
			}
		};
		final ScheduledExecutorService scheduler =
			     Executors.newScheduledThreadPool(1);
		scheduler.scheduleAtFixedRate(informer, 1, 10, TimeUnit.SECONDS);
		long [] field = GameFieldUtils.createInital();
		GameFieldUtils.set(field, Players.SOUTH.pos, 1);
		GameFieldUtils.set(field, Players.SOUTH.pos, 2);
		GameFieldUtils.set(field, Players.NORTH.pos, 57);

		int res = Search.DLS(field, Players.SOUTH, 4 * 4, 0, Zobrist.hash(field));
		System.out.println(GameFieldUtils.toString(field));
		System.out.println(res);
	}

}
