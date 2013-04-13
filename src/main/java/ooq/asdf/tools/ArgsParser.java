package ooq.asdf.tools;

import java.util.Map;

import ooq.asdf.Action;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

public class ArgsParser {
	
	public static final String PARAM_ACTION = "a";
	
	public static Runnable getAction(final String[] args) {
		final Map<String, String> params = getParams(args);
		return Action.tryParse(params.get(PARAM_ACTION)).setParams(getOtherParams(params));
	}
	
	private static Map<String, String> getOtherParams(final Map<String, String> params) {
		return Maps.filterKeys(params, Predicates.not(new Predicate<String>() {
			@Override public boolean apply(final String key) {
				return PARAM_ACTION.equals(key);
			}
		}));
	}

	private static Map<String, String> getParams(final String[] args) {
		if (args == null)
			return ImmutableMap.of();
		assert args.length % 2 == 0;
		final ImmutableMap.Builder<String, String> builder = ImmutableMap
				.builder();
		for (int i = 0; i < args.length; i += 2) {
			final String name = args[i].trim();
			assert name.startsWith("-") : "each param name must start with a dash";
			final String value = args[i + 1].trim();
			builder.put(name.substring(1), value);
		}
		return builder.build();
	}

}
