package ooq.asdf.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;

public class AppFiles {

    /**
     * This enum lists all the possible keys in the user properties file
     *
     * @see ooq.asdf.tools.AppFiles#_openUserPropertiesFile()
     */
	public enum UserPropertyKey {

        /**
         *  blockstore db name
         */
		BLOCKSTORE_DB_NAME("blocks");

        private final String defaultValue;

        private UserPropertyKey(String defaultValue) {
            this.defaultValue = defaultValue;
        }


    }
	
	private static volatile AppFiles INSTANCE = null;

    private final Log log = new Log();
	
	public static AppFiles appFiles() {
		if (INSTANCE == null) {
			INSTANCE = new AppFiles();
		}
		return INSTANCE;
	}


	
	private static final String DEFAULT_APP_HOME = "bitj";
	private static final String PROPERTIES_FILE = "bitj.properties"; // in 'app home' folder
	
	private final Map<UserPropertyKey, String> properties;
	
	private AppFiles() {
		super();
		final File f = _openUserPropertiesFile();
		final Properties props = new Properties();
		FileInputStream in = null;
		try {
			in = new FileInputStream(f);
			props.load(in);
		} catch (final FileNotFoundException e) {
			log.error("Bad stuff", e);
		} catch (final IOException e) {
			log.error("Bad stuff", e);
		} finally {
			try {
				in.close();
			} catch (final IOException e) {
				log.error("Bad stuff", e);
			}
		}
		properties = convertProps(props);
	}
	
	private Map<UserPropertyKey, String> convertProps(final Properties properties) {
		final Builder<UserPropertyKey, String> builder = ImmutableMap.builder();
		for (final Entry<Object, Object> e: properties.entrySet()) {
			try {
				final String $key = e.getKey().toString().trim();
				final UserPropertyKey key = UserPropertyKey.valueOf($key.toUpperCase());
				builder.put(key, e.getValue().toString().trim());
			} catch (final RuntimeException f) {
				log.error("Bad stuff", f);
			}
		}
		return builder.build();
	}

	public String getUserProperty(final UserPropertyKey key) {
		return properties.get(key);
	}

    /**
     * @param key
     * @return a file inside the user directory with a file name (last segment) given by {@code key}
     */
	public File getFileNameProperty(final UserPropertyKey key) {
		return new File(getHome(), getUserProperty(key));
	}

	private String getUserProperty(final UserPropertyKey key, final String defaultValue) {
		final String string = getUserProperty(key);
		if (string != null) {
			return string;
		} else {
			return defaultValue;
		}
		
	}

    /**
     * @return The application directory that holds all application data.
     * This directory is called BITJ_HOME in the docs.
     */
	public File getHome() {
		final String userHome = System.getProperty("user.home");
		final String $appHome;
		if (System.getProperty("bitj.home") != null) {
			$appHome = System.getProperty("bitj.home");
		} else {
			$appHome = DEFAULT_APP_HOME;
		}
		final File appHome = new File(userHome, $appHome);
		if (!appHome.exists()) {
			try {
				appHome.mkdirs();
				log.info("Created BITJ_HOME = {}", appHome.getCanonicalPath());
			} catch (final IOException e) {
                log.error("Could not create BITJ_HOME.", e);
			}
		} else {
			try {
				log.info("Using BITJ_HOME = {}", appHome.getCanonicalPath());
			} catch (final IOException e) {
                log.error("Bad stuff", e);
			}
		}
		return appHome;
	}

    /**
     * @return The user's preference, aka rc file.
     *
     * Use {@link ooq.asdf.tools.AppFiles#getUserProperty(UserPropertyKey)}
     * to read the contents of this file.
     *
     * The file will be created with default data if not found.
     */
	private final File _openUserPropertiesFile() {
		final File result = new File(getHome(), PROPERTIES_FILE);
		if (!result.exists()) {
			PrintWriter pw = null;
			try {
				if (result.createNewFile()) {
					pw = new PrintWriter(result);
                    for (UserPropertyKey key: UserPropertyKey.values()) {
                        pw.format("%s = %s%n", key.name().toLowerCase(), key.defaultValue);
                    }
					pw.close();
				}
				log.info("Created new user properties file: {}", result.getCanonicalPath());
			} catch (final IOException e) {
                log.error("Bad stuff", e);
			} finally {
				if (pw != null) {
					pw.close();
				}
			}
		}
		return result;
	}

    /**
     * AppFiles is used in logback.groovy, so the logging system is not available here.
     */
    private static class Log {

        static enum Level {
            INFO, ERROR, WARN, DEBUG
        }

        void log(Level level, String msg, String[] params) {
            if (params != null && params.length != 0) {
                String mesg = String.format(msg.replace("{}", "%s"), params);
                System.out.format("%s [%s] %s%n", AppFiles.class.getCanonicalName(), level, mesg);
            } else {
                System.out.format("%s [%s] %s%n", AppFiles.class.getCanonicalName(), level, msg);
            }
        }

        void info(String msg) {
            log(Level.INFO, msg, null);
        }

        void info(String msg, String... params) {
            log(Level.INFO, msg, params);
        }

        void error(String msg, Exception e) {
            log(Level.ERROR, msg, null);
            e.printStackTrace();
        }

    }


}
