package com.formos.web.rest;

import com.formos.domain.BeerCategory;
import com.formos.repository.BeerCategoryRepository;
import com.formos.service.BeerCategoryService;
import com.formos.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.formos.domain.BeerCategory}.
 */
@RestController
@RequestMapping("/api")
public class BeerCategoryResource {

    private final Logger log = LoggerFactory.getLogger(BeerCategoryResource.class);

    private static final String ENTITY_NAME = "beerCategory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BeerCategoryService beerCategoryService;

    private final BeerCategoryRepository beerCategoryRepository;

    public BeerCategoryResource(BeerCategoryService beerCategoryService, BeerCategoryRepository beerCategoryRepository) {
        this.beerCategoryService = beerCategoryService;
        this.beerCategoryRepository = beerCategoryRepository;
    }

    /**
     * {@code POST  /beer-categories} : Create a new beerCategory.
     *
     * @param beerCategory the beerCategory to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new beerCategory, or with status {@code 400 (Bad Request)} if the beerCategory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/beer-categories")
    public ResponseEntity<BeerCategory> createBeerCategory(@RequestBody BeerCategory beerCategory) throws URISyntaxException {
        log.debug("REST request to save BeerCategory : {}", beerCategory);
        if (beerCategory.getId() != null) {
            throw new BadRequestAlertException("A new beerCategory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BeerCategory result = beerCategoryService.save(beerCategory);
        return ResponseEntity
            .created(new URI("/api/beer-categories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /beer-categories/:id} : Updates an existing beerCategory.
     *
     * @param id the id of the beerCategory to save.
     * @param beerCategory the beerCategory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated beerCategory,
     * or with status {@code 400 (Bad Request)} if the beerCategory is not valid,
     * or with status {@code 500 (Internal Server Error)} if the beerCategory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/beer-categories/{id}")
    public ResponseEntity<BeerCategory> updateBeerCategory(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody BeerCategory beerCategory
    ) throws URISyntaxException {
        log.debug("REST request to update BeerCategory : {}, {}", id, beerCategory);
        if (beerCategory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, beerCategory.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!beerCategoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        BeerCategory result = beerCategoryService.update(beerCategory);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, beerCategory.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /beer-categories/:id} : Partial updates given fields of an existing beerCategory, field will ignore if it is null
     *
     * @param id the id of the beerCategory to save.
     * @param beerCategory the beerCategory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated beerCategory,
     * or with status {@code 400 (Bad Request)} if the beerCategory is not valid,
     * or with status {@code 404 (Not Found)} if the beerCategory is not found,
     * or with status {@code 500 (Internal Server Error)} if the beerCategory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/beer-categories/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BeerCategory> partialUpdateBeerCategory(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody BeerCategory beerCategory
    ) throws URISyntaxException {
        log.debug("REST request to partial update BeerCategory partially : {}, {}", id, beerCategory);
        if (beerCategory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, beerCategory.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!beerCategoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BeerCategory> result = beerCategoryService.partialUpdate(beerCategory);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, beerCategory.getId().toString())
        );
    }

    /**
     * {@code GET  /beer-categories} : get all the beerCategories.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of beerCategories in body.
     */
    @GetMapping("/beer-categories")
    public ResponseEntity<List<BeerCategory>> getAllBeerCategories(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of BeerCategories");
        Page<BeerCategory> page = beerCategoryService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /beer-categories/:id} : get the "id" beerCategory.
     *
     * @param id the id of the beerCategory to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the beerCategory, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/beer-categories/{id}")
    public ResponseEntity<BeerCategory> getBeerCategory(@PathVariable Long id) {
        log.debug("REST request to get BeerCategory : {}", id);
        Optional<BeerCategory> beerCategory = beerCategoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(beerCategory);
    }

    /**
     * {@code DELETE  /beer-categories/:id} : delete the "id" beerCategory.
     *
     * @param id the id of the beerCategory to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/beer-categories/{id}")
    public ResponseEntity<Void> deleteBeerCategory(@PathVariable Long id) {
        log.debug("REST request to delete BeerCategory : {}", id);
        beerCategoryService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
