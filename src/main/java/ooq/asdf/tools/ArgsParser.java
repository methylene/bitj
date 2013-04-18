package ooq.asdf.tools;

import static org.slf4j.LoggerFactory.getLogger;

import java.util.Map;
import java.util.concurrent.Callable;

import ooq.asdf.Action;
import ooq.asdf.DefaultAction;
import ooq.asdf.tools.CommandLine.Key;

import org.slf4j.Logger;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

public final class ArgsParser {

	private final Logger log = getLogger(ArgsParser.class);
	
	private final String[] args;

	public Runnable getAction() {
		final String action = CommandLine.commandLine().nonActionParam(CommandLine.Key.ACTION);
		final Callable<Runnable> factory = Action.factoryFor(action);
		try {
			return factory.call();
		} catch (final Exception e) {
			getLogger(ArgsParser.class).error("bad stuff", e);
			return DefaultAction.defaultRunnable();
		}
	}

	public ArgsParser(final String[] args) {
		super();
		this.args = args;
		CommandLine.init(this);
	}

	public Map<CommandLine.Key, String> getParams() {
		if (args == null)
			return ImmutableMap.of();
		if (args.length % 2 != 0) {
			log.error("uneven number of args is not allowed -- ignoring all args: `{}'",
					ImmutableList.copyOf(args));
			return ImmutableMap.of();
		}
		try {
			final ImmutableMap.Builder<CommandLine.Key, String> builder = ImmutableMap.builder();
			for (int i = 0; i < args.length; i += 2) {
				final String $name = args[i].trim();
				final String $value = args[i + 1].trim();
				final Key key;
				if ($name.startsWith("-")) {
					key = CommandLine.Key.tryParse($name.substring(1));
				} else {
					key = CommandLine.Key.tryParse($name);
				}
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
