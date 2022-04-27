package com.formos.service;

import com.formos.domain.Beer;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Beer}.
 */
public interface BeerService {
    /**
     * Save a beer.
     *
     * @param beer the entity to save.
     * @return the persisted entity.
     */
    Beer save(Beer beer);

    /**
     * Updates a beer.
     *
     * @param beer the entity to update.
     * @return the persisted entity.
     */
    Beer update(Beer beer);

    /**
     * Partially updates a beer.
     *
     * @param beer the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Beer> partialUpdate(Beer beer);

    /**
     * Get all the beers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Beer> findAll(Pageable pageable);

    /**
     * Get the "id" beer.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Beer> findOne(Long id);

    /**
     * Delete the "id" beer.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
