package de.htw.jschmolling.ai;

import static de.htw.jschmolling.ai.GameFieldUtils.*;
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
			currentBestMoves[i] = INVALID_POSITION;
		}
	}

	public int DLS(long[] field, Players movingPlayer, int depth,
			int limit, int hash, int alpha, int beta) {
//		System.out.println("\nDepth: " + depth + " " + movingPlayer.toString());
//		System.out.println(GameFieldUtils.toString(field));
		if (depth == limit) {
			return GameFieldUtils.eval(s,field, movingPlayer);
		}  

		int maxAlpha = alpha;
		int result = 0;
		int max = Integer.MIN_VALUE;
		int [] moves = new int[6 * 3];
		int [] positionsBuffer = new int[6];
		
		SMove.getPossibleMoves(field, movingPlayer, positionsBuffer,moves);
		
		if (moves[0] == INVALID_POSITION){
			// RECURSION: player cant move
			result = -DLS(field, movingPlayer.next(), depth - 1, limit, hash, -beta, -maxAlpha);
		} else {
			for (int i = 0; i < moves.length; i++) {
				if (moves[i] == INVALID_POSITION){
					break;
				}
				GameFieldUtils.performMove(field, moves[i], movingPlayer);
				int rehash = Zobrist.rehash(hash, movingPlayer.pos, moves[i]);
				fieldStateCounter += 1;
				// RECURSION
				result = - DLS(field, movingPlayer.next(), depth + 1, limit, rehash, -beta, -maxAlpha);
				GameFieldUtils.performMove(field, SMove.unmove(moves[i]), movingPlayer);
				if (result > maxAlpha){
					maxAlpha  = result;
					if (maxAlpha >= beta) {
						// beta cutoff
						break;
					}
					currentBestMoves[depth] = SMove.setMovingPlayer(moves[i]);
				}
				
			}
		 }
		return maxAlpha;
	}

	public int search(long[] field, Players movingPlayer, int limit, int hash) {
		System.out.println("<-- start search for " + movingPlayer + " limit=" + limit);
		this.startNanoSecond = System.nanoTime();
		System.out.println();
		int max =  DLS(field, movingPlayer, 0, limit, hash, Integer.MIN_VALUE, Integer.MAX_VALUE);
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
