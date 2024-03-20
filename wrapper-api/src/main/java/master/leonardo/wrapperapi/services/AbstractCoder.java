package master.leonardo.wrapperapi.services;

import master.leonardo.wrapperapi.models.EncryptedPerson;

/**
 *
 * @param <T> Type of DTO class, which converted to Encrypted person
 */
public interface AbstractCoder<T> {
    public EncryptedPerson code(T dto);
}
