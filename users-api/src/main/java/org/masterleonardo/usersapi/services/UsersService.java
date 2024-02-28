package org.masterleonardo.usersapi.services;

import org.masterleonardo.usersapi.dto.UserDTO;
import org.masterleonardo.usersapi.models.User;
import org.masterleonardo.usersapi.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsersService {
    private final UsersRepository usersRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UsersService(UsersRepository usersRepository, BCryptPasswordEncoder passwordEncoder) {
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User castDTOtoModel(UserDTO userDTO){
        User user = new User();
        user.setName(userDTO.getName());
        user.setLogin(userDTO.getLogin());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setRole("ROLE_BASE");
        return user;
    }


    public void saveUser(User user){
        usersRepository.save(user);
    }
}
