package com.formos.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.formos.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BeerCategoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BeerCategory.class);
        BeerCategory beerCategory1 = new BeerCategory();
        beerCategory1.setId(1L);
        BeerCategory beerCategory2 = new BeerCategory();
        beerCategory2.setId(beerCategory1.getId());
        assertThat(beerCategory1).isEqualTo(beerCategory2);
        beerCategory2.setId(2L);
        assertThat(beerCategory1).isNotEqualTo(beerCategory2);
        beerCategory1.setId(null);
        assertThat(beerCategory1).isNotEqualTo(beerCategory2);
    }
}
