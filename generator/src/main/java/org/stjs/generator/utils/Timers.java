package org.stjs.generator.utils;

import java.util.HashMap;
import java.util.Map;

public class Timers {
	private static Map<String, Timer> timers = new HashMap<String, Timer>();

	public static synchronized void start(String name) {
		Timer timer = timers.get(name);
		if (timer == null) {
			timer = new Timer();
			timers.put(name, timer);
		}
		timer.start = System.currentTimeMillis();
	}

	public static synchronized void end(String name) {
		Timer timer = timers.get(name);
		if (timer != null) {
			timer.end = System.currentTimeMillis();
			timer.total += (timer.end - timer.start);
		}
	}

	public static synchronized void dump() {
		long total = 0;
		for (Map.Entry<String, Timer> entry : timers.entrySet()) {
			System.out.println(entry.getKey() + "=" + entry.getValue().getTotal());
			total += entry.getValue().getTotal();
		}
		System.out.println("----------------------------");
		System.out.println("Total:" + total);
	}

	private static class Timer {
		private long total = 0;
		private long start;
		private long end;

		public long getTotal() {
			return total;
		}

		public void setTotal(long total) {
			this.total = total;
		}

		public long getStart() {
			return start;
		}

		public void setStart(long start) {
			this.start = start;
		}

		public long getEnd() {
			return end;
		}

		public void setEnd(long end) {
			this.end = end;
		}

	}
}
