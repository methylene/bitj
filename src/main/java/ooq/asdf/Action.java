package ooq.asdf;

import static org.slf4j.LoggerFactory.getLogger;
import ooq.asdf.tools.ArgsParser;
import ooq.asdf.tools.ParameterizedRunnable;
import ooq.asdf.view.PrivateToPublicPopup;

public enum Action {
	CONVERT(PrivateToPublicPopup.SHOW_GUI);
	
	private static final ParameterizedRunnable DEFAULT_ACTION = new ParameterizedRunnable() {
		@Override public void run() {
			getLogger(Action.class).info("Nothing to do. Specify action using -{} [action]. Actions available: {}", ArgsParser.PARAM_ACTION, Action.values());
		}
	};
	
	
	private Action(final ParameterizedRunnable action) {
		this.action = action;
	}

	public final ParameterizedRunnable action;

	public static ParameterizedRunnable tryParse(final String action) {
		try {
			return action == null ? DEFAULT_ACTION : Action.valueOf(action.toUpperCase()).action;
		} catch (final RuntimeException e) {
			getLogger(Action.class).warn("parse error", e);
			return DEFAULT_ACTION;
		}
	}
}
