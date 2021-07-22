package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.HieuRepository;
import com.mycompany.myapp.service.HieuService;
import com.mycompany.myapp.service.dto.HieuDTO;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Hieu}.
 */
@RestController
@RequestMapping("/api")
public class HieuResource {

    private final Logger log = LoggerFactory.getLogger(HieuResource.class);

    private static final String ENTITY_NAME = "hieu";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HieuService hieuService;

    private final HieuRepository hieuRepository;

    public HieuResource(HieuService hieuService, HieuRepository hieuRepository) {
        this.hieuService = hieuService;
        this.hieuRepository = hieuRepository;
    }

    /**
     * {@code POST  /hieus} : Create a new hieu.
     *
     * @param hieuDTO the hieuDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new hieuDTO, or with status {@code 400 (Bad Request)} if the hieu has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/hieus")
    public ResponseEntity<HieuDTO> createHieu(@RequestBody HieuDTO hieuDTO) throws URISyntaxException {
        log.debug("REST request to save Hieu : {}", hieuDTO);
        if (hieuDTO.getId() != null) {
            throw new BadRequestAlertException("A new hieu cannot already have an ID", ENTITY_NAME, "idexists");
        }
        HieuDTO result = hieuService.save(hieuDTO);
        return ResponseEntity
            .created(new URI("/api/hieus/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /hieus/:id} : Updates an existing hieu.
     *
     * @param id the id of the hieuDTO to save.
     * @param hieuDTO the hieuDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated hieuDTO,
     * or with status {@code 400 (Bad Request)} if the hieuDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the hieuDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/hieus/{id}")
    public ResponseEntity<HieuDTO> updateHieu(@PathVariable(value = "id", required = false) final Long id, @RequestBody HieuDTO hieuDTO)
        throws URISyntaxException {
        log.debug("REST request to update Hieu : {}, {}", id, hieuDTO);
        if (hieuDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, hieuDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!hieuRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        HieuDTO result = hieuService.save(hieuDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, hieuDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /hieus/:id} : Partial updates given fields of an existing hieu, field will ignore if it is null
     *
     * @param id the id of the hieuDTO to save.
     * @param hieuDTO the hieuDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated hieuDTO,
     * or with status {@code 400 (Bad Request)} if the hieuDTO is not valid,
     * or with status {@code 404 (Not Found)} if the hieuDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the hieuDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/hieus/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<HieuDTO> partialUpdateHieu(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody HieuDTO hieuDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Hieu partially : {}, {}", id, hieuDTO);
        if (hieuDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, hieuDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!hieuRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<HieuDTO> result = hieuService.partialUpdate(hieuDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, hieuDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /hieus} : get all the hieus.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of hieus in body.
     */
    @GetMapping("/hieus")
    public ResponseEntity<List<HieuDTO>> getAllHieus(Pageable pageable) {
        log.debug("REST request to get a page of Hieus");
        Page<HieuDTO> page = hieuService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /hieus/:id} : get the "id" hieu.
     *
     * @param id the id of the hieuDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the hieuDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/hieus/{id}")
    public ResponseEntity<HieuDTO> getHieu(@PathVariable Long id) {
        log.debug("REST request to get Hieu : {}", id);
        Optional<HieuDTO> hieuDTO = hieuService.findOne(id);
        return ResponseUtil.wrapOrNotFound(hieuDTO);
    }

    /**
     * {@code DELETE  /hieus/:id} : delete the "id" hieu.
     *
     * @param id the id of the hieuDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/hieus/{id}")
    public ResponseEntity<Void> deleteHieu(@PathVariable Long id) {
        log.debug("REST request to delete Hieu : {}", id);
        hieuService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
