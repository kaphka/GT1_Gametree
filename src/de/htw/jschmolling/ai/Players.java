package de.htw.jschmolling.ai;


/**
 * Describes all player positions and move directions
 * 
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
	
	public static int NEUTRAL = -1;
	
	//       {0,-1}
	// {-1,0}      {1,0}
	//       {0, 1}
	static public final int[] DIR_MOVE_N = {  0, -1};
	static public final int[] DIR_MOVE_E = { -1,  0};
	static public final int[] DIR_MOVE_S = {  0,  1};
	static public final int[] DIR_MOVE_W = {  1,  0};
	

	static public final int[] DIR_MOVE_N_LEFT = {  1, -1};
	static public final int[] DIR_MOVE_E_LEFT = { -1, -1};
	static public final int[] DIR_MOVE_S_LEFT = { -1,  1};
	static public final int[] DIR_MOVE_W_LEFT = {  1,  1};
	
	
	static public final int[] DIR_MOVE_N_RIGHT = { -1, -1};
	static public final int[] DIR_MOVE_E_RIGHT = { -1,  1};
	static public final int[] DIR_MOVE_S_RIGHT = {  1,  1};
	static public final int[] DIR_MOVE_W_RIGHT = {  1, -1};
	
	static public final int[][] left =  {DIR_MOVE_N_LEFT,  DIR_MOVE_E_LEFT,  DIR_MOVE_S_LEFT,  DIR_MOVE_W_LEFT};
	static public final int[][] right = {DIR_MOVE_N_RIGHT, DIR_MOVE_E_RIGHT, DIR_MOVE_S_RIGHT, DIR_MOVE_W_RIGHT};
	
//	static public final int[] DIR_STARTING_N = { 0,-1};
//	static public final int[] DIR_STARTING_E = { 1, 0};
//	static public final int[] DIR_STARTING_S = { 0, 1};
//	static public final int[] DIR_STARTING_W = {-1, 0};
	
//	static public final int[] INIT_POSITION_N = { 0, 1};
//	static public final int[] INIT_POSITION_E = { 1, 0};
//	static public final int[] INIT_POSITION_S = { 0, 1};
//	static public final int[] INIT_POSITION_W = {-1, 0};
	
	static public final int[] NEUTRAL_DIRECTION = {0,0};
	
	static public final int[][] dir = {DIR_MOVE_N,DIR_MOVE_E,DIR_MOVE_S,DIR_MOVE_W};
	
	static public final int[] N_STARTING_POS = {57, 58, 59, 60, 61, 62};
	static public final int[] E_STARTING_POS = {15, 23, 31, 39, 47, 55};
	static public final int[] S_STARTING_POS = {1, 2, 3, 4, 5, 6};
	static public final int[] W_STARTING_POS = {8, 16, 24, 32, 40, 48};
	static public final int[][] STARTING_POSITIONS = {N_STARTING_POS, E_STARTING_POS, S_STARTING_POS, W_STARTING_POS};
	
	
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

	public Players next() {
		return Players.values()[(this.pos + 1) % Players.values().length];
	}

}
