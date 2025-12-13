package com.example.handwrittennotesapi.repository;

import com.example.handwrittennotesapi.model.OcrJob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OcrJobRepository extends JpaRepository<OcrJob, Long> {
}
