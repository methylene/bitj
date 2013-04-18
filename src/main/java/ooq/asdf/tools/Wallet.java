package ooq.asdf.tools;

import static ooq.asdf.tools.BlockChain.blockChain;
import static ooq.asdf.tools.Params.params;
import static ooq.asdf.tools.PeerGroup.peerGroup;
import static ooq.asdf.tools.PublicKey.publicKey;
import static org.slf4j.LoggerFactory.getLogger;

import com.google.bitcoin.core.AddressFormatException;
import com.google.bitcoin.core.Base58;
import com.google.bitcoin.core.ECKey;

public class Wallet {

	private static volatile com.google.bitcoin.core.Wallet INSTANCE = null;

	public static com.google.bitcoin.core.Wallet wallet() {
		try {
			if (INSTANCE == null) {
				INSTANCE = new com.google.bitcoin.core.Wallet(params());
				INSTANCE.keychain.add(new ECKey(null, Base58.decode(publicKey())));
				blockChain().addWallet(INSTANCE);
				peerGroup().addWallet(INSTANCE);
			}
			return INSTANCE;
		} catch (final AddressFormatException e) {
			getLogger(Wallet.class).error("bad stuff", e);
			return null;
		}
	}

}
