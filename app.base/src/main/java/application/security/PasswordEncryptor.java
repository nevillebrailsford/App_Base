package application.security;

import java.security.InvalidParameterException;
import java.util.logging.Logger;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

import application.definition.ApplicationConfiguration;

/**
 * This class performs encryption and decryption of strings based on an AES-256
 * coding protocol and is based on an example using a secret key and and an
 * initial random vector.
 * <p>
 * See the following page for the example:
 * https://howtodoinjava.com/java/java-security/aes-256-encryption-decryption/
 * <br>
 * 
 * @author neville
 * @version 4.1.0
 */
public class PasswordEncryptor {

	private static final String CLASS_NAME = PasswordEncryptor.class.getName();
	private static final Logger LOGGER = ApplicationConfiguration.logger();

	private static PasswordEncryptor instance = null;
	private String key = null;
	private String initVector = "RandomInitVector";

	private PasswordEncryptor() {
	}

	/**
	 * Get the singleton instance of this class.
	 * 
	 * @return
	 */
	public static PasswordEncryptor instance() {
		LOGGER.entering(CLASS_NAME, "instance");
		if (instance == null) {
			instance = new PasswordEncryptor();
		}
		LOGGER.exiting(CLASS_NAME, "instance");
		return instance;
	}

	/**
	 * Set the secret key.
	 * 
	 * @param key
	 */
	public void setKey(String key) {
		LOGGER.entering(CLASS_NAME, "setKey", "********");
		this.key = key;
		LOGGER.exiting(CLASS_NAME, "setKey");
	}

	/**
	 * Encrypt a plain string password into an AES-256 encrypted string.
	 * 
	 * @param password is the password to be encrypted. If the encryption generates
	 *                 a string longer than 64 characters an
	 *                 InvlaidParameterException will be thrown. This is to ensure
	 *                 that any encrypted passwords can be stored in the database,
	 *                 where a maximum of 65 characters is defined.
	 * @return the encrypted string.
	 * @throws Exception
	 */
	public String encrypt(String password) throws Exception {
		String result = "";
		LOGGER.entering(CLASS_NAME, "encrypt", "********");
		if (key == null) {
			IllegalStateException e = new IllegalStateException("Key has not been set");
			LOGGER.throwing(CLASS_NAME, "encrypt", e);
			LOGGER.exiting(CLASS_NAME, "encrypt");
			throw e;
		}
		try {
			IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
			SecretKeySpec sKeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
			cipher.init(Cipher.ENCRYPT_MODE, sKeySpec, iv);

			byte[] encrypted = cipher.doFinal(password.getBytes());
			result = Base64.encodeBase64String(encrypted);
			if (result.length() > 64) {
				InvalidParameterException e = new InvalidParameterException("value is too long for this application.");
				LOGGER.throwing(CLASS_NAME, "encrypt", e);
				LOGGER.exiting(CLASS_NAME, "encrypt");
				throw e;
			}
		} catch (Exception e) {
			LOGGER.warning("Caught exception: " + e.getMessage());
			LOGGER.throwing(CLASS_NAME, "encrypt", e);
			LOGGER.exiting(CLASS_NAME, "encryot");
			throw e;
		}
		LOGGER.exiting(CLASS_NAME, "encrypt", "********");
		return result;
	}

	/**
	 * Take an encrypted string and return the original password.
	 * 
	 * @param encrypted string.
	 * @return the original password.
	 * @throws Exception
	 */
	public String decrypt(String encrypted) throws Exception {
		byte[] original = null;
		LOGGER.entering(CLASS_NAME, "decrypt", "********");
		if (key == null) {
			IllegalStateException e = new IllegalStateException("Key has not been set");
			LOGGER.throwing(CLASS_NAME, "decrypt", e);
			LOGGER.exiting(CLASS_NAME, "decrypt");
			throw e;
		}
		try {
			IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
			SecretKeySpec sKeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
			cipher.init(Cipher.DECRYPT_MODE, sKeySpec, iv);

			original = cipher.doFinal(Base64.decodeBase64(encrypted));
		} catch (Exception e) {
			LOGGER.warning("Caught exception: " + e.getMessage());
			LOGGER.throwing(CLASS_NAME, "decrypt", e);
			LOGGER.exiting(CLASS_NAME, "decrypt");
			throw e;
		}
		LOGGER.exiting(CLASS_NAME, "decrypt", "********");
		return new String(original);
	}

	public static void main(String[] args) throws Exception {
		String key = "Bar12345Bar12345";

		PasswordEncryptor myEnc = PasswordEncryptor.instance();
		myEnc.setKey(key);

		System.out.println(myEnc.encrypt("01234567890").length());
		System.out.println(myEnc.decrypt(myEnc.encrypt("1234567890")));
	}
}
