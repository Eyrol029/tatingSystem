package com.backend.backend.controller.FamilyPlanning;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.backend.model.FamilyPlanning.TypeOfClient;
import com.backend.backend.service.FamilyPlanning.TypeOfClientService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/familyplanning/typeofclient")
public class TypeOfClientController {

    @Autowired
    private TypeOfClientService typeOfClientService;
@CrossOrigin(origins = "*")
    @PostMapping
    public TypeOfClient addTypeOfClient(@RequestBody TypeOfClient typeOfClient) {
        return typeOfClientService.addTypeOfClient(typeOfClient);
    }
@CrossOrigin(origins = "*")
    @GetMapping("/{id}")
    public TypeOfClient getTypeOfClient(@PathVariable Integer id) {
        return typeOfClientService.getTypeOfClientById(id);
    }
@CrossOrigin(origins = "*")
    @GetMapping
    public List<TypeOfClient> getTypeOfClients() {
        return typeOfClientService.getTypeOfClients();
    }
@CrossOrigin(origins = "*")
    @PutMapping
    public TypeOfClient updateTypeOfClient(@RequestBody TypeOfClient typeOfClient) {
        return typeOfClientService.updateTypeOfClient(typeOfClient);
    }
@CrossOrigin(origins = "*")
    @DeleteMapping("/{id}")
    public String deleteTypeOfClient(@PathVariable Integer id) {
        typeOfClientService.deleteTypeOfClient(id);
        return "TypeOfClient deleted.";
    }
@CrossOrigin(origins = "*")
    @GetMapping("/client/{clientID}")
    public List<TypeOfClient> getByClientID(@PathVariable Integer clientID) {
        return typeOfClientService.getTypeOfClientsByClientID(clientID);
    }
@CrossOrigin(origins = "*")
    @GetMapping("/search/newacceptor/{isNewAcceptor}")
    public List<TypeOfClient> getByIsNewAcceptor(@PathVariable Boolean isNewAcceptor) {
        return typeOfClientService.getTypeOfClientsByIsNewAcceptor(isNewAcceptor);
    }
@CrossOrigin(origins = "*")
    @GetMapping("/search/currentuser/{isCurrentUser}")
    public List<TypeOfClient> getByIsCurrentUser(@PathVariable Boolean isCurrentUser) {
        return typeOfClientService.getTypeOfClientsByIsCurrentUser(isCurrentUser);
    }
}
