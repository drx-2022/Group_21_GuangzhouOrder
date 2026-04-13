package com.example.guangzhouorder.repository;

import com.example.guangzhouorder.entity.TransactionHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionHistoryRepository extends JpaRepository<TransactionHistory, Long> {
    List<TransactionHistory> findByBaseProductBaseProductIdOrderByCreatedAtAsc(Long baseProductId);
    List<TransactionHistory> findByProductCardProductCardIdOrderByCreatedAtAsc(Long productCardId);
}
