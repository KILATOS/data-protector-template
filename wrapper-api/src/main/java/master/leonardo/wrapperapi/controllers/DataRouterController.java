package master.leonardo.wrapperapi.controllers;

import master.leonardo.wrapperapi.DTO.PersonDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DataRouterController {


    @GetMapping
    public List<PersonDTO> getPeople(){
        //TODO
        return null;
    }

    @PostMapping
    public ResponseEntity<HttpStatus> putOnePersonToDB(@RequestBody PersonDTO payload){
        return null;
    }
}
