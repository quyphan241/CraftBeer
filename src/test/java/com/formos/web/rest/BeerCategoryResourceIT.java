package com.formos.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.formos.IntegrationTest;
import com.formos.domain.BeerCategory;
import com.formos.repository.BeerCategoryRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link BeerCategoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BeerCategoryResourceIT {

    private static final String DEFAULT_CATEGORY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CATEGORY_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/beer-categories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BeerCategoryRepository beerCategoryRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBeerCategoryMockMvc;

    private BeerCategory beerCategory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BeerCategory createEntity(EntityManager em) {
        BeerCategory beerCategory = new BeerCategory().categoryName(DEFAULT_CATEGORY_NAME);
        return beerCategory;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BeerCategory createUpdatedEntity(EntityManager em) {
        BeerCategory beerCategory = new BeerCategory().categoryName(UPDATED_CATEGORY_NAME);
        return beerCategory;
    }

    @BeforeEach
    public void initTest() {
        beerCategory = createEntity(em);
    }

    @Test
    @Transactional
    void createBeerCategory() throws Exception {
        int databaseSizeBeforeCreate = beerCategoryRepository.findAll().size();
        // Create the BeerCategory
        restBeerCategoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(beerCategory)))
            .andExpect(status().isCreated());

        // Validate the BeerCategory in the database
        List<BeerCategory> beerCategoryList = beerCategoryRepository.findAll();
        assertThat(beerCategoryList).hasSize(databaseSizeBeforeCreate + 1);
        BeerCategory testBeerCategory = beerCategoryList.get(beerCategoryList.size() - 1);
        assertThat(testBeerCategory.getCategoryName()).isEqualTo(DEFAULT_CATEGORY_NAME);
    }

    @Test
    @Transactional
    void createBeerCategoryWithExistingId() throws Exception {
        // Create the BeerCategory with an existing ID
        beerCategory.setId(1L);

        int databaseSizeBeforeCreate = beerCategoryRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBeerCategoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(beerCategory)))
            .andExpect(status().isBadRequest());

        // Validate the BeerCategory in the database
        List<BeerCategory> beerCategoryList = beerCategoryRepository.findAll();
        assertThat(beerCategoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllBeerCategories() throws Exception {
        // Initialize the database
        beerCategoryRepository.saveAndFlush(beerCategory);

        // Get all the beerCategoryList
        restBeerCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(beerCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].categoryName").value(hasItem(DEFAULT_CATEGORY_NAME)));
    }

    @Test
    @Transactional
    void getBeerCategory() throws Exception {
        // Initialize the database
        beerCategoryRepository.saveAndFlush(beerCategory);

        // Get the beerCategory
        restBeerCategoryMockMvc
            .perform(get(ENTITY_API_URL_ID, beerCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(beerCategory.getId().intValue()))
            .andExpect(jsonPath("$.categoryName").value(DEFAULT_CATEGORY_NAME));
    }

    @Test
    @Transactional
    void getNonExistingBeerCategory() throws Exception {
        // Get the beerCategory
        restBeerCategoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewBeerCategory() throws Exception {
        // Initialize the database
        beerCategoryRepository.saveAndFlush(beerCategory);

        int databaseSizeBeforeUpdate = beerCategoryRepository.findAll().size();

        // Update the beerCategory
        BeerCategory updatedBeerCategory = beerCategoryRepository.findById(beerCategory.getId()).get();
        // Disconnect from session so that the updates on updatedBeerCategory are not directly saved in db
        em.detach(updatedBeerCategory);
        updatedBeerCategory.categoryName(UPDATED_CATEGORY_NAME);

        restBeerCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedBeerCategory.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedBeerCategory))
            )
            .andExpect(status().isOk());

        // Validate the BeerCategory in the database
        List<BeerCategory> beerCategoryList = beerCategoryRepository.findAll();
        assertThat(beerCategoryList).hasSize(databaseSizeBeforeUpdate);
        BeerCategory testBeerCategory = beerCategoryList.get(beerCategoryList.size() - 1);
        assertThat(testBeerCategory.getCategoryName()).isEqualTo(UPDATED_CATEGORY_NAME);
    }

    @Test
    @Transactional
    void putNonExistingBeerCategory() throws Exception {
        int databaseSizeBeforeUpdate = beerCategoryRepository.findAll().size();
        beerCategory.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBeerCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, beerCategory.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(beerCategory))
            )
            .andExpect(status().isBadRequest());

        // Validate the BeerCategory in the database
        List<BeerCategory> beerCategoryList = beerCategoryRepository.findAll();
        assertThat(beerCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBeerCategory() throws Exception {
        int databaseSizeBeforeUpdate = beerCategoryRepository.findAll().size();
        beerCategory.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBeerCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(beerCategory))
            )
            .andExpect(status().isBadRequest());

        // Validate the BeerCategory in the database
        List<BeerCategory> beerCategoryList = beerCategoryRepository.findAll();
        assertThat(beerCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBeerCategory() throws Exception {
        int databaseSizeBeforeUpdate = beerCategoryRepository.findAll().size();
        beerCategory.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBeerCategoryMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(beerCategory)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BeerCategory in the database
        List<BeerCategory> beerCategoryList = beerCategoryRepository.findAll();
        assertThat(beerCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBeerCategoryWithPatch() throws Exception {
        // Initialize the database
        beerCategoryRepository.saveAndFlush(beerCategory);

        int databaseSizeBeforeUpdate = beerCategoryRepository.findAll().size();

        // Update the beerCategory using partial update
        BeerCategory partialUpdatedBeerCategory = new BeerCategory();
        partialUpdatedBeerCategory.setId(beerCategory.getId());

        partialUpdatedBeerCategory.categoryName(UPDATED_CATEGORY_NAME);

        restBeerCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBeerCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBeerCategory))
            )
            .andExpect(status().isOk());

        // Validate the BeerCategory in the database
        List<BeerCategory> beerCategoryList = beerCategoryRepository.findAll();
        assertThat(beerCategoryList).hasSize(databaseSizeBeforeUpdate);
        BeerCategory testBeerCategory = beerCategoryList.get(beerCategoryList.size() - 1);
        assertThat(testBeerCategory.getCategoryName()).isEqualTo(UPDATED_CATEGORY_NAME);
    }

    @Test
    @Transactional
    void fullUpdateBeerCategoryWithPatch() throws Exception {
        // Initialize the database
        beerCategoryRepository.saveAndFlush(beerCategory);

        int databaseSizeBeforeUpdate = beerCategoryRepository.findAll().size();

        // Update the beerCategory using partial update
        BeerCategory partialUpdatedBeerCategory = new BeerCategory();
        partialUpdatedBeerCategory.setId(beerCategory.getId());

        partialUpdatedBeerCategory.categoryName(UPDATED_CATEGORY_NAME);

        restBeerCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBeerCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBeerCategory))
            )
            .andExpect(status().isOk());

        // Validate the BeerCategory in the database
        List<BeerCategory> beerCategoryList = beerCategoryRepository.findAll();
        assertThat(beerCategoryList).hasSize(databaseSizeBeforeUpdate);
        BeerCategory testBeerCategory = beerCategoryList.get(beerCategoryList.size() - 1);
        assertThat(testBeerCategory.getCategoryName()).isEqualTo(UPDATED_CATEGORY_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingBeerCategory() throws Exception {
        int databaseSizeBeforeUpdate = beerCategoryRepository.findAll().size();
        beerCategory.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBeerCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, beerCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(beerCategory))
            )
            .andExpect(status().isBadRequest());

        // Validate the BeerCategory in the database
        List<BeerCategory> beerCategoryList = beerCategoryRepository.findAll();
        assertThat(beerCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBeerCategory() throws Exception {
        int databaseSizeBeforeUpdate = beerCategoryRepository.findAll().size();
        beerCategory.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBeerCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(beerCategory))
            )
            .andExpect(status().isBadRequest());

        // Validate the BeerCategory in the database
        List<BeerCategory> beerCategoryList = beerCategoryRepository.findAll();
        assertThat(beerCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBeerCategory() throws Exception {
        int databaseSizeBeforeUpdate = beerCategoryRepository.findAll().size();
        beerCategory.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBeerCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(beerCategory))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the BeerCategory in the database
        List<BeerCategory> beerCategoryList = beerCategoryRepository.findAll();
        assertThat(beerCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBeerCategory() throws Exception {
        // Initialize the database
        beerCategoryRepository.saveAndFlush(beerCategory);

        int databaseSizeBeforeDelete = beerCategoryRepository.findAll().size();

        // Delete the beerCategory
        restBeerCategoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, beerCategory.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<BeerCategory> beerCategoryList = beerCategoryRepository.findAll();
        assertThat(beerCategoryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
