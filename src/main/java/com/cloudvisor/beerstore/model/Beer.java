package com.cloudvisor.beerstore.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@Entity
@Table(name = "beer")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
public class Beer {

    @Id
    @SequenceGenerator(name = "beer_seq", sequenceName = "beer_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "beer_seq")
    @EqualsAndHashCode.Include
    private Long id;

    @NotBlank(message = "name.mandatory")
    private String name;

    @NotNull(message = "type.mandatory")
    private BeerType type;

    @DecimalMin(message = "volume.minValue", value = "0")
    @NotNull(message = "volume.mandatory")
    private BigDecimal volume;

    public Beer(String name, BeerType type, BigDecimal volume) {
        this.name = name;
        this.type = type;
        this.volume = volume;
    }

    @JsonIgnore
    public boolean isNew() {
        return getId() == null;
    }

    @JsonIgnore
    public boolean isUpdate() {
        return getId() != null;
    }
}