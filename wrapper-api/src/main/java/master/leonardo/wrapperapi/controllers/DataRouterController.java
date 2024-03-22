package master.leonardo.wrapperapi.controllers;

import master.leonardo.wrapperapi.DTO.PersonDTO;
import master.leonardo.wrapperapi.exceptions.PersonNotValidException;
import master.leonardo.wrapperapi.exceptions.PersonSaveProcessingException;
import master.leonardo.wrapperapi.responses.PersonNotValidResponse;
import master.leonardo.wrapperapi.services.PeopleService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import java.util.List;

@RestController
public class DataRouterController {


    @GetMapping
    public List<PersonDTO> getPeople(){
        //TODO
        return null;
    }

    private final PeopleService peopleService;
    
    @Autowired
    public DataRouterController(PeopleService peopleService) {
        this.peopleService = peopleService;
    }
    
    @PostMapping
    public ResponseEntity<HttpStatus> putOnePersonToDB(@RequestBody @Valid PersonDTO payload,
    													BindingResult bindingResult){
    	if (bindingResult.hasErrors()){
    		StringBuilder message = new StringBuilder();
    		for (ObjectError error : bindingResult.getAllErrors()) {
                message.append(error.getDefaultMessage());
                message.append("\n");
    		}
            throw new PersonNotValidException(message.toString());
    	}  
    	try {
    		peopleService.addPerson(payload);
    	} catch (RuntimeException e) {
    		throw new PersonSaveProcessingException(e.getCause().getMessage());
    	}
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    
    @ExceptionHandler
    private ResponseEntity<PersonNotValidResponse> handleUserNotValidException(PersonNotValidException e){
    	return new ResponseEntity<>(new PersonNotValidResponse(e.getMessage()),HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler
    private ResponseEntity<PersonSaveProcessingException> handlePersonSaveProcessingException(PersonSaveProcessingException e){
        return new ResponseEntity<>(new PersonSaveProcessingException(e.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    
}
