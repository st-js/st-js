package org.stjs.generator.utils;

import java.util.HashMap;
import java.util.Map;

public final class Timers {
	private static Map<String, Timer> timers = new HashMap<String, Timer>();

	private Timers() {
		// private
	}

	public static void start(String name) {
		synchronized (timers) {
			Timer timer = timers.get(name);
			if (timer == null) {
				timer = new Timer();
				timers.put(name, timer);
			}
			timer.start = System.currentTimeMillis();
		}
	}

	public static void end(String name) {
		synchronized (timers) {
			Timer timer = timers.get(name);
			if (timer != null) {
				timer.end = System.currentTimeMillis();
				timer.total += (timer.end - timer.start);
			}
		}
	}

	@SuppressWarnings("PMD.SystemPrintln")
	public static void dump() {
		synchronized (timers) {
			long total = 0;
			for (Map.Entry<String, Timer> entry : timers.entrySet()) {
				System.out.println(entry.getKey() + "=" + entry.getValue().getTotal());
				total += entry.getValue().getTotal();
			}
			System.out.println("----------------------------");
			System.out.println("Total:" + total);
		}
	}

	private static class Timer {
		private long total;
		private long start;
		private long end;

		public long getTotal() {
			return total;
		}

	}
}
