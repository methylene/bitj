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

public class AppFiles {

	public enum UserPropertyKey {
		
		BLOCKSTORE;
		
	}
	
	private static volatile AppFiles INSTANCE = null;
	
	public static AppFiles userProperties() {
		if (INSTANCE == null) {
			INSTANCE = new AppFiles();
		}
		return INSTANCE;
	}

	private static final String DEFAULT_H2 = "blocks";
	
	private static final String DEFAULT_APP_HOME = "bitj";
	private static final String PROPERTIES_FILE = "bitj.properties"; // in 'app home' folder
	
	private final Map<UserPropertyKey, String> properties;
	
	private AppFiles() {
		super();
		final File f = openUserPropertiesFile();
		final Properties props = new Properties();
		FileInputStream in = null;
		try {
			in = new FileInputStream(f);
			props.load(in);
		} catch (final FileNotFoundException e) {
			getLogger(AppFiles.class).error("Bad stuff", e);
		} catch (final IOException e) {
			getLogger(AppFiles.class).error("Bad stuff", e);
		} finally {
			try {
				in.close();
			} catch (final IOException e) {
				getLogger(getClass()).error("Bad stuff", e);
			}
		}
		properties = convertProps(props);
	}
	
	private Map<UserPropertyKey, String> convertProps(final Properties properties) {
		final Builder<UserPropertyKey, String> builder = ImmutableMap.builder();
		for (final Entry<Object, Object> e: properties.entrySet()) {
			try {
				final String $key = e.getKey().toString().trim().toUpperCase(Locale.ENGLISH);
				final UserPropertyKey key = UserPropertyKey.valueOf($key);
				builder.put(key, e.getValue().toString().trim());
			} catch (final RuntimeException f) {
				getLogger(getClass()).error("Unrecognized option: {}", e.getKey().toString());
			}
		}
		return builder.build();
	}

	public String getUserProperty(final UserPropertyKey key) {
		return properties.get(key);
	}
	
	public File getUserFile(final UserPropertyKey key) {
		return new File(appHome(), properties.get(key));
	}
	
	public String getUserFileName(final UserPropertyKey key) throws IOException {
		return getUserFile(key).getCanonicalPath();
	}
	
	public String getUserProperty(final UserPropertyKey key, final String defaultValue) {
		final String string = properties.get(key);
		if (string != null) {
			return string;
		} else {
			return defaultValue;
		}
		
	}

	public File appHome() {
		final String userHome = System.getProperty("user.home");
		final String $appHome;
		if (commandLine().argument(Key.RC) != null) {
			$appHome = commandLine().argument(Key.RC);
		} else {
			$appHome = DEFAULT_APP_HOME;
		}
		final File appHome = new File(userHome, $appHome);
		if (!appHome.exists()) {
			try {
				appHome.mkdirs();
				getLogger(getClass()).info("Created app home directory: " + appHome.getCanonicalPath());
			} catch (final IOException e) {
				getLogger(getClass()).error("Bad stuff", e);
			}
		} else {
			try {
				getLogger(getClass()).info("Using app home directory: " + appHome.getCanonicalPath());
			} catch (final IOException e) {
				getLogger(getClass()).error("Bad stuff", e);
			}
		}
		return appHome;
	}
	
	private File openUserPropertiesFile() {
		final File result = new File(appHome(), PROPERTIES_FILE);
		if (!result.exists()) {
			PrintWriter pw = null;
			try {
				result.createNewFile();
				if (result.length() == 0l) {
					pw = new PrintWriter(result);
					final String k = UserPropertyKey.BLOCKSTORE.name().toLowerCase(Locale.ENGLISH);
					pw.format(Locale.ENGLISH, "%s = %s", k, DEFAULT_H2);
					pw.close();
				}
				getLogger(getClass()).info("Created user properties: " + result.getCanonicalPath());
			} catch (final IOException e) {
				getLogger(getClass()).error("Bad stuff", e);
			} finally {
				if (pw != null) {
					pw.close();
				}
			}
		}
		return result;
	}
	
}
