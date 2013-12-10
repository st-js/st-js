package org.stjs.generator.writer.w;

import org.junit.Test;
import org.stjs.generator.utils.Timers;

public class DumpTest {

	@Test
	public void testNothing() {
		Thread th = new Thread() {
			@Override
			public void run() {
				while (true) {
					try {
						sleep(1000);
						Timers.dump();
					}
					catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		};
		th.setDaemon(true);
		th.start();

	}
}
