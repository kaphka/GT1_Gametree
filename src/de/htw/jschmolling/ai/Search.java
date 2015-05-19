package de.htw.jschmolling.ai;

import de.htw.jschmolling.ai.GameFieldUtils.EvalStrategy;


public class Search {
	public long fieldStateCounter = 0;
	public long startNanoSecond = 0;
	private final EvalStrategy s;
	private int[] currentBestMoves = new int [4 * 6 * 7];
	
	private int [] lookuptable;
	
	public Search(EvalStrategy s) {
		this.s = s;
//		128mb
		lookuptable = new int[128 * 1024 * 1024 * 8 / 32];
		for (int i = 0; i < currentBestMoves.length; i++) {
			currentBestMoves[i] = GameFieldUtils.INVALID_POSITION;
		}
	}

	public int DLS(long[] field, Players movingPlayer, int depth,
			int limit, int hash) {
//		System.out.println("\nDepth: " + depth + " " + movingPlayer.toString());
//		System.out.println(GameFieldUtils.toString(field));
		int result = 0;
		int max = Integer.MIN_VALUE;
		if (depth == limit) {
			return GameFieldUtils.eval(s,field, movingPlayer);
		} else {
			int [] moves = new int[6 * 3];
			int [] positionsBuffer = new int[6];
			SMove.getPossibleMoves(field, movingPlayer, positionsBuffer,moves);
			if (moves[0] == GameFieldUtils.INVALID_POSITION){
				// RECURSION: player cant move
				result = DLS(field, movingPlayer.next(), depth - 1, limit, hash);
			} else {
				for (int i = 0; i < moves.length; i++) {
					if (moves[i] == GameFieldUtils.INVALID_POSITION){
						break;
					}
					GameFieldUtils.performMove(field, moves[i], movingPlayer);
					int rehash = Zobrist.rehash(hash, movingPlayer.pos, moves[i]);
					fieldStateCounter += 1;
//					RECURSION
					result = - DLS(field, movingPlayer.next(), depth + 1, limit, rehash);
					if (result > max){
						currentBestMoves[depth] = SMove.setMovingPlayer(moves[i]);
						result  = max;
					}
					GameFieldUtils.performMove(field, SMove.unmove(moves[i]), movingPlayer);
				}
			}
		}
		return max;
	}

	public int search(long[] field, Players movingPlayer, int limit, int hash) {
		System.out.println("<-- start search for " + movingPlayer + " limit=" + limit);
		this.startNanoSecond = System.nanoTime();
		System.out.println();
		int max =  DLS(field, movingPlayer, 0, limit, hash);
		for (int i = 0; i < currentBestMoves.length; i++) {
			if (currentBestMoves[i] == GameFieldUtils.INVALID_POSITION){
				break;
			}
			System.out.println(SMove.toString(currentBestMoves[i]));
		}
		System.out.println("--> Search finished");
		return max;
	}
}
