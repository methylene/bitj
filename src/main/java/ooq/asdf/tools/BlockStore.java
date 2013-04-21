package ooq.asdf.tools;

import static ooq.asdf.tools.BlockChainFile.blockChainFile;
import static ooq.asdf.tools.Params.networkParams;
import static org.slf4j.LoggerFactory.getLogger;

import java.io.File;

import org.multibit.store.ReplayableBlockStore;

import com.google.bitcoin.store.BlockStoreException;

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
						final File blockChainFile = blockChainFile();
						getLogger(BlockStore.class).info("block chain file: {}", blockChainFile);
						blockStore = new ReplayableBlockStore(networkParams(), blockChainFile, true);
//						blockStore = new H2FullPrunedBlockStore(params(), "h2:mem:chain", fullStoreDepth());
						return blockStore;
					} catch (final BlockStoreException e) {
						throw new RuntimeException(e);
					}
				}
			}
		}
	}

}
