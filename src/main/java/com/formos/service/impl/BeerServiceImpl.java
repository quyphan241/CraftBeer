package com.formos.service.impl;

import com.formos.domain.Beer;
import com.formos.repository.BeerRepository;
import com.formos.service.BeerService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Beer}.
 */
@Service
@Transactional
public class BeerServiceImpl implements BeerService {

    private final Logger log = LoggerFactory.getLogger(BeerServiceImpl.class);

    private final BeerRepository beerRepository;

    public BeerServiceImpl(BeerRepository beerRepository) {
        this.beerRepository = beerRepository;
    }

    @Override
    public Beer save(Beer beer) {
        log.debug("Request to save Beer : {}", beer);
        return beerRepository.save(beer);
    }

    @Override
    public Beer update(Beer beer) {
        log.debug("Request to save Beer : {}", beer);
        return beerRepository.save(beer);
    }

    @Override
    public Optional<Beer> partialUpdate(Beer beer) {
        log.debug("Request to partially update Beer : {}", beer);

        return beerRepository
            .findById(beer.getId())
            .map(existingBeer -> {
                if (beer.getName() != null) {
                    existingBeer.setName(beer.getName());
                }
                if (beer.getCountry() != null) {
                    existingBeer.setCountry(beer.getCountry());
                }
                if (beer.getPrice() != null) {
                    existingBeer.setPrice(beer.getPrice());
                }
                if (beer.getDescription() != null) {
                    existingBeer.setDescription(beer.getDescription());
                }

                return existingBeer;
            })
            .map(beerRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Beer> findAll(Pageable pageable) {
        log.debug("Request to get all Beers");
        return beerRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Beer> findOne(Long id) {
        log.debug("Request to get Beer : {}", id);
        return beerRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Beer : {}", id);
        beerRepository.deleteById(id);
    }
}
