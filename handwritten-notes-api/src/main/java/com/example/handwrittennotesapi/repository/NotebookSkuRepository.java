package com.example.handwrittennotesapi.repository;

import com.example.handwrittennotesapi.model.NotebookSku;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NotebookSkuRepository extends JpaRepository<NotebookSku, Long> {
    Optional<NotebookSku> findByName(String name);
}
