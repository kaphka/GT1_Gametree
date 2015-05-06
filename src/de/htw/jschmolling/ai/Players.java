package de.htw.jschmolling.ai;

/*
 * Starting Positions:
 *  y
 *  ^
 *  |
 *   0 1 2 3 4 5 6 7
 *  7  N N N N N N              
 *  6W             E  
 *  5W             E  
 *  4W             E  
 *  3W             E  
 *  2W             E  
 *  1W             E  
 *  0  S S S S S S          
 *   0 1 2 3 4 5 6 7 -> x
 * 
 * 
 * 
 */

public enum Players {
	NORTH(0),
	EAST(1),
	SOUTH(2),
	WEST(3);
	
	public static int NEUTRAL = 4;
	
	//       {0,-1}
	// {-1,0}      {1,0}[
	//       {0, 1}
	static public int[] DIR_MOVE_N = { 0,-1};
	static public int[] DIR_MOVE_E = { 1, 0};
	static public int[] DIR_MOVE_S = { 0, 1};
	static public int[] DIR_MOVE_W = {-1, 0};
	
	static public int[] DIR_STARTING_N = { 0,-1};
	static public int[] DIR_STARTING_E = { 1, 0};
	static public int[] DIR_STARTING_S = { 0, 1};
	static public int[] DIR_STARTING_W = {-1, 0};
	
	static public int[] INIT_POSITION_N = { 0, 1};
	static public int[] INIT_POSITION_E = { 1, 0};
	static public int[] INIT_POSITION_S = { 0, 1};
	static public int[] INIT_POSITION_W = {-1, 0};
	
	static public int[] NEUTRAL_DIRECTION = {0,0};
	
	static public int[][] MOVING_DIRECTIONS = {DIR_MOVE_N,DIR_MOVE_E,DIR_MOVE_S,DIR_MOVE_W};
	
	
	public final int pos;
	
	private Players( int pos){
		this.pos = pos;
	}
	
	@Override
	public String toString() {
		return Players.getShortString(pos);
	}
	
	public final static String getShortString(int n){
		switch (n) {
		case 0:
			return "N";
		case 1:
			return "E";
		case 2:
			return "S";
		case 3:
			return "W";

		default:
			return "Invalid";
		}
	}
}
