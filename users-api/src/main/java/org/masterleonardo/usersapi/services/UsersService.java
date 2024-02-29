package org.masterleonardo.usersapi.services;

import org.masterleonardo.usersapi.dto.UserDTO;
import org.masterleonardo.usersapi.models.User;
import org.masterleonardo.usersapi.repositories.UsersRepository;
import org.masterleonardo.usersapi.security.PersonDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsersService implements UserDetailsService {
    private final UsersRepository usersRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UsersService(UsersRepository usersRepository, BCryptPasswordEncoder passwordEncoder) {
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User castDTOtoModel(UserDTO userDTO){
        User user = new User();
        user.setLogin(userDTO.getLogin());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setRole("ROLE_BASE");
        return user;
    }
    public UserDTO castModelToDTO(User user){
        UserDTO userDTO = new UserDTO();
        userDTO.setLogin(user.getLogin());
        userDTO.setPassword(user.getPassword());
        return userDTO;
    }


    public User saveUser(User user){
        usersRepository.save(user);
        return user;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = usersRepository.findUserByLogin(username);
        if (user.isEmpty()) throw new UsernameNotFoundException("User with login " + username + " not found.");

        return new PersonDetails(user.get());
    }
}
