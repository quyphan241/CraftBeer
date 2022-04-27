package com.formos.repository;

import com.formos.domain.BeerCategory;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the BeerCategory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BeerCategoryRepository extends JpaRepository<BeerCategory, Long> {}
