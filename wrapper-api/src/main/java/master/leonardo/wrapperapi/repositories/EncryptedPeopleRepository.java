package master.leonardo.wrapperapi.repositories;

import master.leonardo.wrapperapi.models.EncryptedPerson;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EncryptedPeopleRepository extends JpaRepository<EncryptedPerson, Long> {
}
