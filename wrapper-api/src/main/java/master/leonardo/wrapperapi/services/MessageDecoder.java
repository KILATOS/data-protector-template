package master.leonardo.wrapperapi.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
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

import lombok.extern.slf4j.Slf4j;
import master.leonardo.wrapperapi.DTO.PersonDTO;
import master.leonardo.wrapperapi.DTO.PersonDTOBuilder;
import master.leonardo.wrapperapi.models.EncryptedPerson;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


/**
 * This class is responsible for decoding the encrypted person which is stored in the database.
 * Also it is responsible for validating the signature of the encrypted person.
 */
@Service
@Slf4j
public class MessageDecoder implements AbstractDecoder<EncryptedPerson> {
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
		URL privateKeyUrl = MessageCoder.class
				.getClassLoader().getResource("receiver_keystore.p12");
		File file = null;
		try {
			file = new File(privateKeyUrl.toURI());
		} catch (URISyntaxException e) {
			file = new File(privateKeyUrl.getPath());
		}
		try {
			keyStore = KeyStore.getInstance("PKCS12");
		} catch (KeyStoreException e) {
			log.error(e.getMessage());
			throw new RuntimeException(e);
		}
		try {
			keyStore.load(new FileInputStream(file), passwordToKeyFile);
		} catch (NoSuchAlgorithmException | CertificateException | IOException e) {
			log.error(e.getMessage());
			throw new RuntimeException(e);
		}
		Certificate certificate = null;
		try {
			certificate = keyStore.getCertificate("receiverKeyPair");
			
		} catch (KeyStoreException e) {
			log.error(e.getMessage());
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
			log.error(e.getMessage());
			throw new RuntimeException(e);
		}
		try {
			cipher.init(Cipher.DECRYPT_MODE, publicKey);
		} catch (InvalidKeyException e) {
			log.error(e.getMessage());
			throw new RuntimeException(e);
		}
		byte[] decryptedMessageHash = null;
		try {
			decryptedMessageHash = cipher.doFinal(encryptedMessageHash);
		} catch (IllegalBlockSizeException | BadPaddingException e) {
			log.error(e.getMessage());
			throw new RuntimeException(e);
		}
		String stringToDecrypt = encryptedPerson.toString();
		
		MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            log.error(e.getMessage());
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
        	log.error("Signature is not correct!" + encryptedPerson.toString());
        }
        return Optional.of(personToReturn);
		
	}

}
