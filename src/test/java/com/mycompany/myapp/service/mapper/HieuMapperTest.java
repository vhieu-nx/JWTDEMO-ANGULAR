package com.mycompany.myapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HieuMapperTest {

    private HieuMapper hieuMapper;

    @BeforeEach
    public void setUp() {
        hieuMapper = new HieuMapperImpl();
    }
}
