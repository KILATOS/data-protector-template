package org.masterleonardo.usersapi.controllers;

import jakarta.validation.Valid;
import org.masterleonardo.usersapi.dto.UserDTO;
import org.masterleonardo.usersapi.exceptions.UserNotValidException;
import org.masterleonardo.usersapi.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
public class UsersController {

    private final Environment environment;
    private final UsersService usersService;

    @Autowired
    public UsersController(Environment environment, UsersService usersService) {
        this.environment = environment;
        this.usersService = usersService;
    }

    @GetMapping("/test")
    public String test(){
        return "test from " + environment.getProperty("local.server.port");
    }

    @PostMapping("/user")
    public ResponseEntity<HttpStatus> addUser(@RequestBody @Valid UserDTO userDTO,
                                              BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            throw new UserNotValidException();
        }
        usersService.saveUser(usersService.castDTOtoModel(userDTO));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    @ExceptionHandler
    private ResponseEntity<HttpStatus> handleUserNotValidException(UserNotValidException e){
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
