package com.example.handwrittennotesapi.repository;

import com.example.handwrittennotesapi.model.CoinLedger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CoinLedgerRepository extends JpaRepository<CoinLedger, Long> {
    @Query("SELECT SUM(cl.amount) FROM CoinLedger cl WHERE cl.user.id = :userId")
    Integer getBalanceByUserId(Long userId);

    List<CoinLedger> findByUserId(Long userId);
}
