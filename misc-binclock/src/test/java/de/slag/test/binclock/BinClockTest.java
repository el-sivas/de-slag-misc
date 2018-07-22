package de.slag.test.binclock;

import org.junit.Test;

import de.slag.binclock.BinaryClock;

public class BinClockTest {

	@Test
	public void test2() {
		BinaryClock.run();
		while (true) {
			sleep(20 * 1000);
		}
	}

	@Test
	public void test() {
		BinaryClock.run();
		sleep(10 * 1000);
		BinaryClock.addAlarm("0100");
		int i = 2;
		while (i > 1) {
			sleep(1000);
		}
		BinaryClock.stop();
	}

	private void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

}
