package org.masterleonardo.usersapi.repositories;

import org.masterleonardo.usersapi.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends JpaRepository<User, Long> {

}
