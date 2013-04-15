package ooq.asdf;

import static org.slf4j.LoggerFactory.getLogger;

import java.util.Map;

import org.slf4j.Logger;

import ooq.asdf.tools.ArgsParser;
import ooq.asdf.tools.RunnableFactory;
import ooq.asdf.view.BalancePopup;
import ooq.asdf.view.PrivateToPublicPopup;

public enum Action {

	CONVERT(PrivateToPublicPopup.FACTORY),
	BALANCE(BalancePopup.FACTORY);

	private static final RunnableFactory DEFAULT_ACTION = new RunnableFactory() {
		@Override public Runnable newRunnable(final Map<String, String> params) {
			return new Runnable() {
				@Override public void run() {
					final Logger log = getLogger(Action.class);
					log.info("Nothing to do. Specify action using -{} [action]. Actions available: {}",
							ArgsParser.ACTION_PARAM, Action.values());
				}
			};
		}
	};

	private Action(final RunnableFactory factory) {
		this.factory = factory;
	}

	public final RunnableFactory factory;

	public static RunnableFactory factoryFor(final String action) {
		try {
			return action == null ? DEFAULT_ACTION : valueOf(action.toUpperCase()).factory;
		} catch (final RuntimeException e) {
			getLogger(Action.class).warn("parse error", e);
			return DEFAULT_ACTION;
		}
	}

}
