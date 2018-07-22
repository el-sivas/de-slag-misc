package de.slag.binclock;

import java.time.LocalTime;

public class BinTimeUtils {

	private final static int PRECISION = 8;
	private final static int BEATS_PER_DAY = (int) Math.pow(2, PRECISION);
	private final static double SECONDS_PER_DAY = 24 * 60 * 60;

	static int toBinTimeBeat(final BinTime binTime) {
		final int secondOfDay = binTime.toLocalTime().toSecondOfDay();
		return (int) ((secondOfDay / SECONDS_PER_DAY) * BEATS_PER_DAY);
	}

	static LocalTime toLocalTime(final int binTimeBeats) {
		return LocalTime.ofSecondOfDay((long) (((double) binTimeBeats / BEATS_PER_DAY) * SECONDS_PER_DAY));
	}
	
	static String toBinaryString(final BinTime binTime) {
		return toBinaryString(toBinTimeBeat(binTime));
	}

	static String toBinaryString(final int binTimeBeats) {
		if (binTimeBeats < 0 || binTimeBeats > BEATS_PER_DAY) {
			throw new IllegalArgumentException("min: 0, max: " + BEATS_PER_DAY + ", current:" + binTimeBeats);
		}
		final String binaryString = Integer.toBinaryString(binTimeBeats);
		return getFillBits(binaryString.length()) + binaryString;

	}

	private static String getFillBits(int length) {
		if (length == PRECISION) {
			return "";
		}
		final StringBuilder sb = new StringBuilder();
		for (int i = 0; i < PRECISION - length; i++) {
			sb.append("0");
		}
		return sb.toString();
	}

	// static Boolean[] toBoolean(int binTimeBeats) {
	// return toBooleanList(binTimeBeats, 8).toArray(new Boolean[0]);
	// }

	// static List<Boolean> toBooleanList(int binTimeBeats, int exp) {
	// if (exp > PRECISION) {
	// throw new RuntimeException("exp > PRECISION");
	// }
	// if (exp < 0) {
	// throw new RuntimeException("exp < 0");
	// }
	// final int subtract = (int) Math.pow(2, exp);
	// }

}
