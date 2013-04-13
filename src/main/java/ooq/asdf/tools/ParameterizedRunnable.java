package ooq.asdf.tools;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

public abstract class ParameterizedRunnable implements Runnable {
	
	private volatile Map<String, String> params;

	public final ParameterizedRunnable setParams(final Map<String, String> params) {
		assert params == null : "attempt to set params twice";
		this.params = params;
		return this;
	}

	public final Map<String, String> getParams() {
		if (params == null)
			return ImmutableMap.of();
		return params;
	}

}
