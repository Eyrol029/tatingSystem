package com.backend.backend.repository.FamilyPlanning;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.backend.backend.model.FamilyPlanning.Acknowledgement;

@Repository
public interface AcknowledgementRepository extends CrudRepository<Acknowledgement, Long> {

    List<Acknowledgement> findByClientID(Long clientID);

    // Fixed: OrderByAcknowledgementIdDesc (lowercase 'd')
    Optional<Acknowledgement> findFirstByServiceIDOrderByAcknowledgementIdDesc(Long serviceID);
}