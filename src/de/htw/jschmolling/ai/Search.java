package de.htw.jschmolling.ai;

import de.htw.jschmolling.performance.Zobrist;

public class Search {
	public static long fieldStateCounter = 0;

	public static int DLS(long[] field, Players movingPlayer, int depth,
			int limit, int hash) {
//		System.out.println("\nDepth: " + depth + " " + movingPlayer.toString());
//		System.out.println(GameFieldUtils.toString(field));
		int result = 0;
		if (depth == limit) {
			return depth;
		} else {
			int [] moves = new int[6 * 3];
			int [] positionsBuffer = new int[6];
			SMove.getPossibleMoves(field, movingPlayer, positionsBuffer,moves);
			if (moves[0] == GameFieldUtils.EMPTY_POSITION){
				// player cant move
				result = DLS(field, movingPlayer.next(), depth - 1, limit, hash);
			} else {
				for (int i = 0; i < moves.length; i++) {
					if (moves[i] == GameFieldUtils.EMPTY_POSITION){
						break;
					}
					GameFieldUtils.performMove(field, moves[i], movingPlayer);
					int rehash = Zobrist.rehash(hash, movingPlayer.pos, moves[i]);
					fieldStateCounter += 1;
					result = DLS(field, movingPlayer.next(), depth - 1, limit, rehash);
					GameFieldUtils.performMove(field, SMove.unmove(moves[i]), movingPlayer);
				}
			}
		}
		return result;
	}
}
