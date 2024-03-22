package master.leonardo.wrapperapi.services;

import org.apache.commons.logging.LogFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import master.leonardo.wrapperapi.DTO.PersonDTO;
import master.leonardo.wrapperapi.models.EncryptedPerson;
import master.leonardo.wrapperapi.repositories.EncryptedPeopleRepository;

@Service
public class PeopleService {
	private static final Logger logger = LogManager.getLogger(PeopleService.class);
	private final MessageCoder messageCoder;
	private final EncryptedPeopleRepository encryptedPeopleRepository;
	
	@Autowired
    public PeopleService(MessageCoder messageCoder, EncryptedPeopleRepository encryptedPeopleRepository) {
        this.messageCoder = messageCoder;
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

}
