package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Hieu;
import com.mycompany.myapp.repository.HieuRepository;
import com.mycompany.myapp.service.dto.HieuDTO;
import com.mycompany.myapp.service.mapper.HieuMapper;
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
 * Integration tests for the {@link HieuResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class HieuResourceIT {

    private static final String ENTITY_API_URL = "/api/hieus";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private HieuRepository hieuRepository;

    @Autowired
    private HieuMapper hieuMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHieuMockMvc;

    private Hieu hieu;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Hieu createEntity(EntityManager em) {
        Hieu hieu = new Hieu();
        return hieu;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Hieu createUpdatedEntity(EntityManager em) {
        Hieu hieu = new Hieu();
        return hieu;
    }

    @BeforeEach
    public void initTest() {
        hieu = createEntity(em);
    }

    @Test
    @Transactional
    void createHieu() throws Exception {
        int databaseSizeBeforeCreate = hieuRepository.findAll().size();
        // Create the Hieu
        HieuDTO hieuDTO = hieuMapper.toDto(hieu);
        restHieuMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(hieuDTO)))
            .andExpect(status().isCreated());

        // Validate the Hieu in the database
        List<Hieu> hieuList = hieuRepository.findAll();
        assertThat(hieuList).hasSize(databaseSizeBeforeCreate + 1);
        Hieu testHieu = hieuList.get(hieuList.size() - 1);
    }

    @Test
    @Transactional
    void createHieuWithExistingId() throws Exception {
        // Create the Hieu with an existing ID
        hieu.setId(1L);
        HieuDTO hieuDTO = hieuMapper.toDto(hieu);

        int databaseSizeBeforeCreate = hieuRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restHieuMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(hieuDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Hieu in the database
        List<Hieu> hieuList = hieuRepository.findAll();
        assertThat(hieuList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllHieus() throws Exception {
        // Initialize the database
        hieuRepository.saveAndFlush(hieu);

        // Get all the hieuList
        restHieuMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(hieu.getId().intValue())));
    }

    @Test
    @Transactional
    void getHieu() throws Exception {
        // Initialize the database
        hieuRepository.saveAndFlush(hieu);

        // Get the hieu
        restHieuMockMvc
            .perform(get(ENTITY_API_URL_ID, hieu.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(hieu.getId().intValue()));
    }

    @Test
    @Transactional
    void getNonExistingHieu() throws Exception {
        // Get the hieu
        restHieuMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewHieu() throws Exception {
        // Initialize the database
        hieuRepository.saveAndFlush(hieu);

        int databaseSizeBeforeUpdate = hieuRepository.findAll().size();

        // Update the hieu
        Hieu updatedHieu = hieuRepository.findById(hieu.getId()).get();
        // Disconnect from session so that the updates on updatedHieu are not directly saved in db
        em.detach(updatedHieu);
        HieuDTO hieuDTO = hieuMapper.toDto(updatedHieu);

        restHieuMockMvc
            .perform(
                put(ENTITY_API_URL_ID, hieuDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(hieuDTO))
            )
            .andExpect(status().isOk());

        // Validate the Hieu in the database
        List<Hieu> hieuList = hieuRepository.findAll();
        assertThat(hieuList).hasSize(databaseSizeBeforeUpdate);
        Hieu testHieu = hieuList.get(hieuList.size() - 1);
    }

    @Test
    @Transactional
    void putNonExistingHieu() throws Exception {
        int databaseSizeBeforeUpdate = hieuRepository.findAll().size();
        hieu.setId(count.incrementAndGet());

        // Create the Hieu
        HieuDTO hieuDTO = hieuMapper.toDto(hieu);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHieuMockMvc
            .perform(
                put(ENTITY_API_URL_ID, hieuDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(hieuDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Hieu in the database
        List<Hieu> hieuList = hieuRepository.findAll();
        assertThat(hieuList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchHieu() throws Exception {
        int databaseSizeBeforeUpdate = hieuRepository.findAll().size();
        hieu.setId(count.incrementAndGet());

        // Create the Hieu
        HieuDTO hieuDTO = hieuMapper.toDto(hieu);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHieuMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(hieuDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Hieu in the database
        List<Hieu> hieuList = hieuRepository.findAll();
        assertThat(hieuList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamHieu() throws Exception {
        int databaseSizeBeforeUpdate = hieuRepository.findAll().size();
        hieu.setId(count.incrementAndGet());

        // Create the Hieu
        HieuDTO hieuDTO = hieuMapper.toDto(hieu);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHieuMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(hieuDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Hieu in the database
        List<Hieu> hieuList = hieuRepository.findAll();
        assertThat(hieuList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateHieuWithPatch() throws Exception {
        // Initialize the database
        hieuRepository.saveAndFlush(hieu);

        int databaseSizeBeforeUpdate = hieuRepository.findAll().size();

        // Update the hieu using partial update
        Hieu partialUpdatedHieu = new Hieu();
        partialUpdatedHieu.setId(hieu.getId());

        restHieuMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHieu.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHieu))
            )
            .andExpect(status().isOk());

        // Validate the Hieu in the database
        List<Hieu> hieuList = hieuRepository.findAll();
        assertThat(hieuList).hasSize(databaseSizeBeforeUpdate);
        Hieu testHieu = hieuList.get(hieuList.size() - 1);
    }

    @Test
    @Transactional
    void fullUpdateHieuWithPatch() throws Exception {
        // Initialize the database
        hieuRepository.saveAndFlush(hieu);

        int databaseSizeBeforeUpdate = hieuRepository.findAll().size();

        // Update the hieu using partial update
        Hieu partialUpdatedHieu = new Hieu();
        partialUpdatedHieu.setId(hieu.getId());

        restHieuMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHieu.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHieu))
            )
            .andExpect(status().isOk());

        // Validate the Hieu in the database
        List<Hieu> hieuList = hieuRepository.findAll();
        assertThat(hieuList).hasSize(databaseSizeBeforeUpdate);
        Hieu testHieu = hieuList.get(hieuList.size() - 1);
    }

    @Test
    @Transactional
    void patchNonExistingHieu() throws Exception {
        int databaseSizeBeforeUpdate = hieuRepository.findAll().size();
        hieu.setId(count.incrementAndGet());

        // Create the Hieu
        HieuDTO hieuDTO = hieuMapper.toDto(hieu);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHieuMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, hieuDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(hieuDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Hieu in the database
        List<Hieu> hieuList = hieuRepository.findAll();
        assertThat(hieuList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchHieu() throws Exception {
        int databaseSizeBeforeUpdate = hieuRepository.findAll().size();
        hieu.setId(count.incrementAndGet());

        // Create the Hieu
        HieuDTO hieuDTO = hieuMapper.toDto(hieu);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHieuMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(hieuDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Hieu in the database
        List<Hieu> hieuList = hieuRepository.findAll();
        assertThat(hieuList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamHieu() throws Exception {
        int databaseSizeBeforeUpdate = hieuRepository.findAll().size();
        hieu.setId(count.incrementAndGet());

        // Create the Hieu
        HieuDTO hieuDTO = hieuMapper.toDto(hieu);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHieuMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(hieuDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Hieu in the database
        List<Hieu> hieuList = hieuRepository.findAll();
        assertThat(hieuList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteHieu() throws Exception {
        // Initialize the database
        hieuRepository.saveAndFlush(hieu);

        int databaseSizeBeforeDelete = hieuRepository.findAll().size();

        // Delete the hieu
        restHieuMockMvc
            .perform(delete(ENTITY_API_URL_ID, hieu.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Hieu> hieuList = hieuRepository.findAll();
        assertThat(hieuList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
