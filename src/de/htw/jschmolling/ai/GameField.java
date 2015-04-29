package de.htw.jschmolling.ai;


public class GameField {

	private static final int INT_NUMBER = 4;
	private static int NUMBER_OF_TOKENS = 6;
	private static int DIMENSION = 8;

	static public long[] createInital() {
		long[] field = new long[INT_NUMBER];
		
		for (Players player : Players.values()) {
			for (int i = 0; i < NUMBER_OF_TOKENS ; i++) {
				
			}			
		}
		
		
		return field;
	}
	
	static public void set(long[] field, int pos, int x, int y){
		field[pos] = field[pos] | (1l << y * DIMENSION + x);
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

	
}
