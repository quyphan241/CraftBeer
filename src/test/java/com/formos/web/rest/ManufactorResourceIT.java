package com.formos.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.formos.IntegrationTest;
import com.formos.domain.Manufactor;
import com.formos.repository.ManufactorRepository;
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
 * Integration tests for the {@link ManufactorResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ManufactorResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/manufactors";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ManufactorRepository manufactorRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restManufactorMockMvc;

    private Manufactor manufactor;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Manufactor createEntity(EntityManager em) {
        Manufactor manufactor = new Manufactor().name(DEFAULT_NAME);
        return manufactor;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Manufactor createUpdatedEntity(EntityManager em) {
        Manufactor manufactor = new Manufactor().name(UPDATED_NAME);
        return manufactor;
    }

    @BeforeEach
    public void initTest() {
        manufactor = createEntity(em);
    }

    @Test
    @Transactional
    void createManufactor() throws Exception {
        int databaseSizeBeforeCreate = manufactorRepository.findAll().size();
        // Create the Manufactor
        restManufactorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(manufactor)))
            .andExpect(status().isCreated());

        // Validate the Manufactor in the database
        List<Manufactor> manufactorList = manufactorRepository.findAll();
        assertThat(manufactorList).hasSize(databaseSizeBeforeCreate + 1);
        Manufactor testManufactor = manufactorList.get(manufactorList.size() - 1);
        assertThat(testManufactor.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createManufactorWithExistingId() throws Exception {
        // Create the Manufactor with an existing ID
        manufactor.setId(1L);

        int databaseSizeBeforeCreate = manufactorRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restManufactorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(manufactor)))
            .andExpect(status().isBadRequest());

        // Validate the Manufactor in the database
        List<Manufactor> manufactorList = manufactorRepository.findAll();
        assertThat(manufactorList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllManufactors() throws Exception {
        // Initialize the database
        manufactorRepository.saveAndFlush(manufactor);

        // Get all the manufactorList
        restManufactorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(manufactor.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getManufactor() throws Exception {
        // Initialize the database
        manufactorRepository.saveAndFlush(manufactor);

        // Get the manufactor
        restManufactorMockMvc
            .perform(get(ENTITY_API_URL_ID, manufactor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(manufactor.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingManufactor() throws Exception {
        // Get the manufactor
        restManufactorMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewManufactor() throws Exception {
        // Initialize the database
        manufactorRepository.saveAndFlush(manufactor);

        int databaseSizeBeforeUpdate = manufactorRepository.findAll().size();

        // Update the manufactor
        Manufactor updatedManufactor = manufactorRepository.findById(manufactor.getId()).get();
        // Disconnect from session so that the updates on updatedManufactor are not directly saved in db
        em.detach(updatedManufactor);
        updatedManufactor.name(UPDATED_NAME);

        restManufactorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedManufactor.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedManufactor))
            )
            .andExpect(status().isOk());

        // Validate the Manufactor in the database
        List<Manufactor> manufactorList = manufactorRepository.findAll();
        assertThat(manufactorList).hasSize(databaseSizeBeforeUpdate);
        Manufactor testManufactor = manufactorList.get(manufactorList.size() - 1);
        assertThat(testManufactor.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingManufactor() throws Exception {
        int databaseSizeBeforeUpdate = manufactorRepository.findAll().size();
        manufactor.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restManufactorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, manufactor.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(manufactor))
            )
            .andExpect(status().isBadRequest());

        // Validate the Manufactor in the database
        List<Manufactor> manufactorList = manufactorRepository.findAll();
        assertThat(manufactorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchManufactor() throws Exception {
        int databaseSizeBeforeUpdate = manufactorRepository.findAll().size();
        manufactor.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restManufactorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(manufactor))
            )
            .andExpect(status().isBadRequest());

        // Validate the Manufactor in the database
        List<Manufactor> manufactorList = manufactorRepository.findAll();
        assertThat(manufactorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamManufactor() throws Exception {
        int databaseSizeBeforeUpdate = manufactorRepository.findAll().size();
        manufactor.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restManufactorMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(manufactor)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Manufactor in the database
        List<Manufactor> manufactorList = manufactorRepository.findAll();
        assertThat(manufactorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateManufactorWithPatch() throws Exception {
        // Initialize the database
        manufactorRepository.saveAndFlush(manufactor);

        int databaseSizeBeforeUpdate = manufactorRepository.findAll().size();

        // Update the manufactor using partial update
        Manufactor partialUpdatedManufactor = new Manufactor();
        partialUpdatedManufactor.setId(manufactor.getId());

        partialUpdatedManufactor.name(UPDATED_NAME);

        restManufactorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedManufactor.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedManufactor))
            )
            .andExpect(status().isOk());

        // Validate the Manufactor in the database
        List<Manufactor> manufactorList = manufactorRepository.findAll();
        assertThat(manufactorList).hasSize(databaseSizeBeforeUpdate);
        Manufactor testManufactor = manufactorList.get(manufactorList.size() - 1);
        assertThat(testManufactor.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void fullUpdateManufactorWithPatch() throws Exception {
        // Initialize the database
        manufactorRepository.saveAndFlush(manufactor);

        int databaseSizeBeforeUpdate = manufactorRepository.findAll().size();

        // Update the manufactor using partial update
        Manufactor partialUpdatedManufactor = new Manufactor();
        partialUpdatedManufactor.setId(manufactor.getId());

        partialUpdatedManufactor.name(UPDATED_NAME);

        restManufactorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedManufactor.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedManufactor))
            )
            .andExpect(status().isOk());

        // Validate the Manufactor in the database
        List<Manufactor> manufactorList = manufactorRepository.findAll();
        assertThat(manufactorList).hasSize(databaseSizeBeforeUpdate);
        Manufactor testManufactor = manufactorList.get(manufactorList.size() - 1);
        assertThat(testManufactor.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingManufactor() throws Exception {
        int databaseSizeBeforeUpdate = manufactorRepository.findAll().size();
        manufactor.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restManufactorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, manufactor.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(manufactor))
            )
            .andExpect(status().isBadRequest());

        // Validate the Manufactor in the database
        List<Manufactor> manufactorList = manufactorRepository.findAll();
        assertThat(manufactorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchManufactor() throws Exception {
        int databaseSizeBeforeUpdate = manufactorRepository.findAll().size();
        manufactor.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restManufactorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(manufactor))
            )
            .andExpect(status().isBadRequest());

        // Validate the Manufactor in the database
        List<Manufactor> manufactorList = manufactorRepository.findAll();
        assertThat(manufactorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamManufactor() throws Exception {
        int databaseSizeBeforeUpdate = manufactorRepository.findAll().size();
        manufactor.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restManufactorMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(manufactor))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Manufactor in the database
        List<Manufactor> manufactorList = manufactorRepository.findAll();
        assertThat(manufactorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteManufactor() throws Exception {
        // Initialize the database
        manufactorRepository.saveAndFlush(manufactor);

        int databaseSizeBeforeDelete = manufactorRepository.findAll().size();

        // Delete the manufactor
        restManufactorMockMvc
            .perform(delete(ENTITY_API_URL_ID, manufactor.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Manufactor> manufactorList = manufactorRepository.findAll();
        assertThat(manufactorList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
