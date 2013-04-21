package ooq.asdf.tools;

import static ooq.asdf.tools.CommandLine.commandLine;
import static org.slf4j.LoggerFactory.getLogger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import ooq.asdf.tools.CommandLine.Key;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;

public class UserProperties {

	public enum UserPropertyKey {
		
		BLOCKCHAIN_FILE("chain");
		
		public final String key;

		private UserPropertyKey(final String key) {
			this.key = key;
		}
	}
	
	private static volatile UserProperties INSTANCE = null;
	
	public static UserProperties userProperties() {
		if (INSTANCE == null) {
			INSTANCE = new UserProperties();
		}
		return INSTANCE;
	}

	private static final String DEFAULT_PROPS = "ooq.asdf.tools.bitj.properties";
	private static final String DEFAULT_APP_HOME = "bitj";
	
	private final Map<String, String> p;
	
	private UserProperties() {
		super();
		final File f = file();
		final Properties pp = new Properties();
		FileInputStream in = null;
		try {
			if (f.createNewFile()) {
				getLogger(getClass()).info("created user properties file: " + f.getCanonicalPath());
			}
			in = new FileInputStream(f);
			pp.load(in);
		} catch (final FileNotFoundException e) {
			getLogger(UserProperties.class).error("bad stuff", e);
		} catch (final IOException e) {
			getLogger(UserProperties.class).error("bad stuff", e);
		} finally {
			try {
				in.close();
			} catch (final IOException e) {
				getLogger(getClass()).error("bad stuff", e);
			}
		}
		p = convertProps(pp);
	}
	
	private Map<String, String> convertProps(final Properties properties) {
		final Builder<String, String> builder = ImmutableMap.builder();
		for (final Entry<Object, Object> e: properties.entrySet()) {
			builder.put(e.getKey().toString(), e.getValue().toString());
		}
		return builder.build();
	}

	public String getUserProperty(final UserPropertyKey key) {
		return p.get(key.key);
	}
	
	public String getUserProperty(final UserPropertyKey key, final String defaultValue) {
		final String string = p.get(key.key);
		if (string != null) {
			return string;
		} else {
			return defaultValue;
		}
		
	}

	public File appHome() {
		final String userHome = System.getProperty("user.home");
		final String $appHome = commandLine().argument(Key.RC, DEFAULT_APP_HOME);
		final File appHome = new File(userHome, $appHome);
		if (!appHome.exists()) {
			try {
				appHome.mkdirs();
				getLogger(getClass()).info("created app home directory " + appHome.getCanonicalPath());
			} catch (final IOException e) {
				getLogger(getClass()).error("bad stuff", e);
			}
		}
		return appHome;
	}
	
	private File file() {
		final String rc = commandLine().argument(Key.RC, DEFAULT_PROPS);
		final File result;
		if (rc != null) {
			result = new File(appHome(), rc);
		} else {
			result = new File(appHome(), DEFAULT_PROPS);
		}
		PrintWriter pw = null;
		try {
			final boolean created = result.createNewFile();
			if (created) {
				getLogger(getClass()).info("created user properties: " + result.getCanonicalPath());
				if (result.length() == 0l) {
					pw = new PrintWriter(result);
					pw.format(Locale.ENGLISH, "%s = %s", UserPropertyKey.BLOCKCHAIN_FILE.key, BlockChainFile.blockChainFile().getName());
					pw.close();
				}
			}
		} catch (final IOException e) {
			getLogger(getClass()).error("bad stuff", e);
		} finally {
			if (pw != null) {
				pw.close();
			}
		}
		return result;
	}

}
