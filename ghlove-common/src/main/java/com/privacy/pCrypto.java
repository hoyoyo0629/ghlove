package com.privacy;

import java.io.UnsupportedEncodingException;

/**
 * LOCAL DEV ONLY shim for the real com.privacy.pCrypto (libs/privacy.jar), which
 * loads a proprietary native library (pdbJava.dll) that only exists on the real
 * production/dev servers. This class shadows the jar's version on the classpath
 * so Encrypt/Decrypt calls throughout the codebase pass data through unchanged
 * instead of throwing UnsatisfiedLinkError.
 *
 * DO NOT deploy this to any shared or production environment - it disables
 * encryption entirely.
 */
public class pCrypto {

	public static String Encrypt(String mode, String data, String arg3) throws UnsupportedEncodingException {
		return data;
	}

	public static String Decrypt(String mode, String data, String arg3, int arg4) throws UnsupportedEncodingException {
		return data;
	}
}
