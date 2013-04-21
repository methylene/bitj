package ooq.asdf.view;

import static ooq.asdf.tools.BlockChain.blockChain;
import static ooq.asdf.tools.BlockChainFile.blockChainFile;
import static ooq.asdf.tools.DownloadListener.downloadListener;
import static ooq.asdf.tools.Params.networkParams;
import static org.slf4j.LoggerFactory.getLogger;

import java.io.File;
import java.util.concurrent.Callable;

import javax.swing.JOptionPane;

import org.multibit.store.ReplayableBlockStore;

import com.google.bitcoin.core.AddressFormatException;
import com.google.bitcoin.core.Base58;
import com.google.bitcoin.core.BlockChain;
import com.google.bitcoin.core.ECKey;
import com.google.bitcoin.core.PeerGroup;
import com.google.bitcoin.core.Wallet;
import com.google.bitcoin.discovery.DnsDiscovery;
import com.google.bitcoin.store.BlockStoreException;

public final class BalancePopup {

	public static Callable<Runnable> FACTORY = new Callable<Runnable>() {
		@Override public Runnable call() {
			return new Runnable() {
				@Override public void run() {
					doIt();
				}

			};
		}
	};

	private static void doIt() {
		try {
			final String $priv = JOptionPane.showInputDialog("Enter private key:");
			final Wallet wallet = new Wallet(networkParams());
			final byte[] priv = Base58.decode($priv);
			wallet.keychain.add(new ECKey(priv, null));
			final File blockChainFile = blockChainFile();
			final ReplayableBlockStore blockStore = new ReplayableBlockStore(networkParams(), blockChainFile, false);
			final BlockChain blockChain = new BlockChain(networkParams(), blockStore);
			final PeerGroup peerGroup = new com.google.bitcoin.core.PeerGroup(networkParams(), blockChain());
			peerGroup.addPeerDiscovery(new DnsDiscovery(networkParams()));
			peerGroup.startAndWait();
			peerGroup.startBlockChainDownload(downloadListener());
			blockChain.addWallet(wallet);
			peerGroup.addWallet(wallet);
			peerGroup.downloadBlockChain();
		} catch (final AddressFormatException e) {
			getLogger(BalancePopup.class).error("bad stuff", e);
		} catch (final BlockStoreException e) {
			getLogger(BalancePopup.class).error("bad stuff", e);
		}
	}

}
