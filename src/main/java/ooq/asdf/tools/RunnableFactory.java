package ooq.asdf.tools;

import java.util.Map;

public interface RunnableFactory {

	Runnable newRunnable(final Map<String, String> params);
	
}
