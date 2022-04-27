package com.formos.web.rest;

import com.formos.domain.Manufactor;
import com.formos.repository.ManufactorRepository;
import com.formos.service.ManufactorService;
import com.formos.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.formos.domain.Manufactor}.
 */
@RestController
@RequestMapping("/api")
public class ManufactorResource {

    private final Logger log = LoggerFactory.getLogger(ManufactorResource.class);

    private static final String ENTITY_NAME = "manufactor";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ManufactorService manufactorService;

    private final ManufactorRepository manufactorRepository;

    public ManufactorResource(ManufactorService manufactorService, ManufactorRepository manufactorRepository) {
        this.manufactorService = manufactorService;
        this.manufactorRepository = manufactorRepository;
    }

    /**
     * {@code POST  /manufactors} : Create a new manufactor.
     *
     * @param manufactor the manufactor to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new manufactor, or with status {@code 400 (Bad Request)} if the manufactor has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/manufactors")
    public ResponseEntity<Manufactor> createManufactor(@RequestBody Manufactor manufactor) throws URISyntaxException {
        log.debug("REST request to save Manufactor : {}", manufactor);
        if (manufactor.getId() != null) {
            throw new BadRequestAlertException("A new manufactor cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Manufactor result = manufactorService.save(manufactor);
        return ResponseEntity
            .created(new URI("/api/manufactors/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /manufactors/:id} : Updates an existing manufactor.
     *
     * @param id the id of the manufactor to save.
     * @param manufactor the manufactor to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated manufactor,
     * or with status {@code 400 (Bad Request)} if the manufactor is not valid,
     * or with status {@code 500 (Internal Server Error)} if the manufactor couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/manufactors/{id}")
    public ResponseEntity<Manufactor> updateManufactor(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Manufactor manufactor
    ) throws URISyntaxException {
        log.debug("REST request to update Manufactor : {}, {}", id, manufactor);
        if (manufactor.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, manufactor.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!manufactorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Manufactor result = manufactorService.update(manufactor);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, manufactor.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /manufactors/:id} : Partial updates given fields of an existing manufactor, field will ignore if it is null
     *
     * @param id the id of the manufactor to save.
     * @param manufactor the manufactor to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated manufactor,
     * or with status {@code 400 (Bad Request)} if the manufactor is not valid,
     * or with status {@code 404 (Not Found)} if the manufactor is not found,
     * or with status {@code 500 (Internal Server Error)} if the manufactor couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/manufactors/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Manufactor> partialUpdateManufactor(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Manufactor manufactor
    ) throws URISyntaxException {
        log.debug("REST request to partial update Manufactor partially : {}, {}", id, manufactor);
        if (manufactor.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, manufactor.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!manufactorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Manufactor> result = manufactorService.partialUpdate(manufactor);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, manufactor.getId().toString())
        );
    }

    /**
     * {@code GET  /manufactors} : get all the manufactors.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of manufactors in body.
     */
    @GetMapping("/manufactors")
    public List<Manufactor> getAllManufactors() {
        log.debug("REST request to get all Manufactors");
        return manufactorService.findAll();
    }

    /**
     * {@code GET  /manufactors/:id} : get the "id" manufactor.
     *
     * @param id the id of the manufactor to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the manufactor, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/manufactors/{id}")
    public ResponseEntity<Manufactor> getManufactor(@PathVariable Long id) {
        log.debug("REST request to get Manufactor : {}", id);
        Optional<Manufactor> manufactor = manufactorService.findOne(id);
        return ResponseUtil.wrapOrNotFound(manufactor);
    }

    /**
     * {@code DELETE  /manufactors/:id} : delete the "id" manufactor.
     *
     * @param id the id of the manufactor to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/manufactors/{id}")
    public ResponseEntity<Void> deleteManufactor(@PathVariable Long id) {
        log.debug("REST request to delete Manufactor : {}", id);
        manufactorService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
