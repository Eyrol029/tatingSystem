package com.backend.backend.repository.Postpartum;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backend.backend.model.Postpartum.PostpartumCareRecord;

@Repository
public interface PostpartumCareRepository extends JpaRepository<PostpartumCareRecord, Long> {
    List<PostpartumCareRecord> findByServiceID(Long serviceID);
}
