package master.leonardo.wrapperapi.repositories;

import master.leonardo.wrapperapi.models.EncryptedPerson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface EncryptedPeopleRepository extends JpaRepository<EncryptedPerson, Long> {
}
