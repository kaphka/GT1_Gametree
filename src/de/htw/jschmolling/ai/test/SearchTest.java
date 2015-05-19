package de.htw.jschmolling.ai.test;

import static org.junit.Assert.*;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import de.htw.jschmolling.ai.GameFieldUtils;
import de.htw.jschmolling.ai.GameFieldUtils.EvalStrategy;
import de.htw.jschmolling.ai.Players;
import de.htw.jschmolling.ai.SMove;
import de.htw.jschmolling.ai.Search;
import de.htw.jschmolling.ai.Search.OrderStrategy;
import de.htw.jschmolling.ai.Zobrist;

public class SearchTest {

	@Test
	public void testSimpleSearch() {
		
		final Search testSearch = new Search(EvalStrategy.HIT_FIRST, OrderStrategy.RANDOM);
		
		Runnable informer = new Runnable() {
			
			@Override
			public void run() {
				System.out.println("> SearchObservator: fieldStates: " + testSearch.fieldStateCounter);
			}
		};
		
		final ScheduledExecutorService scheduler =
			     Executors.newScheduledThreadPool(1);
		scheduler.scheduleAtFixedRate(informer, 1, 10, TimeUnit.SECONDS);
		long [] field = GameFieldUtils.createInital();
//		GameFieldUtils.set(field, Players.SOUTH.pos, 1);
//		GameFieldUtils.set(field, Players.SOUTH.pos, 2);
//		GameFieldUtils.set(field, Players.NORTH.pos, 57);

		String timings = "";
		String fieldStates = "";
//		for (int limit = 0; limit < 40; limit++) {
			long start = System.nanoTime();
			testSearch.setTimeLimit(4000);
			int res = testSearch.search(field, Players.SOUTH, 50, Zobrist.hash(field));
			long time = System.nanoTime() - start;
			timings +="" + 1.0 * time / TimeUnit.SECONDS.toNanos(1) + ", ";
			fieldStates += "" + testSearch.fieldStateCounter + ", ";
//		}
		System.out.println(timings);			
		System.out.println(fieldStates);
		
		System.out.println(GameFieldUtils.toString(field));
	}
	
	@Test
	public void testMulti() throws Exception {
		long [] field = GameFieldUtils.createInital();
		Search s = new Search(EvalStrategy.HIT_FIRST, OrderStrategy.RANDOM);
		int result = s.spawnSearch(field, Players.SOUTH, 35);
		System.out.println(SMove.toString(result));
		
	}

}
