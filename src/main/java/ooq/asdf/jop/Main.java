package ooq.asdf.jop;

import static ooq.asdf.tools.BlockChain.blockChain;
import static ooq.asdf.tools.BlockChainFile.blockChainFile;
import static ooq.asdf.tools.BlockStore.blockStore;
import static ooq.asdf.tools.DownloadListener.downloadListener;
import static ooq.asdf.tools.Params.params;
import static ooq.asdf.tools.PublicKey.publicKey;
import static org.slf4j.LoggerFactory.getLogger;

import java.io.File;

import javax.swing.JOptionPane;

import org.multibit.store.ReplayableBlockStore;

import ooq.asdf.tools.Base58Tools;
import ooq.asdf.tools.BlockStore;

import com.google.bitcoin.core.AddressFormatException;
import com.google.bitcoin.core.Base58;
import com.google.bitcoin.core.BlockChain;
import com.google.bitcoin.core.ECKey;
import com.google.bitcoin.core.PeerGroup;
import com.google.bitcoin.core.Wallet;
import com.google.bitcoin.discovery.DnsDiscovery;
import com.google.bitcoin.store.BlockStoreException;

public class Main {
	
	public static void main(final String[] args) throws AddressFormatException, BlockStoreException {
		final String privKey = JOptionPane.showInputDialog("Enter private key: ");
		final Wallet wallet = new Wallet(params());
		wallet.keychain.add(new ECKey(Base58.decode(privKey), Base58.decode(Base58Tools.publicFromPrivate(privKey))));
		final File blockChainFile = blockChainFile();
		final ReplayableBlockStore blockStore = new ReplayableBlockStore(params(), blockChainFile, true);
		final BlockChain blockChain = new BlockChain(params(), blockStore);
		final PeerGroup peerGroup = new com.google.bitcoin.core.PeerGroup(params(), blockChain());
		peerGroup.addPeerDiscovery(new DnsDiscovery(params()));
		peerGroup.startAndWait();
		peerGroup.startBlockChainDownload(downloadListener());
		blockChain.addWallet(wallet);
		peerGroup.addWallet(wallet);
		peerGroup.downloadBlockChain();
	}

}
