package com.adidas.dwx.microservice.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
public class Member {

    @Id
    private String name;

    @ElementCollection
    @JsonProperty
    private Set<Location> locations;

    public void addLocation(Location loc) {
        if (locations == null) {
            locations = new HashSet<>();
        }

        locations.add(loc);
    }

}
