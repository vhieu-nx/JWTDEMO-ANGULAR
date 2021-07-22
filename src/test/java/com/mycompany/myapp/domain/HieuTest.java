package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HieuTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Hieu.class);
        Hieu hieu1 = new Hieu();
        hieu1.setId(1L);
        Hieu hieu2 = new Hieu();
        hieu2.setId(hieu1.getId());
        assertThat(hieu1).isEqualTo(hieu2);
        hieu2.setId(2L);
        assertThat(hieu1).isNotEqualTo(hieu2);
        hieu1.setId(null);
        assertThat(hieu1).isNotEqualTo(hieu2);
    }
}
