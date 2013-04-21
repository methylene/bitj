package ooq.asdf.tools;

import static org.slf4j.LoggerFactory.getLogger;

import java.util.Map;

import ooq.asdf.tools.CommandLine.Key;

import org.slf4j.Logger;

import com.google.common.collect.ImmutableMap;

public final class ArgsParser {

	private final Logger log = getLogger(ArgsParser.class);
	
	private final String[] args;

	public ArgsParser(final String[] args) {
		super();
		this.args = args;
	}

	public Map<CommandLine.Key, String> getParams() {
		if (args == null)
			return ImmutableMap.of();
		try {
			final ImmutableMap.Builder<Key, String> builder = ImmutableMap.builder();
			for (int i = 0; i < args.length; i += 2) {
				final String $key = args[i]; // keys at even indexes
				final String $value = i + 1 < args.length ? args[i + 1] : "";
				final Key key = Key.tryParse($key.substring($key.startsWith("-") ? 1 : 0));
				if (key != null) {
					builder.put(key, $value);
				}
			}
			return builder.build();
		} catch (final RuntimeException e) {
			log.error("bad stuff", e);
			return ImmutableMap.of();
		}
	}

}
