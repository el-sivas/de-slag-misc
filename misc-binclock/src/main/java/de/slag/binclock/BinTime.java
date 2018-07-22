package de.slag.binclock;

import java.time.LocalTime;

public class BinTime {

	private LocalTime localTime;

	private BinTime(final LocalTime localTime) {
		this.localTime = localTime;
	}

	LocalTime toLocalTime() {
		return localTime;
	}

	public static BinTime now() {
		return of(LocalTime.now());
	}

	public static BinTime of(final LocalTime localTime) {
		return new BinTime(localTime);
	}

	@Override
	public String toString() {
		return BinTimeUtils.toBinaryString(this);
	}

	public String toString2() {
		final int beats = BinTimeUtils.toBinTimeBeat(this);
		return localTime + "; " + beats + "; " + BinTimeUtils.toLocalTime(beats) + "; " + BinTimeUtils.toBinaryString(beats);
	}
}
