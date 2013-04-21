package ooq.asdf.view;

import static ooq.asdf.tools.Params.networkParams;
import static ooq.asdf.tools.AppFiles.appFiles;
import static org.slf4j.LoggerFactory.getLogger;

import java.awt.HeadlessException;
import java.io.File;
import java.util.concurrent.Callable;

import javax.swing.JOptionPane;

import ooq.asdf.tools.DownloadListener;
import ooq.asdf.tools.AppFiles.UserPropertyKey;

import com.google.bitcoin.core.AddressFormatException;
import com.google.bitcoin.core.Base58;
import com.google.bitcoin.core.BlockChain;
import com.google.bitcoin.core.ECKey;
import com.google.bitcoin.core.PeerGroup;
import com.google.bitcoin.core.Wallet;
import com.google.bitcoin.discovery.DnsDiscovery;
import com.google.bitcoin.store.BlockStore;
import com.google.bitcoin.store.H2FullPrunedBlockStore;

public final class BalancePopup {

	public static Callable<Runnable> FACTORY = new Callable<Runnable>() {
		@Override public Runnable call() {
			return new Runnable() {
				@Override public void run() {
					try {
						showBalance(Base58.decode(JOptionPane.showInputDialog("Enter private key:")));
					} catch (final HeadlessException e) {
						getLogger(BalancePopup.class).error("Bad stuff", e);
					} catch (final AddressFormatException e) {
						getLogger(BalancePopup.class).error("Bad stuff", e);
					}
				}
			};
		}
	};

	/**
	 * How to acquire a valid input to this method: Generate a private key at bitaddress.org 
	 * then convert it to bytes using 
	 * {@link com.google.bitcoin.core.Base58#decode(String) Base58#decode(String)}. 
	 * 
	 * To do: This method should should preferably require public key only.
	 * 
	 * @param priv A bitcoin account 'private key'.
	 * @see <a href="https://www.bitaddress.org/">https://www.bitaddress.org</a>
	 * @see <a href="https://en.bitcoin.it/wiki/Original_Bitcoin_client/API_calls_list">https://en.bitcoin.it/wiki/Original_Bitcoin_client/API_calls_list</a>
	 * @see <a href="http://blockexplorer.com/q/addressbalance">http://blockexplorer.com/q/addressbalance</a>
	 */
	private static void showBalance(final byte[] priv) {
		try {
			final Wallet wallet = new Wallet(networkParams());
			wallet.keychain.add(new ECKey(priv, null));
			final File file = appFiles().getFileNameProperty(UserPropertyKey.BLOCKSTORE_DB_NAME);
			final BlockStore blockStore = new H2FullPrunedBlockStore(networkParams(), file.getCanonicalPath(), 1000);
			final BlockChain blockChain = new BlockChain(networkParams(), blockStore);
			final PeerGroup peerGroup = new PeerGroup(networkParams(), blockChain);
			peerGroup.addPeerDiscovery(new DnsDiscovery(networkParams()));
			peerGroup.startAndWait();
			peerGroup.startBlockChainDownload(new DownloadListener(wallet)); // hmm ..
			blockChain.addWallet(wallet);
			peerGroup.addWallet(wallet);
			peerGroup.downloadBlockChain(); // .. same?
		} catch (final Exception e) {
			getLogger(BalancePopup.class).error("Bad stuff", e);
		}
	}

}
