package ooq.asdf.tools;

import com.google.bitcoin.core.NetworkParameters;

public class Params {
	
	public static NetworkParameters params() {
		return NetworkParameters.prodNet();
	}

}
