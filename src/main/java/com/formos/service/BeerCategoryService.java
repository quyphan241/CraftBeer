package com.formos.service;

import com.formos.domain.BeerCategory;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link BeerCategory}.
 */
public interface BeerCategoryService {
    /**
     * Save a beerCategory.
     *
     * @param beerCategory the entity to save.
     * @return the persisted entity.
     */
    BeerCategory save(BeerCategory beerCategory);

    /**
     * Updates a beerCategory.
     *
     * @param beerCategory the entity to update.
     * @return the persisted entity.
     */
    BeerCategory update(BeerCategory beerCategory);

    /**
     * Partially updates a beerCategory.
     *
     * @param beerCategory the entity to update partially.
     * @return the persisted entity.
     */
    Optional<BeerCategory> partialUpdate(BeerCategory beerCategory);

    /**
     * Get all the beerCategories.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<BeerCategory> findAll(Pageable pageable);

    /**
     * Get the "id" beerCategory.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<BeerCategory> findOne(Long id);

    /**
     * Delete the "id" beerCategory.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
