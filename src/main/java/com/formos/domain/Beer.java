package com.formos.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;

/**
 * A Beer.
 */
@Entity
@Table(name = "beer")
public class Beer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "country")
    private String country;

    @Column(name = "price")
    private Long price;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "category_id")
    @JsonIgnoreProperties(value = { "beers" }, allowSetters = true)
    private BeerCategory beerCategory;

    @ManyToOne
    @JoinColumn(name = "manufactor_id")
    @JsonIgnoreProperties(value = { "beers" }, allowSetters = true)
    private Manufactor manufactor;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Beer id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Beer name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return this.country;
    }

    public Beer country(String country) {
        this.setCountry(country);
        return this;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Long getPrice() {
        return this.price;
    }

    public Beer price(Long price) {
        this.setPrice(price);
        return this;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public String getDescription() {
        return this.description;
    }

    public Beer description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BeerCategory getBeerCategory() {
        return this.beerCategory;
    }

    public void setBeerCategory(BeerCategory beerCategory) {
        this.beerCategory = beerCategory;
    }

    public Beer beerCategory(BeerCategory beerCategory) {
        this.setBeerCategory(beerCategory);
        return this;
    }

    public Manufactor getManufactor() {
        return this.manufactor;
    }

    public void setManufactor(Manufactor manufactor) {
        this.manufactor = manufactor;
    }

    public Beer manufactor(Manufactor manufactor) {
        this.setManufactor(manufactor);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Beer)) {
            return false;
        }
        return id != null && id.equals(((Beer) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Beer{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", country='" + getCountry() + "'" +
            ", price=" + getPrice() +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
