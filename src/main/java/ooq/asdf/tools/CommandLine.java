package ooq.asdf.tools;

import static org.slf4j.LoggerFactory.getLogger;

import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;

public class CommandLine {

	public enum Key {

		ACTION("a"), RC("rc");

		public final String key;

		private Key(final String key) {
			this.key = key;
		}

		private static List<Key> keysThatStartWith(final String s) {
			if (s == null || s.isEmpty()) {
				return ImmutableList.of();
			} else {
				final Builder<Key> builder = ImmutableList.builder();
				for (final Key k : values()) {
					if (k.key.startsWith(s)) {
						builder.add(k);
					}
				}
				return builder.build();
			}
		}

		public static Key tryParse(final String s) {
			final List<Key> candidates = keysThatStartWith(s);
			if (candidates.isEmpty()) {
				getLogger(CommandLine.class).warn("Ignoring unknown command line argument: `{}'", s);	
				return null;
			} else if (candidates.size() > 1) {
				getLogger(CommandLine.class).warn("Ignoring ambiguous command line argument: `{}'. Candidates: [{}]", s, candidates);
				return null;
			} else {
				return candidates.get(0);
			}
		}

	}

	private final Map<Key, String> params;

	private static volatile CommandLine INSTANCE;

	public static CommandLine init(final String[] args) {
		if (INSTANCE != null) {
			getLogger(CommandLine.class).warn("Calling init multiple times.");
			return INSTANCE;
		} else {
			INSTANCE = new CommandLine(new ArgsParser(args).getParams());
			return INSTANCE;
		}
	}

	public static CommandLine commandLine() {
		if (INSTANCE != null) {
			return INSTANCE;
		} else {
			throw new IllegalStateException("Please call init first.");
		}
	}
	
	public String argument(final Key key) {
		final String v = params.get(key);
		if (v == null) {
			getLogger(getClass()).info("Command line argument `{}' not found. Returning null", key.key);
		}
		return v;
	}
	
	public String argument(final Key key, final String defaultValue) {
		final String v = params.get(key);
		if (v != null) {
			return v;
		} else {
			getLogger(getClass()).info("Command line argument `{}' not found. Returning default value `{}'", key.key, defaultValue);
			return defaultValue;
		}
	}

	private CommandLine(final Map<Key, String> nonActionParams) {
		super();
		this.params = nonActionParams;
	}

}
