package master.leonardo.wrapperapi.services;

import master.leonardo.wrapperapi.DTO.PersonDTO;
import master.leonardo.wrapperapi.models.EncryptedPerson;

public class MessageDecoder implements AbstractDecoder<EncryptedPerson> {

	@Override
	public PersonDTO decode(EncryptedPerson encryptedPerson) {
		byte[] encryptedMessageHash = encryptedPerson.getSignature().getBytes();
		return null;
	}

}
