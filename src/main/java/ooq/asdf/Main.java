package ooq.asdf;

import static ooq.asdf.Action.getAction;
import ooq.asdf.tools.CommandLine;

public final class Main {

	public static void main(final String[] args) {
		getAction(CommandLine.init(args)).run();
	}

}
