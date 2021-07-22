package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.HaDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.Ha}.
 */
public interface HaService {
    /**
     * Save a ha.
     *
     * @param haDTO the entity to save.
     * @return the persisted entity.
     */
    HaDTO save(HaDTO haDTO);

    /**
     * Partially updates a ha.
     *
     * @param haDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<HaDTO> partialUpdate(HaDTO haDTO);

    /**
     * Get all the has.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<HaDTO> findAll(Pageable pageable);

    /**
     * Get the "id" ha.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<HaDTO> findOne(Long id);

    /**
     * Delete the "id" ha.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
