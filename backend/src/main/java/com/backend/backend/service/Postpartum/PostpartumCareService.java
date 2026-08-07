package com.backend.backend.service.Postpartum;

import com.backend.backend.model.PostpartumCareRecord;
import java.util.List;

public interface PostpartumCareService {
    List<PostpartumCareRecord> getAllRecords();
    PostpartumCareRecord getRecordById(Long id);
    List<PostpartumCareRecord> getRecordsByServiceID(Long serviceID);
    PostpartumCareRecord createRecord(PostpartumCareRecord record);
    PostpartumCareRecord updateRecord(Long id, PostpartumCareRecord record);
    void deleteRecord(Long id);
}