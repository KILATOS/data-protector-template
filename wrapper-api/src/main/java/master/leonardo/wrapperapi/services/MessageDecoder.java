package master.leonardo.wrapperapi.services;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import master.leonardo.wrapperapi.DTO.PersonDTO;
import master.leonardo.wrapperapi.models.EncryptedPerson;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class MessageDecoder implements AbstractDecoder<EncryptedPerson> {
	private static final Logger logger = LogManager.getLogger(MessageCoder.class);

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
			keyStore.load(new FileInputStream("../wrapper-api/src/main/resources/receiver_keystore.p12"), "changeit".toCharArray());
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
		byte[] encryptedMessageHash = Base64.getDecoder().decode(encryptedPerson.getSignature());
		
		//decrypting signature
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
		byte[] decryptedMessageHash = null;
		try {
			decryptedMessageHash = cipher.doFinal(encryptedMessageHash);
		} catch (IllegalBlockSizeException | BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String stringToDecrypt = encryptedPerson.toString();
		
		MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
        byte[] messageHash = md.digest(stringToDecrypt.getBytes());
        
        boolean isCorrect = Arrays.equals(decryptedMessageHash, messageHash);
        System.out.println(isCorrect);
		
		return null;
	}

}
