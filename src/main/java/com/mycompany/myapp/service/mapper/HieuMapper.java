package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.service.dto.HieuDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Hieu} and its DTO {@link HieuDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface HieuMapper extends EntityMapper<HieuDTO, Hieu> {}
