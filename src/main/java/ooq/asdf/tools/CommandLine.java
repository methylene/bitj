package ooq.asdf.tools;

import static org.slf4j.LoggerFactory.getLogger;

import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import com.google.common.collect.ImmutableMap;

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
				getLogger(CommandLine.class).warn("ignoring unknown command line argument: `{}'", s);	
				return null;
			} else if (candidates.size() > 1) {
				getLogger(CommandLine.class).warn("ignoring ambiguous command line argument: `{}', candidates: [{}]", s, candidates);
				return null;
			} else {
				return candidates.get(0);
			}
		}

	}

	private final Map<Key, String> nonActionParams;

	private static volatile CommandLine INSTANCE;

	public static CommandLine init(final ArgsParser parser) {
		if (INSTANCE != null) {
			getLogger(CommandLine.class).warn("calling init twice?");
			return INSTANCE;
		} else {
			INSTANCE = new CommandLine(parser.getParams());
			return INSTANCE;
		}
	}

	public static CommandLine commandLine() {
		if (INSTANCE != null) {
			return INSTANCE;
		} else {
			getLogger(CommandLine.class).error("call init first");
			return new CommandLine(ImmutableMap.<Key, String> of());
		}
	}
	
	public String nonActionParam(final Key key) {
		return nonActionParams.get(key);
	}
	
	public String nonActionParam(final Key key, final String defaultValue) {
		final String v = nonActionParams.get(key);
		if (v != null) {
			return v;
		} else {
			getLogger(getClass()).info("command line argument `{}' not found; using default value `{}'", key.key, defaultValue);
			return defaultValue;
		}
	}

	private CommandLine(final Map<Key, String> nonActionParams) {
		super();
		this.nonActionParams = nonActionParams;
	}

}
