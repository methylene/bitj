package ooq.asdf.tools;

import static ooq.asdf.tools.BlockStore.blockStore;
import static ooq.asdf.tools.Params.params;

import com.google.bitcoin.store.BlockStoreException;

public class BlockChain {

	private static volatile com.google.bitcoin.core.BlockChain INSTANCE;

	public static com.google.bitcoin.core.BlockChain chain() {
		if (INSTANCE != null) {
			return INSTANCE;
		} else {
			synchronized (BlockChain.class) {
				if (INSTANCE != null) {
					return INSTANCE;
				} else {
					try {
						INSTANCE = new com.google.bitcoin.core.BlockChain(params(), blockStore());
						return INSTANCE;
					} catch (final BlockStoreException e) {
						throw new RuntimeException(e);
					}
				}
			}
		}
	}

}
