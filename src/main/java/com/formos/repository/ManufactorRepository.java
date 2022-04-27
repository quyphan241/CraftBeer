package com.formos.repository;

import com.formos.domain.Manufactor;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Manufactor entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ManufactorRepository extends JpaRepository<Manufactor, Long> {}
