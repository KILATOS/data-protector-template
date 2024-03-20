package master.leonardo.wrapperapi.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import master.leonardo.wrapperapi.DTO.PersonDTO;
import master.leonardo.wrapperapi.models.EncryptedPerson;
import org.apache.commons.codec.digest.MessageDigestAlgorithms;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.DigestInfo;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;
/**
 * MessageCoder class`s purpose is to encode PersonDTO to EncodedPerson, to write it in database.
 * 1) Firstly, we load private key from sender_keystore.p12 file 
 * 2) Next, we convert DTO object to JSON with ObjectMapper
 * 3) Then, generate hash of Json with MessageDigest
 * 4) Finally, encrypt latter hash with Cipher class
 * 
 *  
 */
@Component
public class MessageCoder implements AbstractCoder<PersonDTO>{
	/**
	 * Just logger
	 */
    private static final Logger logger = LogManager.getLogger(MessageCoder.class);
    private final Environment environment;
    
    @Autowired
    public MessageCoder(Environment environment) {
		this.environment = environment;
    	
    }
    
    
    /**
     * The only one method, which deals with the whole business logic
     * @param PersonDTO is the object to convert
     * @return the object which represents encrypted personDTO object
     */

    @Override
    public EncryptedPerson code(PersonDTO dto) {
    	//Private key loading
        KeyStore keyStore;
        PrivateKey privateKey;
        
        try {
            keyStore= KeyStore.getInstance("PKCS12");
            char[] passwordToKeyFile = environment.getProperty("encryption.passwordToPrivateKeyFile").toCharArray();
			keyStore.load(new FileInputStream("sender_keystore.p12"), passwordToKeyFile);
            privateKey =
                    (PrivateKey) keyStore.getKey("senderKeyPair", passwordToKeyFile);
        } catch (IOException | NoSuchAlgorithmException | CertificateException | KeyStoreException |
                 UnrecoverableKeyException e) {
            throw new RuntimeException(e);
        }

        //converting DTO object to JSON string
        ObjectMapper objectMapper = new ObjectMapper();
        String stringToEncrypt;

        try {
            stringToEncrypt = objectMapper.writeValueAsString(dto);
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
        
        //message hash generating
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
        byte[] messageHash = md.digest(stringToEncrypt.getBytes());
        
        //encrypting of generated hash
        Cipher cipher;
        byte[] digitalSignature;
		try {
			cipher = Cipher.getInstance("RSA");
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			logger.error(e.getMessage());
			throw new RuntimeException(e);
		} 
        try {
			cipher.init(Cipher.ENCRYPT_MODE, privateKey);
		} catch (InvalidKeyException e) {
			logger.error(e.getMessage());
			throw new RuntimeException(e);
		}
        try {
        	digitalSignature = cipher.doFinal(messageHash);
		} catch (IllegalBlockSizeException | BadPaddingException e) {
			logger.error(e.getMessage());
			throw new RuntimeException(e);
		}
        EncryptedPerson encryptedPerson = new EncryptedPerson(new String(messageHash), new String(digitalSignature));


        return encryptedPerson;
    }

}
