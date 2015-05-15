package de.htw.jschmolling.ai;

import static de.htw.jschmolling.ai.GameFieldUtils.DIMENSION;
import lenz.htw.kimpl.Move;

/**
 * This class helps to store a move description in one integer value.
 * 
 * Design: 6 bits per coordinates because 2^6 = 64; the value 64 is used for
 * invalid positions
 * 
 * @author jschmolling
 *
 */
public class SMove {

	public static final Move defaultMove = new Move(0, 0, 0, 0);
	private static final int SHIFT_FROM = 6;
	private static final int TO_SIDE = 0b111111;

	public static Move getMoveObject(int smove) {
		int from = smove >> SHIFT_FROM;
		int to = smove & TO_SIDE;
		return new Move(from % DIMENSION, from / DIMENSION, to % DIMENSION, to
				/ DIMENSION);
	}

	public static int newSMove(int fromPos, int toPos) {
		return fromPos << SHIFT_FROM | toPos;
	}

	public static int from(int smove) {
		return smove >> SHIFT_FROM;
	}

	public static int to(int smove) {
		return smove & TO_SIDE;
	}

	public static int newSMove(int fromX, int fromY, int toX, int toY) {
		return ((fromY * DIMENSION + fromX) << SHIFT_FROM)
				| (toY * DIMENSION + toX);
	}

	public static String toString(int smove) {
		if (smove == GameFieldUtils.EMPTY_POSITION) {
			return "invalid";
		}
		return getMoveObject(smove).toString();
	}

	public static void getPossibleMoves(long[] field, Players p,
			int[] positionsBuffer, int[] resultBuffer) {
		int[] direction = Players.dir[p.pos];
		GameFieldUtils.getPlayerPositions(field[p.pos], positionsBuffer);
		int cMoves = 0;
		for (int i : positionsBuffer) {
			if (i == GameFieldUtils.EMPTY_POSITION) {
				break;
			}
			int newPos = add(i, direction[0], direction[1]);
			// int left = newSMove(i, add(i, direction[0], direction[1]));
			// int right = newSMove(i, add(i, direction[0], direction[1]));
			if (newPos != GameFieldUtils.EMPTY_POSITION
					&& GameFieldUtils.isEmptyPosition(field, newPos)) {
				resultBuffer[cMoves++] = newSMove(i, newPos );
			}
		}
		for (int i = cMoves; i < resultBuffer.length; i++) {
			resultBuffer[i] = GameFieldUtils.EMPTY_POSITION;
		}
	}

	public static int[] getPossibleMoves(long[] field, Players p) {
		int[] playerPositions = new int[6];
		int[] result = new int[6 * 3];
		getPossibleMoves(field, p, playerPositions, result);
		return result;
	}

	private static int add(int sposition, int x, int y) {
		int xNew = (sposition % DIMENSION) + x;
		int yNew = (sposition / DIMENSION) + y;
		if (xNew >= 0 && yNew >= 0 && xNew < DIMENSION && yNew < DIMENSION) {
			return sposition + y * DIMENSION + x;
		} else {
			return GameFieldUtils.EMPTY_POSITION;
		}
	}

	public static int unmove(int smove) {
		return smove >> SHIFT_FROM | ((smove & TO_SIDE) << SHIFT_FROM);
	}

	// public static int getFromX(int smove){
	//
	// }

}
