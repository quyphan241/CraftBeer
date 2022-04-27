package com.formos.service.impl;

import com.formos.domain.BeerCategory;
import com.formos.repository.BeerCategoryRepository;
import com.formos.service.BeerCategoryService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link BeerCategory}.
 */
@Service
@Transactional
public class BeerCategoryServiceImpl implements BeerCategoryService {

    private final Logger log = LoggerFactory.getLogger(BeerCategoryServiceImpl.class);

    private final BeerCategoryRepository beerCategoryRepository;

    public BeerCategoryServiceImpl(BeerCategoryRepository beerCategoryRepository) {
        this.beerCategoryRepository = beerCategoryRepository;
    }

    @Override
    public BeerCategory save(BeerCategory beerCategory) {
        log.debug("Request to save BeerCategory : {}", beerCategory);
        return beerCategoryRepository.save(beerCategory);
    }

    @Override
    public BeerCategory update(BeerCategory beerCategory) {
        log.debug("Request to save BeerCategory : {}", beerCategory);
        return beerCategoryRepository.save(beerCategory);
    }

    @Override
    public Optional<BeerCategory> partialUpdate(BeerCategory beerCategory) {
        log.debug("Request to partially update BeerCategory : {}", beerCategory);

        return beerCategoryRepository
            .findById(beerCategory.getId())
            .map(existingBeerCategory -> {
                if (beerCategory.getCategoryName() != null) {
                    existingBeerCategory.setCategoryName(beerCategory.getCategoryName());
                }

                return existingBeerCategory;
            })
            .map(beerCategoryRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BeerCategory> findAll(Pageable pageable) {
        log.debug("Request to get all BeerCategories");
        return beerCategoryRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BeerCategory> findOne(Long id) {
        log.debug("Request to get BeerCategory : {}", id);
        return beerCategoryRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete BeerCategory : {}", id);
        beerCategoryRepository.deleteById(id);
    }
}
