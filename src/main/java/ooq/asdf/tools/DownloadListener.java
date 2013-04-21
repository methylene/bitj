package ooq.asdf.tools;

import static org.slf4j.LoggerFactory.getLogger;

import java.math.BigInteger;

import org.slf4j.Logger;

import com.google.bitcoin.core.AbstractPeerEventListener;
import com.google.bitcoin.core.Block;
import com.google.bitcoin.core.Peer;
import com.google.bitcoin.core.Wallet;
import com.google.bitcoin.core.Wallet.BalanceType;

public class DownloadListener extends AbstractPeerEventListener {

	private static final Logger LOG = getLogger(DownloadListener.class);
	private final Wallet wallet;
	private volatile long count = 0;

	public DownloadListener(final Wallet wallet) {
		super();
		this.wallet = wallet;
	}

	@Override public void onBlocksDownloaded(final Peer peer, final Block block, final int blocksLeft) {
		final String hash = block.getHashAsString().substring(0, 24);
		if (count % 1000 == 0 || blocksLeft == 0) {
			LOG.info("block: %9d hash: {} remaining: {}", count, hash, blocksLeft);
		}
		if (blocksLeft < 10) {
			final BigInteger balance = wallet.getBalance(BalanceType.ESTIMATED);
			LOG.info("block: %9d hash: {} remaining: {} balance: {}", count, hash, blocksLeft, balance);
		}
		count += 1;
	}

}
