package ooq.asdf.tools;

import static ooq.asdf.tools.BlockChain.chain;
import static ooq.asdf.tools.Params.params;
import static ooq.asdf.tools.PeerGroup.peerGroup;
import static org.slf4j.LoggerFactory.getLogger;

import java.math.BigInteger;

import com.google.bitcoin.core.AddressFormatException;
import com.google.bitcoin.core.Base58;
import com.google.bitcoin.core.DumpedPrivateKey;
import com.google.bitcoin.core.ECKey;
import com.google.bitcoin.core.Wallet;

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

	public static BigInteger getBalance(final String pubKey) {
		final Wallet wallet = new Wallet(params());
		chain().addWallet(wallet);
		peerGroup().addWallet(wallet);
		try {
			wallet.keychain.add(new ECKey(null, Base58.decode(pubKey)));
			return wallet.getBalance();
		} catch (final AddressFormatException e) {
			getLogger(Base58Tools.class).error("bad stuff", e);
			return null;
		}
	}

}
