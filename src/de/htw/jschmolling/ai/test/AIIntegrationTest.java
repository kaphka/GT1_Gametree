package de.htw.jschmolling.ai.test;

import org.junit.Test;

import de.htw.jschmolling.ai.GameField;
import de.htw.jschmolling.ai.Players;

public class AIIntegrationTest {
	
	@Test
	public void testGetRandomMove(){
		
	}
	
	@Test
	public void testPrintEmpty() throws Exception {
		long[] field = new long[Players.values().length];
		System.out.println(GameField.toString(field));
	}
	
	@Test
	public void testPrintOneSet() throws Exception {
		long[] field = new long[Players.values().length];
		GameField.set(field,Players.NORTH.pos, 0, 0);
		System.out.println(Long.toBinaryString( field[Players.NORTH.pos] & (1l << (4 * 8 + 0))));
		System.out.println(GameField.toString(field));
	}

}
