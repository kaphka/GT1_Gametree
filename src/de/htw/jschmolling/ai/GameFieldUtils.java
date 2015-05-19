package de.htw.jschmolling.ai;

import lenz.htw.kimpl.Move;

/**
 * Helps to store the state of the board into 4 long values.
 * 
 * @author jschmolling
 *
 */
public class GameFieldUtils {

	public static final int INVALID_POSITION = 0b1000000000000;
	public final static int INT_NUMBER = 4;
	public final static int NUMBER_OF_TOKENS = 6;
	public final static int DIMENSION = 8;

	public final  static int [][] pTable = new int[DIMENSION][DIMENSION];
	public static final int DIMENSION2 = DIMENSION * DIMENSION;
	
	static {
		for (int y = 0; y < DIMENSION; y++) {
			for (int x = 0; x < DIMENSION; x++) {
				pTable[x][y] = y * DIMENSION + x;
			}
		}
	}
	static public long[] createInital() {
		long[] field = getEmptyField();
		
		for (Players player : Players.values()) {
			for (int pos : Players.STARTING_POSITIONS[player.pos]) {
				set(field, player.pos, pos);
			}			
		}
		return field;
	}
	/**
	 * sets the field corresponding to x,y to 1
	 * @param field
	 * @param pos
	 * @param x
	 * @param y
	 */
	static public void set(long[] field, int pos, int x, int y){
		field[pos] = field[pos] | (1l << y * DIMENSION + x);
	}
	
	/**
	 * sets the nth bit to 1	
	 * @param field
	 * @param pos
	 * @param i
	 */
	static public void set(long[] field, int pos, int i){
		field[pos] = field[pos] | (1l << i);
	}
	
	static public void unset(long[] field, int pos, int x, int y){
		field[pos] = field[pos] & ~(1l << y * DIMENSION + x);
	}
	
	static public long unset(long field, int bit){
		return field & ~(1l << bit);
	}
	
	
	
	static public boolean isSet(long[] field, int pos, int x, int y) {
		if ((field[pos] & (1l << y * DIMENSION + x)) > 0) {
			return true;
		}else{
			return false;
		}
	}
	
	static public boolean isSet(long field, int pos) {
		return (field & (1l << pos)) != 0;
	}
	
	static public int getPlayerNumber(long[] field, int x, int y){
		int pos = y * DIMENSION + x;
		for (Players p : Players.values()) {
			if (isSet(field[p.pos], pos)){
				return p.pos;
			}
		}
		return Players.NEUTRAL;
	}
	
	public static String toString(long[] fieldState) {
//		System.out.println(fieldState);
		StringBuffer b = new StringBuffer(DIMENSION*DIMENSION + 8);
		for (int y = DIMENSION - 1; y >= 0; --y) {
			b.append("" + y + " ");
			for (int x = 0 ; x < DIMENSION; ++x) {
				int c = 0;
				for (Players p : Players.values()) {
					if (isSet(fieldState,p.pos,x,y)){
						b.append(Players.getShortString(p.pos));						
						++c;
					}					
				}
				if (c == 0) {
					b.append("_");
				}
//				b.append(String.format("(%s,%s)", x,y));
			}
			b.append("\n");
		}
		b.append("  ");
		for (int x = 0; x < DIMENSION; x++) {
			b.append("" + x);
		}
		return b.toString();
	}
	
	public static long[] performMove(long[] fieldState, Move m, Players p) {
		long[] newState = fieldState.clone();
		unset(newState, p.pos, m.fromX, m.fromY);
		set(newState, p.pos, m.toX, m.toY);
		
		for (Players posible : Players.values()) {
			if (isSet(fieldState, posible.pos, m.toX, m.toY)) {
				unset(newState, posible.pos, m.fromX, m.fromY);
			}
		}

		
		return newState;
	}

	public static long[] getEmptyField() {
		// TODO Auto-generated method stub
		return new long[Players.values().length];
	}
	
	
	public static int[] getPlayerPositions(long playerPositions, int[] buffer) {
		long temp = playerPositions;
		int n = Long.bitCount(temp);
		int i = -1;
		int shift = 0;
		for (int c = 0; c < n; c++) {
			shift = Long.numberOfTrailingZeros(temp) + 1;
			i += shift;
//			System.out.println(DUtils.getFullLong(temp) + " i = " + i + " shift = " + shift);
			// example: Long.numberOfTrailingZeros(0x100) -> 2
			buffer[c] = i;
			temp = temp >> shift;			
		}
		for (int c = n; c < buffer.length; c++) {
			buffer[c] = INVALID_POSITION;
		}
		return buffer;
	}
	
	public static boolean isEmptyPosition(long[] field, int sposition) {
		return !isSet(field[0], sposition) &&
			   !isSet(field[1], sposition) &&
			   !isSet(field[2], sposition) &&
			   !isSet(field[3], sposition);
	}
	
	public static int getPlayerNumber(long[] field, int sposition) {
		return (int) (
			   field[0] >> sposition & 0b1 + 
			   field[1] >> sposition & 0b1 + 
			   field[2] >> sposition & 0b1 +
			   field[3] >> sposition & 0b1 - 1);
//		return !isSet(field[0], sposition) +
//			   !isSet(field[1], sposition) +
//			   !isSet(field[2], sposition) +
//			   !isSet(field[3], sposition);
	}
	
	
	public static void performMove(long[] field, int smove, Players movingPlayer) {
		int from = SMove.from(smove);
		int to   = SMove.to(smove);
		field[0] = unset(field[0], from);
		field[1] = unset(field[1], from);
		field[2] = unset(field[2], from);
		field[3] = unset(field[3], from);
		
		field[0] = unset(field[0], to);
		field[1] = unset(field[1], to);
		field[2] = unset(field[2], to);
		field[3] = unset(field[3], to);
		
		set(field, movingPlayer.pos, to);
		
		if (SMove.isUnMove(smove) && SMove.isHit(smove)){
			set(field, SMove.getHitPlayer(smove).pos, from);				
		}
		
	}

	
}
