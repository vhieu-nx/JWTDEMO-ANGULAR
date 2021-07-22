package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Ha;
import com.mycompany.myapp.repository.HaRepository;
import com.mycompany.myapp.service.HaService;
import com.mycompany.myapp.service.dto.HaDTO;
import com.mycompany.myapp.service.mapper.HaMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Ha}.
 */
@Service
@Transactional
public class HaServiceImpl implements HaService {

    private final Logger log = LoggerFactory.getLogger(HaServiceImpl.class);

    private final HaRepository haRepository;

    private final HaMapper haMapper;

    public HaServiceImpl(HaRepository haRepository, HaMapper haMapper) {
        this.haRepository = haRepository;
        this.haMapper = haMapper;
    }

    @Override
    public HaDTO save(HaDTO haDTO) {
        log.debug("Request to save Ha : {}", haDTO);
        Ha ha = haMapper.toEntity(haDTO);
        ha = haRepository.save(ha);
        return haMapper.toDto(ha);
    }

    @Override
    public Optional<HaDTO> partialUpdate(HaDTO haDTO) {
        log.debug("Request to partially update Ha : {}", haDTO);

        return haRepository
            .findById(haDTO.getId())
            .map(
                existingHa -> {
                    haMapper.partialUpdate(existingHa, haDTO);

                    return existingHa;
                }
            )
            .map(haRepository::save)
            .map(haMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<HaDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Has");
        return haRepository.findAll(pageable).map(haMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<HaDTO> findOne(Long id) {
        log.debug("Request to get Ha : {}", id);
        return haRepository.findById(id).map(haMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Ha : {}", id);
        haRepository.deleteById(id);
    }
}
