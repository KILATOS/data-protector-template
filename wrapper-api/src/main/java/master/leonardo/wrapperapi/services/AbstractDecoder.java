package master.leonardo.wrapperapi.services;

import master.leonardo.wrapperapi.DTO.PersonDTO;

/**
*
* @param <T> Type of encryptedPerson class, which converted to PersonDTO
*/
public interface AbstractDecoder<T> {
	public PersonDTO decode(T encryptedPerson);
}
