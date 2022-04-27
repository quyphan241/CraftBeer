package com.formos.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A Manufactor.
 */
@Entity
@Table(name = "manufactor")
public class Manufactor implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "manufactor")
    @JsonIgnoreProperties(value = { "beerCategory", "manufactor" }, allowSetters = true)
    private Set<Beer> beers = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Manufactor id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Manufactor name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Beer> getBeers() {
        return this.beers;
    }

    public void setBeers(Set<Beer> beers) {
        if (this.beers != null) {
            this.beers.forEach(i -> i.setManufactor(null));
        }
        if (beers != null) {
            beers.forEach(i -> i.setManufactor(this));
        }
        this.beers = beers;
    }

    public Manufactor beers(Set<Beer> beers) {
        this.setBeers(beers);
        return this;
    }

    public Manufactor addBeer(Beer beer) {
        this.beers.add(beer);
        beer.setManufactor(this);
        return this;
    }

    public Manufactor removeBeer(Beer beer) {
        this.beers.remove(beer);
        beer.setManufactor(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Manufactor)) {
            return false;
        }
        return id != null && id.equals(((Manufactor) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Manufactor{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
