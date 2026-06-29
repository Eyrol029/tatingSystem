package com.backend.backend.repository;

import com.backend.backend.model.Ward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WardRepository extends JpaRepository<Ward, Long> {

    List<Ward> findByType(String type);

    List<Ward> findByStatus(String status);

    List<Ward> findByFloor(String floor);

    Optional<Ward> findByWardCode(String wardCode);

    boolean existsByWardCode(String wardCode);

    List<Ward> findByTypeAndStatus(String type, String status);
}