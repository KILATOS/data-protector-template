package master.leonardo.wrapperapi.services;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import master.leonardo.wrapperapi.DTO.PersonDTO;
import master.leonardo.wrapperapi.models.EncryptedPerson;

public class MessageDecoder implements AbstractDecoder<EncryptedPerson> {

	@Override
	public PersonDTO decode(EncryptedPerson encryptedPerson) {
		
		//getting public key
		KeyStore keyStore = null;
		try {
			keyStore = KeyStore.getInstance("PKCS12");
		} catch (KeyStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			keyStore.load(new FileInputStream("receiver_keytore.p12"), "changeit".toCharArray());
		} catch (NoSuchAlgorithmException | CertificateException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Certificate certificate = null;
		try {
			certificate = keyStore.getCertificate("receiverKeyPair");
		} catch (KeyStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		PublicKey publicKey = certificate.getPublicKey();
		
		//getting signature
		byte[] encryptedMessageHash = encryptedPerson.getSignature().getBytes();
		
		//decrypting message hash
		Cipher cipher = null;
		try {
			cipher = Cipher.getInstance("RSA");
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			cipher.init(Cipher.DECRYPT_MODE, publicKey);
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		byte[] decryptedMessageHash;
		try {
			decryptedMessageHash = cipher.doFinal(encryptedMessageHash);
		} catch (IllegalBlockSizeException | BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return null;
	}

}
