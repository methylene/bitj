package ooq.asdf.view;

import static ooq.asdf.tools.Params.networkParams;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.concurrent.Callable;

import javax.swing.JOptionPane;

import com.google.bitcoin.core.AddressFormatException;
import com.google.bitcoin.core.DumpedPrivateKey;

public final class PrivateToPublicPopup {

	public static Callable<Runnable> FACTORY = new Callable<Runnable>() {
		@Override public Runnable call() {
			return new Runnable() {
				@Override public void run() {
					final String text = JOptionPane.showInputDialog("Enter private key:");
					System.out.println(publicFromPrivate(text));
				}
			};
		}
	};
	
	public static String publicFromPrivate(final String privKey) {
		try {
			final DumpedPrivateKey key = new DumpedPrivateKey(networkParams(), privKey);
			return key.getKey().toAddress(networkParams()).toString();
		} catch (final AddressFormatException e) {
			getLogger(PrivateToPublicPopup.class).error("bad stuff", e);
			return e.getMessage();
		}
	}


}
