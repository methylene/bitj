package ooq.asdf.tools;

import static ooq.asdf.tools.BlockStore.blockStore;
import static ooq.asdf.tools.Params.params;

import com.google.bitcoin.store.BlockStoreException;

public class BlockChain {

	private static volatile com.google.bitcoin.core.BlockChain instance;

	public static com.google.bitcoin.core.BlockChain chain() {
		if (instance != null) {
			return instance;
		} else {
			synchronized (BlockChain.class) {
				if (instance != null) {
					return instance;
				} else {
					try {
						instance = new com.google.bitcoin.core.BlockChain(params(), blockStore());
						return instance;
					} catch (final BlockStoreException e) {
						throw new RuntimeException(e);
					}
				}
			}
		}
	}

}
