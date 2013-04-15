package ooq.asdf.tools;

import static org.slf4j.LoggerFactory.getLogger;

import java.util.Map;

import org.slf4j.Logger;

import ooq.asdf.Action;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

public final class ArgsParser {

	public static final String ACTION_PARAM = "a";

	private final Logger log = getLogger(ArgsParser.class);

	public static Runnable getAction(final String[] args) {
		final ArgsParser parser = new ArgsParser();
		final Map<String, String> commandLineParams = parser.getParams(args);
		final Map<String, String> nonActionParams = parser.getNonActionParams(commandLineParams);
		final String actionParam = parser.getActionParam(commandLineParams);
		return Action.factoryFor(actionParam).newRunnable(nonActionParams);
	}

	private String getActionParam(final Map<String, String> params) {
		final String actionParam = params.get(ACTION_PARAM);
		if (actionParam == null) {
			log.warn("no action param found, try -{} {}", ACTION_PARAM, Action.values());
		}
		return actionParam;
	}

	private Map<String, String> getNonActionParams(final Map<String, String> params) {
		return Maps.filterKeys(params, Predicates.not(new Predicate<String>() {
			@Override public boolean apply(final String key) {
				return ACTION_PARAM.equals(key);
			}
		}));
	}

	private Map<String, String> getParams(final String[] args) {
		if (args == null)
			return ImmutableMap.of();
		if (args.length % 2 != 0) {
			log.error("uneven number of args is not allowed -- ignoring all args: `{}'",
					ImmutableList.copyOf(args));
			return ImmutableMap.of();
		}
		try {
			final ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();
			for (int i = 0; i < args.length; i += 2) {
				final String name = args[i].trim();
				final String value = args[i + 1].trim();
				if (name.startsWith("-")) {
					final String nameNoDash = name.substring(1);
					builder.put(nameNoDash, value);
				} else {
					final Logger log = getLogger(ArgsParser.class);
					log.error("each param name must start with a dash -- ignoring param pair: `{} {}'",
							name, value);
				}
			}
			return builder.build();
		} catch (final RuntimeException e) {
			log.error("bad stuff", e);
			return ImmutableMap.of();
		}
	}

}
