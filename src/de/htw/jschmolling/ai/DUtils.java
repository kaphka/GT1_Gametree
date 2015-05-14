package de.htw.jschmolling.ai;

public class DUtils {

	public static String getFullLong(long l) {
		String s = "";
		for (int i = 0; i < 64; i++) {
			if (GameFieldUtils.isSet(l, i)) {
				s = "1" + s;
			}else{
				s = "0" + s;
			}
		}
		return s;
	}
}
