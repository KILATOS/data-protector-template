package master.leonardo.wrapperapi.services;

import java.util.Optional;

import org.apache.commons.logging.LogFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import master.leonardo.wrapperapi.DTO.PersonDTO;
import master.leonardo.wrapperapi.models.EncryptedPerson;
import master.leonardo.wrapperapi.repositories.EncryptedPeopleRepository;
import master.leonardo.wrapperapi.exceptions.IntegrityViolationOfDataException;
import master.leonardo.wrapperapi.exceptions.NoSuchPersonException;

@Service
public class PeopleService {
	private static final Logger logger = LogManager.getLogger(PeopleService.class);
	private final MessageCoder messageCoder;
	private final MessageDecoder messageDecoder;
	private final EncryptedPeopleRepository encryptedPeopleRepository;
	
	@Autowired
    public PeopleService(MessageCoder messageCoder, EncryptedPeopleRepository encryptedPeopleRepository, MessageDecoder messageDecoder) {
        this.messageCoder = messageCoder;
		this.messageDecoder = messageDecoder;
		this.encryptedPeopleRepository = encryptedPeopleRepository;
    }
	
	@Transactional
	public void addPerson(PersonDTO person) {
		EncryptedPerson personToAdd;
		try {
			personToAdd = messageCoder.code(person);
			encryptedPeopleRepository.save(personToAdd);
		} catch (RuntimeException e) {
			logger.error(e.getCause().getMessage());
			throw e;
		}	
	}
	
	public PersonDTO getPerson(long id) {
		EncryptedPerson person = encryptedPeopleRepository.findById(id).orElseThrow(
				() -> new NoSuchPersonException(String.format("Person with id %d not found", id))
		);
		Optional<PersonDTO> personToReturn;
		try {
			personToReturn = messageDecoder.decode(person);
		} catch (RuntimeException e) {
			throw new IntegrityViolationOfDataException(String.format("Person with id %d has data violation", id));
		}
		
        return personToReturn.get();
	}

}
