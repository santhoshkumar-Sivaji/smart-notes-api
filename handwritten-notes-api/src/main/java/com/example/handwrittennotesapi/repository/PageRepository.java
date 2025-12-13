package com.example.handwrittennotesapi.repository;

import com.example.handwrittennotesapi.model.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PageRepository extends JpaRepository<Page, Long> {
    Optional<Page> findByUserIdAndClientPageId(Long userId, String clientPageId);
    List<Page> findByUserId(Long userId);
}
