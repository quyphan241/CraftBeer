package com.formos.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A BeerCategory.
 */
@Entity
@Table(name = "beer_category")
public class BeerCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "category_name")
    private String categoryName;

    @OneToMany(mappedBy = "beerCategory")
    @JsonIgnoreProperties(value = { "beerCategory", "manufactor" }, allowSetters = true)
    private Set<Beer> beers = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public BeerCategory id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategoryName() {
        return this.categoryName;
    }

    public BeerCategory categoryName(String categoryName) {
        this.setCategoryName(categoryName);
        return this;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Set<Beer> getBeers() {
        return this.beers;
    }

    public void setBeers(Set<Beer> beers) {
        if (this.beers != null) {
            this.beers.forEach(i -> i.setBeerCategory(null));
        }
        if (beers != null) {
            beers.forEach(i -> i.setBeerCategory(this));
        }
        this.beers = beers;
    }

    public BeerCategory beers(Set<Beer> beers) {
        this.setBeers(beers);
        return this;
    }

    public BeerCategory addBeer(Beer beer) {
        this.beers.add(beer);
        beer.setBeerCategory(this);
        return this;
    }

    public BeerCategory removeBeer(Beer beer) {
        this.beers.remove(beer);
        beer.setBeerCategory(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BeerCategory)) {
            return false;
        }
        return id != null && id.equals(((BeerCategory) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BeerCategory{" +
            "id=" + getId() +
            ", categoryName='" + getCategoryName() + "'" +
            "}";
    }
}
