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

import javax.crypto.Cipher;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;

public class MessageCoder implements AbstractCoder<PersonDTO>{
    private static final Logger logger = LogManager.getLogger(MessageCoder.class);


    @Override
    public EncryptedPerson code(PersonDTO dto) {
        KeyStore keyStore;
        try {
             keyStore= KeyStore.getInstance("PKCS12");
            keyStore.load(new FileInputStream("sender_keystore.p12"), "changeit".toCharArray());
            PrivateKey privateKey =
                    (PrivateKey) keyStore.getKey("senderKeyPair", "changeit".toCharArray());
        } catch (IOException | NoSuchAlgorithmException | CertificateException | KeyStoreException |
                 UnrecoverableKeyException e) {
            throw new RuntimeException(e);
        }


        ObjectMapper objectMapper = new ObjectMapper();
        String stringToEncrypt;

        try {
            stringToEncrypt = objectMapper.writeValueAsString(dto);
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }

        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
        byte[] messageHash = md.digest(stringToEncrypt.getBytes());


        return null;
    }

}
