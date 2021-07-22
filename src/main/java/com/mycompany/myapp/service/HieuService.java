package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.HieuDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.Hieu}.
 */
public interface HieuService {
    /**
     * Save a hieu.
     *
     * @param hieuDTO the entity to save.
     * @return the persisted entity.
     */
    HieuDTO save(HieuDTO hieuDTO);

    /**
     * Partially updates a hieu.
     *
     * @param hieuDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<HieuDTO> partialUpdate(HieuDTO hieuDTO);

    /**
     * Get all the hieus.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<HieuDTO> findAll(Pageable pageable);

    /**
     * Get the "id" hieu.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<HieuDTO> findOne(Long id);

    /**
     * Delete the "id" hieu.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
