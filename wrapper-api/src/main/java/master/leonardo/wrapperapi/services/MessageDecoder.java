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
import java.util.Optional;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import master.leonardo.wrapperapi.DTO.PersonDTO;
import master.leonardo.wrapperapi.DTO.PersonDTOBuilder;
import master.leonardo.wrapperapi.models.EncryptedPerson;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;


/**
 * This class is responsible for decoding the encrypted person which is stored in the database.
 * Also it is responsible for validating the signature of the encrypted person.
 */
@Component
public class MessageDecoder implements AbstractDecoder<EncryptedPerson> {
	private static final Logger logger = LogManager.getLogger(MessageCoder.class);
	private final Environment environment;
	
    @Autowired
    public MessageDecoder(Environment environment) {
        this.environment = environment;
    }
    
    /**
     * Decodes the encrypted person and validates the signature.
     * If the signature is valid, the decrypted person is returned.
     * Otherwise the null is returned.
     * @param encryptedPerson from the database
     * @return Optional of PersonDTO
     */
	@Override
	public Optional<PersonDTO> decode(EncryptedPerson encryptedPerson) {
		
		//getting public key
		KeyStore keyStore = null;
		char[] passwordToKeyFile = environment.getProperty("encryption.passwordToPrivateKeyFile").toCharArray();
		try {
			keyStore = KeyStore.getInstance("PKCS12");
		} catch (KeyStoreException e) {
			logger.error(e.getMessage());
			throw new RuntimeException(e);
		}
		try {
			keyStore.load(new FileInputStream("../wrapper-api/src/main/resources/receiver_keystore.p12"), passwordToKeyFile);
		} catch (NoSuchAlgorithmException | CertificateException | IOException e) {
			logger.error(e.getMessage());
			throw new RuntimeException(e);
		}
		Certificate certificate = null;
		try {
			certificate = keyStore.getCertificate("receiverKeyPair");
			
		} catch (KeyStoreException e) {
			logger.error(e.getMessage());
			throw new RuntimeException(e);
		}
		PublicKey publicKey = certificate.getPublicKey();
		
		//getting signature
		byte[] encryptedMessageHash = Base64.getDecoder().decode(encryptedPerson.getSignature());
		
		//decrypting signature
		Cipher cipher = null;
		try {
			cipher = Cipher.getInstance("RSA");
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			logger.error(e.getMessage());
			throw new RuntimeException(e);
		}
		try {
			cipher.init(Cipher.DECRYPT_MODE, publicKey);
		} catch (InvalidKeyException e) {
			logger.error(e.getMessage());
			throw new RuntimeException(e);
		}
		byte[] decryptedMessageHash = null;
		try {
			decryptedMessageHash = cipher.doFinal(encryptedMessageHash);
		} catch (IllegalBlockSizeException | BadPaddingException e) {
			logger.error(e.getMessage());
			throw new RuntimeException(e);
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
        PersonDTO personToReturn = null;
        if (isCorrect) {
        	PersonDTOBuilder personDTOBuilder = new PersonDTOBuilder();
        	 personToReturn = personDTOBuilder.setActiveMember(encryptedPerson.getActiveMember())
        	.setAge(encryptedPerson.getAge())
        	.setBalance(encryptedPerson.getBalance())
        	.setChurn(encryptedPerson.getChurn())
        	.setCountry(encryptedPerson.getCountry())
        	.setCreditCard(encryptedPerson.getCreditCard())
        	.setCreditScore(encryptedPerson.getCreditScore())
        	.setEstimatedSalary(encryptedPerson.getEstimatedSalary())
        	.setGender(encryptedPerson.getGender())
        	.setProductsNumber(encryptedPerson.getProductsNumber())
        	.setTenure(encryptedPerson.getTenure())
        	.build();
        	
        } else {
        	logger.error("Signature is not correct!" + encryptedPerson.toString());
        }
        return Optional.of(personToReturn);
		
	}

}
