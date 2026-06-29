package com.backend.backend.service;


import com.backend.backend.model.Ward;

import java.util.List;
import java.util.Optional;

public interface WardServiceInterface {

    List<Ward> getAllWards();

    Optional<Ward> getWardById(Long id);

    List<Ward> getWardsByType(String type);

    List<Ward> getWardsByStatus(String status);

    List<Ward> getWardsByFloor(String floor);

    List<Ward> getWardsByTypeAndStatus(String type, String status);

    Ward createWard(Ward ward);

    Optional<Ward> updateWard(Long id, Ward updatedWard);

    boolean deleteWard(Long id);
}
