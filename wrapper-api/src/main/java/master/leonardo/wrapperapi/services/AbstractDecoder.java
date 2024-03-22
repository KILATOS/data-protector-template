package master.leonardo.wrapperapi.services;

import java.util.Optional;

import master.leonardo.wrapperapi.DTO.PersonDTO;

/**
*
* @param <T> Type of encryptedPerson class, which converted to Optional<PersonDTO>
*/
public interface AbstractDecoder<T> {
	public Optional<PersonDTO> decode(T encryptedPerson);
}
