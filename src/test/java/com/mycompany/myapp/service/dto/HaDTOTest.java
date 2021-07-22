package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(HaDTO.class);
        HaDTO haDTO1 = new HaDTO();
        haDTO1.setId(1L);
        HaDTO haDTO2 = new HaDTO();
        assertThat(haDTO1).isNotEqualTo(haDTO2);
        haDTO2.setId(haDTO1.getId());
        assertThat(haDTO1).isEqualTo(haDTO2);
        haDTO2.setId(2L);
        assertThat(haDTO1).isNotEqualTo(haDTO2);
        haDTO1.setId(null);
        assertThat(haDTO1).isNotEqualTo(haDTO2);
    }
}
