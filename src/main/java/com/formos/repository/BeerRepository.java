package com.formos.repository;

import com.formos.domain.Beer;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Beer entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BeerRepository extends JpaRepository<Beer, Long> {}
