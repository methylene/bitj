package ooq.asdf;

import static ooq.asdf.tools.CommandLine.commandLine;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.List;
import java.util.concurrent.Callable;

import org.slf4j.Logger;

import ooq.asdf.tools.ArgsParser;
import ooq.asdf.tools.CommandLine;
import ooq.asdf.view.BalancePopup;
import ooq.asdf.view.PrivateToPublicPopup;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;

public enum Action {

	_DEFAULT(defaultAction()),
	CONVERT(PrivateToPublicPopup.FACTORY),
	BALANCE(BalancePopup.FACTORY);
	
	private static List<Action> keysThatStartWith(final String s) {
		if (s == null || s.isEmpty() || !Character.isAlphabetic(s.charAt(0))) {
			return ImmutableList.of();
		} else {
			final Builder<Action> builder = ImmutableList.builder();
			for (final Action k : values()) {
				if (k.name().startsWith(s.toUpperCase())) {
					builder.add(k);
				}
			}
			return builder.build();
		}
	}

	public static Action tryParse(final String s) {
		final List<Action> candidates = keysThatStartWith(s);
		if (candidates.isEmpty()) {
			getLogger(CommandLine.class).warn("ignoring unknown command line argument: `{}'", s);	
			return _DEFAULT;
		} else if (candidates.size() > 1) {
			getLogger(CommandLine.class).warn("ignoring ambiguous command line argument: `{}', candidates: [{}]", s, candidates);
			return _DEFAULT;
		} else {
			return candidates.get(0);
		}
	}

	private Action(final Callable<Runnable> factory) {
		this.factory = factory;
	}

	public final Callable<Runnable> factory;

	/**
	 * bar
	 * @param action
	 * @return
	 */
	public static Callable<Runnable> factoryFor(final String action) {
		try {
			return action == null ? defaultAction() : tryParse(action).factory;
		} catch (final RuntimeException e) {
			getLogger(Action.class).warn("parse error", e);
			return defaultAction();
		}
	}
	
	public static Runnable getAction(final CommandLine commandLine) {
		final String action = commandLine().argument(CommandLine.Key.ACTION);
		final Callable<Runnable> factory = Action.factoryFor(action);
		try {
			return factory.call();
		} catch (final Exception e) {
			getLogger(ArgsParser.class).error("bad stuff", e);
			return defaultRunnable();
		}
	}
	
	private static Runnable defaultRunnable() {
		return new Runnable() {
			@Override public void run() {
				final Logger log = getLogger(Action.class);
				log.info("Nothing to do. Specify action using -{} [action]. Actions available: {}",
						CommandLine.Key.ACTION.key, Action.values());
			}
		};
	}
	
	private static Callable<Runnable> defaultAction() {
		return new Callable<Runnable>() {
			@Override public Runnable call() {
				return defaultRunnable();
			}
		};
	};

}
