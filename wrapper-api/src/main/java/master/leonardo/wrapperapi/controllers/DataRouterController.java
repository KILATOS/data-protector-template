package master.leonardo.wrapperapi.controllers;

import master.leonardo.wrapperapi.DTO.PersonDTO;
import master.leonardo.wrapperapi.exceptions.IntegrityViolationOfDataException;
import master.leonardo.wrapperapi.exceptions.NoSuchPersonException;
import master.leonardo.wrapperapi.exceptions.PersonNotValidException;
import master.leonardo.wrapperapi.exceptions.PersonSaveProcessingException;
import master.leonardo.wrapperapi.responses.IntegrityViolationOfDataResponse;
import master.leonardo.wrapperapi.responses.NoSuchPersonResponse;
import master.leonardo.wrapperapi.responses.PersonNotValidResponse;
import master.leonardo.wrapperapi.responses.PersonSaveProcessingResponse;
import master.leonardo.wrapperapi.services.PeopleService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import java.util.List;

@RestController
public class DataRouterController {

    private final PeopleService peopleService;
    
    @Autowired
    public DataRouterController(PeopleService peopleService) {
        this.peopleService = peopleService;
    }
    
    @GetMapping("/people")
    public ResponseEntity<List<PersonDTO>> getPeople(){
        return new ResponseEntity<>(peopleService.getPeople(), HttpStatus.OK);
    }
    
    @PostMapping("/person")
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
    /**
     * Handles PersonNotValidException thrown by the controller.
     * @param e Exception object
     * @return ResponseEntity with the error message and Http status code
     */
    @ExceptionHandler
    private ResponseEntity<PersonNotValidResponse> handleUserNotValidException(PersonNotValidException e){
    	return new ResponseEntity<>(new PersonNotValidResponse(e.getMessage()),HttpStatus.BAD_REQUEST);
    }
    
    /**
     * Handles PersonSaveProcessingException thrown by the controller.
     * @param e Exception object
     * @return ResponseEntity with the error message and Http status code
     */
    @ExceptionHandler
    private ResponseEntity<PersonSaveProcessingResponse> handlePersonSaveProcessingException(PersonSaveProcessingException e){
        return new ResponseEntity<>(new PersonSaveProcessingResponse(e.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    
    @GetMapping("/person/{id}")
    public ResponseEntity<PersonDTO> getPerson(@PathVariable long id){
    	PersonDTO personToReturn;
    	try {
    		personToReturn = peopleService.getPerson(id);
    	} catch (NoSuchPersonException | IntegrityViolationOfDataException e) {
    		throw e;
    	} 
    	return new ResponseEntity<>(personToReturn,HttpStatus.OK);
    	
    }
    
    /**
     * Handles NoSuchPersonException thrown by the controller.
     * @param e Exception object
     * @return ResponseEntity with the error message and Http status code
     */
    @ExceptionHandler
    private ResponseEntity<NoSuchPersonResponse> handleNoSuchPersonException(NoSuchPersonException e){
        return new ResponseEntity<>(new NoSuchPersonResponse(e.getMessage()),HttpStatus.NOT_FOUND);
    }
    /**
     * Handles IntegrityViolationOfDataException thrown by the controller
     * @param e exception object
     * @return response entity with the error message and Http status code
     */
    @ExceptionHandler
    private ResponseEntity<IntegrityViolationOfDataResponse> handleIntegrityViolationOfDataException(IntegrityViolationOfDataException e){
        return new ResponseEntity<>(new IntegrityViolationOfDataResponse(e.getMessage()),HttpStatus.CONFLICT);
    }
    
   
    
    
}
