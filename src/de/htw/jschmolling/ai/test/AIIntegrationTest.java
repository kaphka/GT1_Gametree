package de.htw.jschmolling.ai.test;

import static org.junit.Assert.*;

import java.util.Arrays;

import lenz.htw.kimpl.Move;

import org.junit.Test;

import de.htw.jschmolling.ai.DUtils;
import de.htw.jschmolling.ai.GameFieldUtils;
import de.htw.jschmolling.ai.Players;
import de.htw.jschmolling.ai.SMove;
import de.htw.jschmolling.ai.Zobrist;

/**
 * General purpose tests.
 * 
 * @author jschmolling
 *
 */
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
		GameFieldUtils.set(field, Players.NORTH.pos, 23);
		GameFieldUtils.set(field, Players.SOUTH.pos, 2);
		GameFieldUtils.set(field, Players.NORTH.pos, 32);
		int hash = Zobrist.hash(field);
//		System.out.println(Zobrist.print(hash));
	}
	
	@Test
	public void testRehash() throws Exception {
		long[] field = GameFieldUtils.getEmptyField();
		GameFieldUtils.set(field, Players.NORTH.pos, 23);
		GameFieldUtils.set(field, Players.SOUTH.pos, 2);
		GameFieldUtils.set(field, Players.NORTH.pos, 32);
		int hash = Zobrist.hash(field);
		int smove = SMove.newSMove(0, 0,0,1);
//		System.out.println(Integer.toString(smove));
//		System.out.println("Move: " + SMove.toString(smove));
	}
	
	@Test
	public void testGetPlayerPositions() throws Exception {
		int [] playerPositions = { 0, 2, 5, 7, 63, GameFieldUtils.INVALID_POSITION };
		long [] pfield = GameFieldUtils.getEmptyField();
		for (int i = 0; i < playerPositions.length - 1; i++) {
			GameFieldUtils.set(pfield, Players.SOUTH.pos, playerPositions[i]);
		}
		
//		System.out.println(DUtils.getFullLong(pfield[Players.SOUTH.pos]));
		
		int [] playerPositionsResults = new int[playerPositions.length];
		GameFieldUtils.getPlayerPositions(pfield[Players.SOUTH.pos], playerPositionsResults);
//		System.out.println(Arrays.toString(playerPositionsResults));
		for (int i = 0; i < playerPositionsResults.length; i++) {
			assertArrayEquals(playerPositions, playerPositionsResults);
		}
	}
	
	@Test
	public void testSet64() throws Exception {
		long [] s = GameFieldUtils.getEmptyField();
		GameFieldUtils.set(s, 0, 63);
//		System.out.println("set long " + DUtils.getFullLong(s[0]));
//		System.out.println("long     " + Long.toBinaryString(0l | 1l << 63));
	}
	
	@Test
	public void testGetPossibleMoves() throws Exception {
		long [] s = GameFieldUtils.getEmptyField();
		GameFieldUtils.set(s, 2, 1);
		GameFieldUtils.set(s, 2, 2);
		GameFieldUtils.set(s, 2, 3);
		GameFieldUtils.set(s, 3, 1, 1);
//		System.out.println(GameFieldUtils.toString(s));
		
		int [] moves = SMove.getPossibleMoves(s, Players.WEST);
		for (int i : moves) {
			System.out.println(SMove.toString(i));
		}
	}
	
	@Test
	public void testUnmove() throws Exception {
		int smove = SMove.newSMove(5, 62);
		int unmove = SMove.unmove(smove);
		assertEquals(smove | SMove.UNMOVE_FLAG, SMove.unmove(unmove));
	}
	
	@Test
	public void testPerfomMove() throws Exception {
		long [] field = GameFieldUtils.getEmptyField();
		GameFieldUtils.set(field, Players.NORTH.pos, 0);
		int smove = SMove.newSMove(0, 1);
		GameFieldUtils.performMove(field, smove, Players.NORTH);
//		System.out.println(GameFieldUtils.toString(field));
	}
	
	@Test
	public void testNextPlayer() throws Exception {
		Players p = Players.NORTH;
		for (int i = 0; i < 100	; i++) {
			p = p.next();
		}
	}
	
	@Test
	public void testStartingPositions() throws Exception {
		long [] field = GameFieldUtils.createInital();
		int count = 0;
		for (int i = 0; i < field.length; i++) {
			count += Long.bitCount(field[i]);
		}
		assertEquals(4 * 6, count);
//		System.out.println(GameFieldUtils.toString(field));
	}
	
	@Test
	public void testHitMove() throws Exception {
		int move = SMove.newSMove(2, 8);
		move = SMove.setHitPlayer(move, Players.WEST);
		int unmove = SMove.unmove(move);

		long [] field = GameFieldUtils.createInital();
		int hashStartingField = Zobrist.hash(field);
	
		
		GameFieldUtils.performMove(field, move, Players.SOUTH);
		int hashMoved = Zobrist.hash(field);
		int rehashMoved    = Zobrist.rehash(hashStartingField, Players.SOUTH.pos, move);

		int [] buffer = new int[6];
		for (Players p : Players.values()) {
			System.out.println(Arrays.toString(GameFieldUtils.getPlayerPositions(field[p.pos], buffer)));
		}
		
		GameFieldUtils.performMove(field, unmove, Players.SOUTH);
		int hashField = Zobrist.hash(field);
		
		System.out.println(GameFieldUtils.toString(field));
		
		System.out.println(SMove.toString(move));
		System.out.println(SMove.toString(unmove));
		
		int rehashOriginal = Zobrist.rehash(rehashMoved, Players.SOUTH.pos, unmove);
		assertEquals(hashStartingField, hashField);
		assertEquals(hashStartingField, rehashOriginal);
		assertEquals(hashMoved, rehashMoved);
		
	}

}
