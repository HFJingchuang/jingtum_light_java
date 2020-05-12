package com.jccdex.keyStore;

import com.jch.client.Wallet;
import com.jch.keyStore.KeyStore;
import com.jch.keyStore.KeyStoreFile;

import junit.framework.Assert;

public class KeyStoreTest {

	public static void main(String[] args) {
		walletTest();
		keyStoreTest();
	}

	public static void walletTest() {
		Wallet wallet = Wallet.generate();
		String address = wallet.getAddress();
		String secret = wallet.getSecret();
		System.out.println("address:" + address);
		System.out.println("secret:" + secret);

		boolean isValidAddress = Wallet.isValidAddress(address);
		boolean isValidSecret = Wallet.isValidSecret(secret);
		System.out.println("isValidAddress:" + isValidAddress);
		System.out.println("isValidSecret:" + isValidSecret);
		Assert.assertEquals(true, isValidAddress);
		Assert.assertEquals(true, isValidSecret);

		Wallet wallet2 = Wallet.fromSecret(secret);
		String address2 = wallet2.getAddress();
		Assert.assertEquals(address, address2);

	}

	public static void keyStoreTest() {
		String password = "Test123567";
		Wallet wallet = Wallet.generate();
		String address = wallet.getAddress();
		String secret = wallet.getSecret();
		try {
			KeyStoreFile keyStoreFile = KeyStore.createLight(password, wallet);
			String keyStore = keyStoreFile.toString();
			System.out.println("keyStore:" + keyStore);

			Wallet walletDec = KeyStore.decrypt(password, keyStoreFile);

			String addressDec = walletDec.getAddress();
			String secretDec = walletDec.getSecret();
			Assert.assertEquals(address, addressDec);
			Assert.assertEquals(secret, secretDec);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
