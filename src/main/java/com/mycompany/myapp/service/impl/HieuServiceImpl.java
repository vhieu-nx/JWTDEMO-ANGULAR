package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Hieu;
import com.mycompany.myapp.repository.HieuRepository;
import com.mycompany.myapp.service.HieuService;
import com.mycompany.myapp.service.dto.HieuDTO;
import com.mycompany.myapp.service.mapper.HieuMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Hieu}.
 */
@Service
@Transactional
public class HieuServiceImpl implements HieuService {

    private final Logger log = LoggerFactory.getLogger(HieuServiceImpl.class);

    private final HieuRepository hieuRepository;

    private final HieuMapper hieuMapper;

    public HieuServiceImpl(HieuRepository hieuRepository, HieuMapper hieuMapper) {
        this.hieuRepository = hieuRepository;
        this.hieuMapper = hieuMapper;
    }

    @Override
    public HieuDTO save(HieuDTO hieuDTO) {
        log.debug("Request to save Hieu : {}", hieuDTO);
        Hieu hieu = hieuMapper.toEntity(hieuDTO);
        hieu = hieuRepository.save(hieu);
        return hieuMapper.toDto(hieu);
    }

    @Override
    public Optional<HieuDTO> partialUpdate(HieuDTO hieuDTO) {
        log.debug("Request to partially update Hieu : {}", hieuDTO);

        return hieuRepository
            .findById(hieuDTO.getId())
            .map(
                existingHieu -> {
                    hieuMapper.partialUpdate(existingHieu, hieuDTO);

                    return existingHieu;
                }
            )
            .map(hieuRepository::save)
            .map(hieuMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<HieuDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Hieus");
        return hieuRepository.findAll(pageable).map(hieuMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<HieuDTO> findOne(Long id) {
        log.debug("Request to get Hieu : {}", id);
        return hieuRepository.findById(id).map(hieuMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Hieu : {}", id);
        hieuRepository.deleteById(id);
    }
}
