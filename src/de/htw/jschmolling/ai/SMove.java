package de.htw.jschmolling.ai;

import static de.htw.jschmolling.ai.GameFieldUtils.DIMENSION;
import lenz.htw.kimpl.Move;


public class SMove {
	
	public static final Move defaultMove = new Move(0, 0, 0, 0);
	private static final int SHIFT_FROM = 6;
	private static final int TO_SIDE = 0x111111;
	
	public Move getMoveObject(int smove) {
		int from = smove >> SHIFT_FROM;
		int to   = smove & TO_SIDE;
		return new Move(from % DIMENSION, from / DIMENSION, to % DIMENSION, to / DIMENSION);
	}
	
	public static int newSMove(int fromPos, int toPos) {
		return fromPos << SHIFT_FROM | toPos;
	}
	
//	public static int getFromX(int smove){
//		
//	}

}
