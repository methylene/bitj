package ooq.asdf.tools;

import static org.slf4j.LoggerFactory.getLogger;

import java.math.BigInteger;

import com.google.bitcoin.core.AddressFormatException;
import com.google.bitcoin.core.Base58;
import com.google.bitcoin.core.DumpedPrivateKey;
import com.google.bitcoin.core.ECKey;
import com.google.bitcoin.core.NetworkParameters;
import com.google.bitcoin.core.Wallet;

public final class Base58Tools {

	public static String publicFromPrivate(final String privKey) {
		final NetworkParameters networkParams = NetworkParameters.prodNet();
		try {
			final DumpedPrivateKey key = new DumpedPrivateKey(networkParams, privKey);
			return key.getKey().toAddress(networkParams).toString();
		} catch (final AddressFormatException e) {
			getLogger(Base58Tools.class).error("bad stuff", e);
			return e.getMessage();
		}
	}

	public static BigInteger getBalance(final String pubKey) {
		final NetworkParameters params = NetworkParameters.prodNet();
		final Wallet wallet = new Wallet(params);
		byte[] pub;
		try {
			pub = Base58.decode(pubKey);
			wallet.keychain.add(new ECKey(null, pub));
			return wallet.getBalance();
		} catch (final AddressFormatException e) {
			getLogger(Base58Tools.class).error("bad stuff", e);
			return null;
		}
	}

}
