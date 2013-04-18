package ooq.asdf.tools;

import static ooq.asdf.tools.Params.params;
import static org.slf4j.LoggerFactory.getLogger;

import com.google.bitcoin.core.AddressFormatException;
import com.google.bitcoin.core.DumpedPrivateKey;

public final class Base58Tools {

	public static String publicFromPrivate(final String privKey) {
		try {
			final DumpedPrivateKey key = new DumpedPrivateKey(params(), privKey);
			return key.getKey().toAddress(params()).toString();
		} catch (final AddressFormatException e) {
			getLogger(Base58Tools.class).error("bad stuff", e);
			return e.getMessage();
		}
	}

}
