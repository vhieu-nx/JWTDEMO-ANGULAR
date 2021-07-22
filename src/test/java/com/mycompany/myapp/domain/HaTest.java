package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Ha.class);
        Ha ha1 = new Ha();
        ha1.setId(1L);
        Ha ha2 = new Ha();
        ha2.setId(ha1.getId());
        assertThat(ha1).isEqualTo(ha2);
        ha2.setId(2L);
        assertThat(ha1).isNotEqualTo(ha2);
        ha1.setId(null);
        assertThat(ha1).isNotEqualTo(ha2);
    }
}
