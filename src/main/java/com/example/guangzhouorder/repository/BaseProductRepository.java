package com.example.guangzhouorder.repository;

import com.example.guangzhouorder.entity.BaseProduct;
import com.example.guangzhouorder.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BaseProductRepository extends JpaRepository<BaseProduct, Long> {
    List<BaseProduct> findByIsActiveTrueOrderByCreatedAtDesc();
    List<BaseProduct> findByIsActiveTrueAndCategoryOrderByCreatedAtDesc(Category category);
    Optional<BaseProduct> findByName(String name);
}
