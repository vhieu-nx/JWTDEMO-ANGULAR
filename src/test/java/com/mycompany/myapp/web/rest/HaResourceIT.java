package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Ha;
import com.mycompany.myapp.repository.HaRepository;
import com.mycompany.myapp.service.dto.HaDTO;
import com.mycompany.myapp.service.mapper.HaMapper;
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
 * Integration tests for the {@link HaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class HaResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/has";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private HaRepository haRepository;

    @Autowired
    private HaMapper haMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHaMockMvc;

    private Ha ha;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ha createEntity(EntityManager em) {
        Ha ha = new Ha().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION);
        return ha;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ha createUpdatedEntity(EntityManager em) {
        Ha ha = new Ha().name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
        return ha;
    }

    @BeforeEach
    public void initTest() {
        ha = createEntity(em);
    }

    @Test
    @Transactional
    void createHa() throws Exception {
        int databaseSizeBeforeCreate = haRepository.findAll().size();
        // Create the Ha
        HaDTO haDTO = haMapper.toDto(ha);
        restHaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(haDTO)))
            .andExpect(status().isCreated());

        // Validate the Ha in the database
        List<Ha> haList = haRepository.findAll();
        assertThat(haList).hasSize(databaseSizeBeforeCreate + 1);
        Ha testHa = haList.get(haList.size() - 1);
        assertThat(testHa.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testHa.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createHaWithExistingId() throws Exception {
        // Create the Ha with an existing ID
        ha.setId(1L);
        HaDTO haDTO = haMapper.toDto(ha);

        int databaseSizeBeforeCreate = haRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restHaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(haDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Ha in the database
        List<Ha> haList = haRepository.findAll();
        assertThat(haList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllHas() throws Exception {
        // Initialize the database
        haRepository.saveAndFlush(ha);

        // Get all the haList
        restHaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ha.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getHa() throws Exception {
        // Initialize the database
        haRepository.saveAndFlush(ha);

        // Get the ha
        restHaMockMvc
            .perform(get(ENTITY_API_URL_ID, ha.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ha.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingHa() throws Exception {
        // Get the ha
        restHaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewHa() throws Exception {
        // Initialize the database
        haRepository.saveAndFlush(ha);

        int databaseSizeBeforeUpdate = haRepository.findAll().size();

        // Update the ha
        Ha updatedHa = haRepository.findById(ha.getId()).get();
        // Disconnect from session so that the updates on updatedHa are not directly saved in db
        em.detach(updatedHa);
        updatedHa.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
        HaDTO haDTO = haMapper.toDto(updatedHa);

        restHaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, haDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(haDTO))
            )
            .andExpect(status().isOk());

        // Validate the Ha in the database
        List<Ha> haList = haRepository.findAll();
        assertThat(haList).hasSize(databaseSizeBeforeUpdate);
        Ha testHa = haList.get(haList.size() - 1);
        assertThat(testHa.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testHa.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingHa() throws Exception {
        int databaseSizeBeforeUpdate = haRepository.findAll().size();
        ha.setId(count.incrementAndGet());

        // Create the Ha
        HaDTO haDTO = haMapper.toDto(ha);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, haDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(haDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ha in the database
        List<Ha> haList = haRepository.findAll();
        assertThat(haList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchHa() throws Exception {
        int databaseSizeBeforeUpdate = haRepository.findAll().size();
        ha.setId(count.incrementAndGet());

        // Create the Ha
        HaDTO haDTO = haMapper.toDto(ha);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(haDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ha in the database
        List<Ha> haList = haRepository.findAll();
        assertThat(haList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamHa() throws Exception {
        int databaseSizeBeforeUpdate = haRepository.findAll().size();
        ha.setId(count.incrementAndGet());

        // Create the Ha
        HaDTO haDTO = haMapper.toDto(ha);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(haDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Ha in the database
        List<Ha> haList = haRepository.findAll();
        assertThat(haList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateHaWithPatch() throws Exception {
        // Initialize the database
        haRepository.saveAndFlush(ha);

        int databaseSizeBeforeUpdate = haRepository.findAll().size();

        // Update the ha using partial update
        Ha partialUpdatedHa = new Ha();
        partialUpdatedHa.setId(ha.getId());

        restHaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHa.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHa))
            )
            .andExpect(status().isOk());

        // Validate the Ha in the database
        List<Ha> haList = haRepository.findAll();
        assertThat(haList).hasSize(databaseSizeBeforeUpdate);
        Ha testHa = haList.get(haList.size() - 1);
        assertThat(testHa.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testHa.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateHaWithPatch() throws Exception {
        // Initialize the database
        haRepository.saveAndFlush(ha);

        int databaseSizeBeforeUpdate = haRepository.findAll().size();

        // Update the ha using partial update
        Ha partialUpdatedHa = new Ha();
        partialUpdatedHa.setId(ha.getId());

        partialUpdatedHa.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restHaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHa.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHa))
            )
            .andExpect(status().isOk());

        // Validate the Ha in the database
        List<Ha> haList = haRepository.findAll();
        assertThat(haList).hasSize(databaseSizeBeforeUpdate);
        Ha testHa = haList.get(haList.size() - 1);
        assertThat(testHa.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testHa.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingHa() throws Exception {
        int databaseSizeBeforeUpdate = haRepository.findAll().size();
        ha.setId(count.incrementAndGet());

        // Create the Ha
        HaDTO haDTO = haMapper.toDto(ha);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, haDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(haDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ha in the database
        List<Ha> haList = haRepository.findAll();
        assertThat(haList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchHa() throws Exception {
        int databaseSizeBeforeUpdate = haRepository.findAll().size();
        ha.setId(count.incrementAndGet());

        // Create the Ha
        HaDTO haDTO = haMapper.toDto(ha);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(haDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ha in the database
        List<Ha> haList = haRepository.findAll();
        assertThat(haList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamHa() throws Exception {
        int databaseSizeBeforeUpdate = haRepository.findAll().size();
        ha.setId(count.incrementAndGet());

        // Create the Ha
        HaDTO haDTO = haMapper.toDto(ha);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(haDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Ha in the database
        List<Ha> haList = haRepository.findAll();
        assertThat(haList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteHa() throws Exception {
        // Initialize the database
        haRepository.saveAndFlush(ha);

        int databaseSizeBeforeDelete = haRepository.findAll().size();

        // Delete the ha
        restHaMockMvc.perform(delete(ENTITY_API_URL_ID, ha.getId()).accept(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Ha> haList = haRepository.findAll();
        assertThat(haList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
