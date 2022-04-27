package com.formos.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.formos.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ManufactorTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Manufactor.class);
        Manufactor manufactor1 = new Manufactor();
        manufactor1.setId(1L);
        Manufactor manufactor2 = new Manufactor();
        manufactor2.setId(manufactor1.getId());
        assertThat(manufactor1).isEqualTo(manufactor2);
        manufactor2.setId(2L);
        assertThat(manufactor1).isNotEqualTo(manufactor2);
        manufactor1.setId(null);
        assertThat(manufactor1).isNotEqualTo(manufactor2);
    }
}
