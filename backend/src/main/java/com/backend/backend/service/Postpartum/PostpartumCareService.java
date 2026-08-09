package com.backend.backend.service.Postpartum;

import java.util.List;

import com.backend.backend.model.Postpartum.PostpartumCareRecord;

public interface PostpartumCareService {
    List<PostpartumCareRecord> getAllRecords();

    PostpartumCareRecord getRecordById(Long id);
    List<PostpartumCareRecord> getRecordsByServiceID(Long serviceID);
    PostpartumCareRecord createRecord(PostpartumCareRecord record);
    PostpartumCareRecord updateRecord(Long id, PostpartumCareRecord record);
    void deleteRecord(Long id);
}