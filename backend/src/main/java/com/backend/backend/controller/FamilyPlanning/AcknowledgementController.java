package com.backend.backend.controller.FamilyPlanning;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.backend.model.FamilyPlanning.Acknowledgement;
import com.backend.backend.service.FamilyPlanning.AcknowledgementService;

@RestController
@RequestMapping("/api/familyplanning/acknowledgements")
@CrossOrigin(origins = "*")
public class AcknowledgementController {

    private final AcknowledgementService acknowledgementService;
    public AcknowledgementController(AcknowledgementService acknowledgementService) {
        this.acknowledgementService = acknowledgementService;
    }
@CrossOrigin(origins = "*")
    @PostMapping
    public ResponseEntity<Acknowledgement> createAcknowledgement(@RequestBody Acknowledgement acknowledgement) {
        Acknowledgement saved = acknowledgementService.saveOrUpdate(acknowledgement);
        return ResponseEntity.ok(saved);
    }
@CrossOrigin(origins = "*")
    @GetMapping("/client/{clientID}")
    public ResponseEntity<List<Acknowledgement>> getByClientID(@PathVariable Long clientID) {
        return ResponseEntity.ok(acknowledgementService.getByClientID(clientID));
    }
@CrossOrigin(origins = "*")
    @GetMapping("/service/{serviceID}")
    public ResponseEntity<Acknowledgement> getByServiceID(@PathVariable Long serviceID) {
        return acknowledgementService.getByServiceID(serviceID)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
