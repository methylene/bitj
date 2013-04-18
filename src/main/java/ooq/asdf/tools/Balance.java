package ooq.asdf.tools;

import static ooq.asdf.tools.Wallet.wallet;

import java.math.BigInteger;

public class Balance {
	
	public volatile static BigInteger INSTANCE = null;
	
	public static void init(final String pubKey) {
		if (INSTANCE == null) {
			PublicKey.init(pubKey);
			INSTANCE = wallet().getBalance();
		} else {
			throw new IllegalStateException("calling init twice?");
		}
	}
	
	public static BigInteger balance() {
		if (INSTANCE != null) {
			return INSTANCE;
		} else {
			throw new IllegalStateException("call init first");
		}
	}

}
