package org.masterleonardo.usersapi.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.masterleonardo.usersapi.dto.UserDTO;
import org.masterleonardo.usersapi.exceptions.UserNotValidException;
import org.masterleonardo.usersapi.models.User;
import org.masterleonardo.usersapi.services.UsersService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
@Tag(name = "Users", description = "Users management APIs")
@RestController
public class UsersController {

    private final Environment environment;
    private final UsersService usersService;
    private static final Logger logger = LogManager.getLogger(UsersController.class);

    @Autowired
    public UsersController(Environment environment, UsersService usersService) {
        this.environment = environment;
        this.usersService = usersService;
    }
    @Operation(
            summary = "Returns port number of current microservice",
            description = "Created just for testing service is alive",
            tags = { "users", "testing" })
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    content = { @Content(schema = @Schema(implementation = String.class), mediaType = "Text") },
                    description = "service works fine"),
            @ApiResponse(responseCode = "404",
                    content = { @Content(schema = @Schema()) },
                    description = "Service isn`t started"),
            @ApiResponse(responseCode = "500",
                    content = { @Content(schema = @Schema()) },
                    description = "Service isn`t started") })
    @GetMapping("/test")
    public String test(){

        return "test from " + environment.getProperty("local.server.port");
    }

    @Operation(
            summary = "Adds user to DB",
            description = "Accepts UserDTO entity with username and password, validate it, add and return one if validation was passed, " +
                    "and throws UserNotValidException otherwise",
            tags = { "users", "adding" })
    @ApiResponses({
            @ApiResponse(responseCode = "201",
                    content = { @Content(schema = @Schema(implementation = String.class), mediaType = "Text") },
                    description = "user was added to DB"),
            @ApiResponse(responseCode = "401",
                    content = { @Content(schema = @Schema()) },
                    description = "user isn`t valid") })
    @PostMapping("/user")
    public ResponseEntity<UserDTO> addUser(@RequestBody @Valid UserDTO userDTO,
                                              BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            throw new UserNotValidException();
        }
        User user = usersService.saveUser(usersService.castDTOtoModel(userDTO));
        return new ResponseEntity<>(usersService.castModelToDTO(user),HttpStatus.CREATED);
    }

    @ExceptionHandler
    private ResponseEntity<HttpStatus> handleUserNotValidException(UserNotValidException e){
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
