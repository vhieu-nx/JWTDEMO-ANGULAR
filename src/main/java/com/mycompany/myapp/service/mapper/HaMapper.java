package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.service.dto.HaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Ha} and its DTO {@link HaDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface HaMapper extends EntityMapper<HaDTO, Ha> {}
