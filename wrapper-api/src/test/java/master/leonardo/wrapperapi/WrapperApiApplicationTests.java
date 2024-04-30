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
import master.leonardo.wrapperapi.exceptions.IntegrityViolationOfDataException;
import master.leonardo.wrapperapi.exceptions.NoSuchPersonException;
import master.leonardo.wrapperapi.models.EncryptedPerson;
import master.leonardo.wrapperapi.services.MessageCoder;
import master.leonardo.wrapperapi.services.MessageDecoder;
import master.leonardo.wrapperapi.services.PeopleService;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class WrapperApiApplicationTests {
	
	private final MessageCoder coder;
	private final MessageDecoder decoder;
	private final PeopleService peopleService;
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
	public WrapperApiApplicationTests(MessageCoder messageCoder, MessageDecoder decoder, PeopleService peopleService) {
		this.coder = messageCoder;
		this.decoder = decoder;
		this.peopleService = peopleService;

	}

    @Test
    void contextLoads() {
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
    
    @Test
    void personServiceAddPersonTest() {
    	Assertions.assertDoesNotThrow(()->{peopleService.addPerson(testPerson);});
    }
    

    @Test
    void personServiceGetPersonRequestTest3() {
        Assertions.assertThrows(NoSuchPersonException.class, () -> peopleService.getPerson(0));
    }
    @Test
    void personServiceGetPersonRequestTest4() {
        //TODO alter user 1 and assert that the exception is thrown
    }

}
