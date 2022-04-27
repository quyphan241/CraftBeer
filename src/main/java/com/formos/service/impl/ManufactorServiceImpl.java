package com.formos.service.impl;

import com.formos.domain.Manufactor;
import com.formos.repository.ManufactorRepository;
import com.formos.service.ManufactorService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Manufactor}.
 */
@Service
@Transactional
public class ManufactorServiceImpl implements ManufactorService {

    private final Logger log = LoggerFactory.getLogger(ManufactorServiceImpl.class);

    private final ManufactorRepository manufactorRepository;

    public ManufactorServiceImpl(ManufactorRepository manufactorRepository) {
        this.manufactorRepository = manufactorRepository;
    }

    @Override
    public Manufactor save(Manufactor manufactor) {
        log.debug("Request to save Manufactor : {}", manufactor);
        return manufactorRepository.save(manufactor);
    }

    @Override
    public Manufactor update(Manufactor manufactor) {
        log.debug("Request to save Manufactor : {}", manufactor);
        return manufactorRepository.save(manufactor);
    }

    @Override
    public Optional<Manufactor> partialUpdate(Manufactor manufactor) {
        log.debug("Request to partially update Manufactor : {}", manufactor);

        return manufactorRepository
            .findById(manufactor.getId())
            .map(existingManufactor -> {
                if (manufactor.getName() != null) {
                    existingManufactor.setName(manufactor.getName());
                }

                return existingManufactor;
            })
            .map(manufactorRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Manufactor> findAll() {
        log.debug("Request to get all Manufactors");
        return manufactorRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Manufactor> findOne(Long id) {
        log.debug("Request to get Manufactor : {}", id);
        return manufactorRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Manufactor : {}", id);
        manufactorRepository.deleteById(id);
    }
}
