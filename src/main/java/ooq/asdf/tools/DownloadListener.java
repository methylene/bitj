package ooq.asdf.tools;

import static ooq.asdf.tools.BlockStore.blockStore;
import static ooq.asdf.tools.Wallet.wallet;
import static org.slf4j.LoggerFactory.getLogger;
import java.util.List;

import com.google.bitcoin.core.AbstractPeerEventListener;
import com.google.bitcoin.core.Block;
import com.google.bitcoin.core.BlockChainListener;
import com.google.bitcoin.core.Peer;
import com.google.bitcoin.core.PeerEventListener;
import com.google.bitcoin.core.ScriptException;
import com.google.bitcoin.core.Sha256Hash;
import com.google.bitcoin.core.StoredBlock;
import com.google.bitcoin.core.Transaction;
import com.google.bitcoin.core.VerificationException;
import com.google.bitcoin.core.AbstractBlockChain.NewBlockType;
import com.google.bitcoin.core.Wallet.BalanceType;

public class DownloadListener {
	
	private static volatile PeerEventListener INSTANCE = null;
	
	
	public static PeerEventListener downloadListener() {
		if (INSTANCE == null) {
			INSTANCE = new AbstractPeerEventListener(){
				private volatile long count = 0;
				@Override public void onBlocksDownloaded(final Peer peer, final Block block, final int blocksLeft) {
					count += 1;
					if (count % 1000 == 0 || blocksLeft == 0) {
						final String hash = block.getHashAsString().substring(0, 24);
						final int height = BlockChain.blockChain().getChainHead().getHeight();
						getLogger(DownloadListener.class).info("hash: {} remaining: {} height: {}", hash, blocksLeft, height);
					}
					if (blocksLeft == 0) {
						getLogger(DownloadListener.class).info("balance of {}: {}", wallet().getBalance(BalanceType.ESTIMATED));
					}
				}
				
			};
		}
		return INSTANCE;
	}
	
}
