package ooq.asdf.tools;

public class PublicKey {
	
	public static volatile String INSTANCE = null;
	
	public static void init(final String publicKey) {
		if (INSTANCE == null) {
			INSTANCE = publicKey;
		} else {
			throw new IllegalStateException("calling init twice?");
		}
	}
	
	public static String publicKey() {
		if (INSTANCE != null) {
			return INSTANCE;
		} else {
			throw new IllegalStateException("call init first");
		}
	}

}
