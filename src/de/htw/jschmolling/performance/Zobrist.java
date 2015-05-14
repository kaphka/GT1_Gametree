package de.htw.jschmolling.performance;

import java.util.Random;

import de.htw.jschmolling.ai.GameFieldUtils;
import de.htw.jschmolling.ai.Players;
import de.htw.jschmolling.ai.SMove;

public class Zobrist {

	public final static int[][] zobristTable = new int[Players.values().length
			+ 1][GameFieldUtils.DIMENSION2];

	static {
		Random rnd = new Random();
		for (int i = 0; i < zobristTable.length; i++) {
			for (int j = 0; j < zobristTable[0].length; j++) {
				zobristTable[i][j] = rnd.nextInt();
			}
		}
	}

	public static int hash(long[] field) {
		int hash = 0;
		int [] buffer = new int[6];
		for (Players p : Players.values()) {
			GameFieldUtils.getPlayerPositions(field[p.pos], buffer);
			for (int i = 0; i < buffer.length; i++) {
				if (buffer[i] == 64){
					break;
				}
				hash ^= zobristTable[p.pos][buffer[i]];
			}
		}
		return hash;
	}
	
	public static int rehash(int hash, int playerPos, int smove) {
		return (hash ^ zobristTable[playerPos][SMove.from(smove)]) ^ zobristTable[playerPos][SMove.to(smove)];
	}
	
	public static String print(int h) {
		return Integer.toHexString(h);
	}

}
