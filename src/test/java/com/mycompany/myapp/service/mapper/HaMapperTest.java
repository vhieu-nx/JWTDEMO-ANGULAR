package com.mycompany.myapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HaMapperTest {

    private HaMapper haMapper;

    @BeforeEach
    public void setUp() {
        haMapper = new HaMapperImpl();
    }
}
