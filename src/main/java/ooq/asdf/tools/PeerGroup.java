package ooq.asdf.tools;

import static ooq.asdf.tools.BlockChain.chain;
import static ooq.asdf.tools.Params.params;

import com.google.bitcoin.discovery.DnsDiscovery;

public class PeerGroup {
	
	private static volatile com.google.bitcoin.core.PeerGroup instance = null;
	
	public static com.google.bitcoin.core.PeerGroup peerGroup() {
		if (instance != null) {
			return instance;
		} else {
			synchronized (PeerGroup.class) {
				if (instance != null) {
					return instance;
				} else {
					instance = new com.google.bitcoin.core.PeerGroup(params(), chain());
					instance.addPeerDiscovery(new DnsDiscovery(params()));
					instance.startAndWait();
					return instance;
				}
			}
		}
	}

}
