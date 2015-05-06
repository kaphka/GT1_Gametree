package de.htw.jschmolling.performance;

import java.util.Random;

import de.htw.jschmolling.ai.GameFieldUtils;
import de.htw.jschmolling.ai.Players;

public class Zobrist {

	public final static int[][] zobristTable = new int[GameFieldUtils.DIMENSION
			* GameFieldUtils.DIMENSION][Players.values().length + 1];

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
		return hash;
	}

}
