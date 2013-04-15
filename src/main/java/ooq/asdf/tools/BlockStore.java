package ooq.asdf.tools;

import static ooq.asdf.tools.FullStoreDepth.fullStoreDepth;
import static ooq.asdf.tools.Params.params;

import com.google.bitcoin.core.FullPrunedBlockChain;
import com.google.bitcoin.store.BlockStoreException;
import com.google.bitcoin.store.H2FullPrunedBlockStore;

public class BlockStore {

	private static volatile com.google.bitcoin.store.BlockStore blockStore;

	public static com.google.bitcoin.store.BlockStore blockStore() {
		if (blockStore != null) {
			return blockStore;
		} else {
			synchronized (BlockStore.class) {
				if (blockStore != null) {
					return blockStore;
				} else {
					try {
						blockStore = new H2FullPrunedBlockStore(params(), "jdbc:h2:bs", fullStoreDepth());
						return blockStore;
					} catch (final BlockStoreException e) {
						throw new RuntimeException(e);
					}
				}
			}
		}
	}

}
