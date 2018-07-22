package de.slag.binclock;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class BinaryClock {

	private static final Logger LOG = LogFactory.getLog(BinaryClock.class);

	private static ClockRunner runner;

	private static Thread thread;

	private static BinaryClock bc;

	private static BinaryClock getInstance() {
		if (bc == null) {
			bc = new BinaryClock();
		}
		return bc;
	}

	private void init() {
		runner = new ClockRunner();
		thread = new Thread(runner);
		thread.start();
	}

	private void stopInternal() {
		runner.interrupt();
	}

	public static void run() {
		getInstance().init();
	}

	public static void addAlarm(String alarmPattern) {
		if (alarmPattern.length() > 8) {
			throw new RuntimeException();
		}
		runner.map.put(alarmPattern, false);
		LOG.info("alarm added: " + alarmPattern);
	}

	public static void stop() {
		getInstance().stopInternal();
		while (thread.isAlive()) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}
		System.out.println("end");
	}

	private class ClockRunner implements Runnable {

		private long lastPrinted = 0;

		private boolean interrupted = false;

		private Map<String, Boolean> map = new HashMap<String, Boolean>();

		public void run() {
			while (!interrupted) {
				if (System.currentTimeMillis() > lastPrinted + 10000) {
					lastPrinted = System.currentTimeMillis();
					System.out.println(BinTime.now().toString2() + " " + map);
				}

				ring();

				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				}
			}
		}

		private void ring() {
			for (final String string : map.keySet()) {
				boolean startsWith = BinTime.now().toString().startsWith(string);
				if (startsWith && !map.get(string)) {
					System.out.println("Bing!");
					map.put(string, true);
				}
			}
		}

		private void interrupt() {
			interrupted = true;
		}

	}

}
