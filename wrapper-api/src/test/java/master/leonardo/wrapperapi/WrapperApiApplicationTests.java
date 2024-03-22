package master.leonardo.wrapperapi;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import master.leonardo.wrapperapi.DTO.PersonDTO;
import master.leonardo.wrapperapi.DTO.PersonDTOBuilder;
import master.leonardo.wrapperapi.models.EncryptedPerson;
import master.leonardo.wrapperapi.services.MessageCoder;
import master.leonardo.wrapperapi.services.MessageDecoder;

@SpringBootTest
class WrapperApiApplicationTests {
	
	private final MessageCoder coder;
	private final MessageDecoder decoder;
	private static final PersonDTO testPerson;
	
	static {
		PersonDTOBuilder personDTOBuilder = new PersonDTOBuilder();
		testPerson = personDTOBuilder.setCreditScore(619)
		.setCountry("France")
		.setGender("Female")
		.setAge(42)
		.setTenure(2)
		.setBalance(0)
		.setProductsNumber(1)
		.setCreditCard(1)
		.setActiveMember(1)
		.setEstimatedSalary(101348.88)
		.setChurn(1)
		.build();
	}
	
	@Autowired
	public WrapperApiApplicationTests(MessageCoder messageCoder, MessageDecoder decoder) {
		this.coder = messageCoder;
		this.decoder = decoder;
		
	}

    @Test
    void contextLoads() {
    }
    
    @Test
    void messageCoderTest() {
    	EncryptedPerson person = coder.code(testPerson);
    	String signature = person.getSignature();
    	decoder.decode(person).get();
    	Assertions.assertEquals(Arrays.toString(Base64.getDecoder().decode(signature)), "[13, 12, -122, 0, -54, -9, 13, 71, -65, -85, -5, 99, -61, 27, -9, 59, 103, 51, 63, 82, -65, -72, -51, 9, -68, 93, 110, 34, -6, 103, -56, 16, 49, -56, -97, -120, -93, 25, -85, 61, -75, -109, 107, 51, 6, -80, 91, -22, 33, -7, -14, -78, -85, 12, -39, -114, 39, 80, 49, 32, 62, -23, -43, -24, -4, 40, -83, 23, 54, -71, 102, 90, 105, -103, -105, -122, 113, 10, -109, 94, -114, -18, -116, 105, -99, -24, 123, 30, -27, -88, -99, 108, 66, -80, 38, -50, 26, 88, 4, -15, -44, 68, -98, -60, -56, -77, -2, 30, 47, 73, -97, 54, -36, 43, -84, 68, 113, -121, 37, -2, 121, 18, 47, -101, -87, -22, -80, 7, -51, -112, 17, 36, -96, 35, -109, 65, -62, -32, -56, 46, 62, -56, 58, -79, -77, 70, -69, 86, -67, -109, 84, -3, -120, 97, -112, 31, -20, 119, 58, 94, 107, 30, -111, -94, -19, -27, 114, -30, -75, -120, -12, -94, 120, 85, 39, 116, -50, -81, -43, 110, -93, -34, -72, 16, 37, 118, -28, -81, 99, 59, -32, -115, 6, 84, -11, 20, 28, 2, -107, 119, 96, 15, -46, -89, 75, 25, 77, 51, 113, 18, 103, 91, -12, 90, 64, -52, 34, -15, 90, 121, 48, -122, 115, -116, -21, -39, -58, 14, 107, -2, -31, 123, 110, 83, -25, -18, 5, 62, -101, 9, 3, -42, 60, 26, 28, 113, 85, 124, -19, -93, -7, 38, -52, 27, 64, 22]",
    			"Something wrong with the decoder or coder");
    	
    }
    
    @Test
    void messageDecoderTest1() {
    	EncryptedPerson person = coder.code(testPerson);
    	Optional<PersonDTO> decodedPerson = decoder.decode(person);
    	Assertions.assertTrue(decodedPerson.isPresent());
    }
    @Test
    void messageDecoderTest2() {
    	EncryptedPerson person = coder.code(testPerson);
        Optional<PersonDTO> decodedPerson = decoder.decode(person);
        Assertions.assertTrue(decodedPerson.get().equals(testPerson));
    }

}
