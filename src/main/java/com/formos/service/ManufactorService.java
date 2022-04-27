package com.formos.service;

import com.formos.domain.Manufactor;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Manufactor}.
 */
public interface ManufactorService {
    /**
     * Save a manufactor.
     *
     * @param manufactor the entity to save.
     * @return the persisted entity.
     */
    Manufactor save(Manufactor manufactor);

    /**
     * Updates a manufactor.
     *
     * @param manufactor the entity to update.
     * @return the persisted entity.
     */
    Manufactor update(Manufactor manufactor);

    /**
     * Partially updates a manufactor.
     *
     * @param manufactor the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Manufactor> partialUpdate(Manufactor manufactor);

    /**
     * Get all the manufactors.
     *
     * @return the list of entities.
     */
    List<Manufactor> findAll();

    /**
     * Get the "id" manufactor.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Manufactor> findOne(Long id);

    /**
     * Delete the "id" manufactor.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
