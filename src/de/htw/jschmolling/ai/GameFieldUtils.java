package de.htw.jschmolling.ai;

import lenz.htw.kimpl.Move;


public class GameFieldUtils {

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
		long[] field = new long[INT_NUMBER];
		
		for (Players player : Players.values()) {
			for (int i = 0; i < NUMBER_OF_TOKENS ; i++) {
				
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
	static public boolean isSet(long[] field, int pos, int x, int y) {
		if ((field[pos] & (1l << y * DIMENSION + x)) > 0) {
			return true;
		}else{
			return false;
		}
	}
	
	static public boolean isSet(long field, int pos) {
		if ((field & (1l << pos)) > 0) {
			return true;
		}else{
			return false;
		}
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
		System.out.println(fieldState);
		StringBuffer b = new StringBuffer(DIMENSION*DIMENSION + 8);
		for (int y = DIMENSION - 1; y >= 0; --y) {
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
		int i = 0;
		int c = 0;
		while (temp > 0) {
			System.out.println(DUtils.getFullLong(temp));
			// example: Long.numberOfTrailingZeros(0x00100) -> 2
			i = Long.numberOfTrailingZeros(temp);
			buffer[c++] = i;
			temp = temp >> (i + 1);
		}
		return buffer;
	}

	
}
