package master.leonardo.wrapperapi.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class PeopleService {
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
			log.error(e.getCause().getMessage());
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
	
	public List<PersonDTO> getPeople() {
		List<EncryptedPerson> people = encryptedPeopleRepository.findAll();
		List<PersonDTO> peopleToReturn = new ArrayList<PersonDTO>();
        for (EncryptedPerson person : people) {
        	try {
        		Optional<PersonDTO> curPerson = messageDecoder.decode(person);
        		if (curPerson.isPresent()) {
        			peopleToReturn.add(curPerson.get());
        		}
        	} catch (RuntimeException e) {
        		log.error(String.format("Person with id %d has data violation", person.getId()));
        	}
        }
        return peopleToReturn;
	}

}
