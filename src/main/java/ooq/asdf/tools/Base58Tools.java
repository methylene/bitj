package ooq.asdf.tools;

import org.slf4j.LoggerFactory;

import com.google.bitcoin.core.AddressFormatException;
import com.google.bitcoin.core.DumpedPrivateKey;
import com.google.bitcoin.core.NetworkParameters;

public class Base58Tools {
	
	public static String publicFromPrivate(final String privKey) {
		final NetworkParameters networkParams = NetworkParameters.prodNet();
		try {
			final DumpedPrivateKey key = new DumpedPrivateKey(networkParams, privKey);
			return key.getKey().toAddress(networkParams).toString();
		} catch (final AddressFormatException e) {
			LoggerFactory.getLogger(Base58Tools.class).error("bad stuff", e);
			return e.getMessage();
		}
	}
	
}
