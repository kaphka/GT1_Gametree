package de.htw.jschmolling.ai.test;

import lenz.htw.kimpl.Move;

import org.junit.Test;

import de.htw.jschmolling.ai.DUtils;
import de.htw.jschmolling.ai.GameFieldUtils;
import de.htw.jschmolling.ai.GameUtils;
import de.htw.jschmolling.ai.Players;
import de.htw.jschmolling.performance.Zobrist;
import static org.junit.Assert.*;

public class AIIntegrationTest {
	
	@Test
	public void testGetRandomMove(){
		
	}
	
	@Test
	public void testPrintEmpty() throws Exception {
		long[] field = new long[Players.values().length];
//		System.out.println(GameFieldUtils.toString(field));
		for (int y = 0; y < GameFieldUtils.DIMENSION; y++) {
			for (int x = 0; x < GameFieldUtils.DIMENSION; x++) {
				for (Players p : Players.values()) {
					assertFalse(GameFieldUtils.isSet(field, p.pos, x, y));
				}
			}
		}
	}
	
	@Test
	public void testPrintOneSet() throws Exception {
		long[] field = new long[Players.values().length];
		GameFieldUtils.set(field,Players.NORTH.pos, 0, 0);
//		System.out.println(Long.toBinaryString( field[Players.NORTH.pos] & (1l << (4 * 8 + 0))));
//		System.out.println(GameFieldUtils.toString(field));
		assertTrue(GameFieldUtils.isSet(field, Players.NORTH.pos, 0, 0));
	}
	
	@Test
	public void testMoveOne() throws Exception {
		long[] field = GameFieldUtils.getEmptyField();
		GameFieldUtils.set(field,Players.NORTH.pos, 0, 0);
		field = GameFieldUtils.performMove(field, new Move(0, 0, 0, 1), Players.NORTH);
//		System.out.println(Long.toBinaryString( field[Players.NORTH.pos] & (1l << (4 * 8 + 0))));
//		System.out.println(GameFieldUtils.toString(field));
		assertTrue(GameFieldUtils.isSet(field, Players.NORTH.pos, 0, 1));
		assertFalse(GameFieldUtils.isSet(field, Players.NORTH.pos, 0, 0));
	}
	
	@Test
	public void testZobrist() throws Exception {
		long[] field = GameFieldUtils.getEmptyField();
		int hash = Zobrist.hash(field);
//		System.out.println(hash);
	}
	
	@Test
	public void testGetPlayerPositions() throws Exception {
		int [] playerPositions = { 0, 5, 8, 5, 63, 2};
		long [] pfield = GameFieldUtils.getEmptyField();
		for (int i = 0; i < playerPositions.length; i++) {
			GameFieldUtils.set(pfield, Players.SOUTH.pos, playerPositions[i]);
		}
		
		int [] playerPositionsResults = new int[playerPositions.length];
		GameFieldUtils.getPlayerPositions(pfield[Players.SOUTH.pos], playerPositionsResults);
		for (int i = 0; i < playerPositionsResults.length; i++) {
			assertArrayEquals(playerPositions, playerPositionsResults);
		}
	}
	
	@Test
	public void testSet64() throws Exception {
		long [] s = GameFieldUtils.getEmptyField();
		GameFieldUtils.set(s, 0, 63);
		System.out.println("set long " + DUtils.getFullLong(s[0]));
		System.out.println("long     " + Long.toBinaryString(0l | 1l << 63));
	}

}
