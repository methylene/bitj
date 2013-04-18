package ooq.asdf.tools;

import static ooq.asdf.tools.UserProperties.userProperties;
import static org.slf4j.LoggerFactory.getLogger;

import java.io.File;
import java.io.IOException;

public class BlockChainFile {

	public static final String DEFAULT_FILE = "ooq.asdf.tools.bitj.blockchain";

	public static File blockChainFile() {
		final File userHome = userProperties().appHome();
		final String filename = userProperties().getUserProperty(UserProperties.Key.BLOCKCHAIN_FILE, DEFAULT_FILE);
		final File file = new File(userHome, filename);
		try {
			if (!file.createNewFile()) {
				getLogger(BlockChainFile.class).info("created blockchain file: {}", file.getCanonicalPath());
			}
		} catch (final IOException e) {
			getLogger(BlockChainFile.class).error("bad stuff", e);
		}
		return file;
	}

}
