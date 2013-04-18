package ooq.asdf;

import static org.slf4j.LoggerFactory.getLogger;

import java.util.concurrent.Callable;

import ooq.asdf.tools.CommandLine;

import org.slf4j.Logger;

public class DefaultAction {
	
	public static Runnable defaultRunnable() {
		return new Runnable() {
			@Override public void run() {
				final Logger log = getLogger(Action.class);
				log.info("Nothing to do. Specify action using -{} [action]. Actions available: {}",
						CommandLine.Key.ACTION.key, Action.values());
			}
		};
	}
	
	public static Callable<Runnable> defaultAction() {
		return new Callable<Runnable>() {
			@Override public Runnable call() {
				return defaultRunnable();
			}
		};
	};

}
