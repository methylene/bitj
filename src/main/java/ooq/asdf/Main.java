package ooq.asdf;

import static java.util.Locale.setDefault;
import static ooq.asdf.Action.getAction;

import java.util.Locale;

import ooq.asdf.tools.CommandLine;

public class Main {

	public static void main(final String[] args) {
		setDefault(Locale.US);
		getAction(CommandLine.init(args)).run();
	}

}
