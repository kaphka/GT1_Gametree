package de.htw.jschmolling.ai;

import static de.htw.jschmolling.ai.GameFieldUtils.INVALID_POSITION;

import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import lenz.htw.kimpl.Move;
import de.htw.jschmolling.ai.GameFieldUtils.EvalStrategy;


public class Search {
	public long fieldStateCounter = 0;
	public long stopTime = 0;
	private final EvalStrategy s;
	private int[] currentBestMoves       = new int [4 * 6 * 7];
	private int[] currentBestMovesScores = new int [4 * 6 * 7];
	private int [] lookuptable;
	private OrderStrategy o;
//	private List<Run>
	
	private final Random rnd = new Random();
	private long timeLimit;
	private final ExecutorService executor;
	private boolean STOP_FLAG = false;
	
	private int maxDepth = 0;;
	
	public enum OrderStrategy{
		RANDOM
	}
	
	public Search(EvalStrategy s, OrderStrategy o) {
		this.s = s;
		this.o = o;
//		128mb
		lookuptable = new int[128 * 1024 * 1024 * 8 / 32];
		for (int i = 0; i < currentBestMoves.length; i++) {
			currentBestMoves[i] = INVALID_POSITION;
		}
		
		executor = Executors.newCachedThreadPool();
	}

	public int DLS(long[] field, Players movingPlayer, int depth,
			int limit, int hash, int alpha, int beta) {
//		System.out.println("\nDepth: " + depth + " " + movingPlayer.toString());
//		System.out.println(GameFieldUtils.toString(field));
		maxDepth = Math.max(depth, maxDepth);
		if (depth >= limit 
				|| System.nanoTime() > stopTime 
				) {
			return GameFieldUtils.eval(s,field, movingPlayer);
		}  

		int maxAlpha = alpha;
		int result = 0;
		
//		int [] moves = new int[6 * 3];
//		int [] positionsBuffer = new int[6];
		int [] orderedMoves = new int[6 * 3];
		
		SMove.getPossibleMoves(field, movingPlayer, positionsBuffer,moves);
		
		if (moves[0] == INVALID_POSITION){
			// RECURSION: player cant move
			result = -DLS(field, movingPlayer.next(), depth + 1, limit, hash, -beta, -maxAlpha);
		} else {
			int n = getOrderedMoves(o, moves, orderedMoves, hash);
			for (int i = 0; i < n; i++) {
				GameFieldUtils.performMove(field, orderedMoves[i], movingPlayer);
				int rehash = Zobrist.rehash(hash, movingPlayer.pos, orderedMoves[i]);
				fieldStateCounter += 1;
				// RECURSION
				result = - DLS(field, movingPlayer.next(), depth + 1, limit, rehash, -beta, -maxAlpha);
				GameFieldUtils.performMove(field, SMove.unmove(orderedMoves[i]), movingPlayer);
				if (result > maxAlpha){
					maxAlpha  = result;
					if (maxAlpha >= beta) {
						// beta cutoff
						break;
					}
					currentBestMoves[depth] = SMove.setMovingPlayer(orderedMoves[i]);
					currentBestMovesScores[depth] = maxAlpha;
				}
				
			}
		 }
		return maxAlpha;
	}

	private int getOrderedMoves(OrderStrategy o, int[] moves, int[] orderedMoves, int hash) {
		int n = 0;
		for (int move : moves) {
			if (move == INVALID_POSITION){
				break;
			}	
			++n;
		}
		
		for (int i = 0; i < orderedMoves.length; i++) {
			orderedMoves[i] = moves[i];
		}
//		if (o == OrderStrategy.RANDOM) {
//			for (int i = 0; i < n; i++) {
//				xorswap(rnd.nextInt(n), rnd.nextInt(n),orderedMoves);
//			}
//		}
		return n;
		
	}

	private void xorswap(int a, int b, int[] arr) {
		arr[a] = arr[a] ^ arr[b];
		arr[b] = arr[a] ^ arr[b];
		arr[a] = arr[a] ^ arr[b];
	}
	
	int [] moves = new int[6 * 3];
	int [] positionsBuffer = new int[6];
	int [] orderedMoves = new int[6 * 3];

	public int search(long[] field, Players movingPlayer, int limit, int hash) {
		System.out.println("<-- start search for " + movingPlayer + " limit=" + limit);
		long before = System.nanoTime();
		this.stopTime = System.nanoTime() + TimeUnit.MILLISECONDS.toNanos(timeLimit - 20);
		for (int i = 0; i < limit; i++) {
//			System.out.println("" + i);
			try {
				moves = new int[6 * 3];
				positionsBuffer = new int[6];
				orderedMoves = new int[6 * 3];
				int max = DLS(field, movingPlayer, 0, i, hash, Integer.MIN_VALUE, Integer.MAX_VALUE);			
			} catch (RuntimeException e) {
				e.printStackTrace();
				System.out.println(GameFieldUtils.toString(field));
				System.out.println("Search arborted");
			}
		}
		for (int i = 0; i < currentBestMoves.length; i++) {
			if (currentBestMoves[i] == GameFieldUtils.INVALID_POSITION){
				break;
			}
			System.out.println(SMove.toString(currentBestMoves[i]));
		}
		double time = 1.0 * (System.nanoTime() - before) / TimeUnit.SECONDS.toNanos(1);
		System.out.println("time= " + time + " s");
		System.out.println("--> Search finished at max:" + maxDepth);
		return 0;
	}

	public int spawnSearch(long[] field, final Players p, final int limit) throws InterruptedException, ExecutionException {
		int[] moves = SMove.getPossibleMoves(field, p);
		final int hash = Zobrist.hash(field);
		final int[] resultField = new int[moves.length];
		for (int i = 0; i < moves.length; i++) {
			
			if (moves[i] != GameFieldUtils.INVALID_POSITION){
				final long[] rfield = field.clone();
				final int index = i;
				Runnable moveRunner = new Runnable() {
					
					@Override
					public void run() {
						resultField[index] = DLS(rfield, p, 0, limit, hash, Integer.MIN_VALUE, Integer.MAX_VALUE);			
					}
				};
				executor.execute(moveRunner);
			} else{
				break;
			}
		}
		int maxResult = Integer.MIN_VALUE;
		int maxResultIndex = 0;
		for (int i = 0; i < resultField.length; i++) {
			if (resultField[i] > maxResult){
				maxResult = resultField[i];
				maxResultIndex = i;
			}
		}
		return moves[maxResultIndex];
	}
	
	public void startThreadedSearch(long[] field, final Players p ) {
		
	}

	public void setTimeLimit(long msTimeLimit) {
		this.timeLimit = msTimeLimit;
		
	}

	public int getBestMove(long[] field, Players currentPlayer) {
		final int hash = Zobrist.hash(field);
		search(field, currentPlayer, 15, hash);
		return currentBestMoves[0];
	}

	public int getBestMove() {
		// TODO Auto-generated method stub
		return currentBestMoves[0];
	}

//	public int getLimitedResult(long[] gfield, Players currentPlayer) {
//		long [] field = gfield.clone();
//		Executors.new
//		return 0;
//	}
}
