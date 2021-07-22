package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HieuDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(HieuDTO.class);
        HieuDTO hieuDTO1 = new HieuDTO();
        hieuDTO1.setId(1L);
        HieuDTO hieuDTO2 = new HieuDTO();
        assertThat(hieuDTO1).isNotEqualTo(hieuDTO2);
        hieuDTO2.setId(hieuDTO1.getId());
        assertThat(hieuDTO1).isEqualTo(hieuDTO2);
        hieuDTO2.setId(2L);
        assertThat(hieuDTO1).isNotEqualTo(hieuDTO2);
        hieuDTO1.setId(null);
        assertThat(hieuDTO1).isNotEqualTo(hieuDTO2);
    }
}
