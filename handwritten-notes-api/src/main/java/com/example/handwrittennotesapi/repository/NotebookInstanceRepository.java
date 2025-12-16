package com.example.handwrittennotesapi.repository;

import com.example.handwrittennotesapi.model.NotebookInstance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NotebookInstanceRepository extends JpaRepository<NotebookInstance, Long> {
    Optional<NotebookInstance> findByQrHash(String qrHash);
}
