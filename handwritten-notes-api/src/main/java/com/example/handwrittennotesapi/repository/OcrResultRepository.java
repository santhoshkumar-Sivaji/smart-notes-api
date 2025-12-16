package com.example.handwrittennotesapi.repository;

import com.example.handwrittennotesapi.model.OcrResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OcrResultRepository extends JpaRepository<OcrResult, Long> {

    @Query("SELECT o FROM OcrResult o WHERE o.page.user.id = :userId AND o.extractedText LIKE %:query%")
    List<OcrResult> searchByUserIdAndQuery(Long userId, String query);
}
