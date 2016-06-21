package com.adidas.dwx.microservice.demo.model;

import lombok.Data;
import lombok.Getter;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.LinkedList;
import java.util.List;

@Data
@Entity
public class Member {

    @Id
    private String name;

    @ElementCollection
    private List<Location> locations;

    public void addLocation(Location loc) {
        if (locations == null) {
            locations = new LinkedList<>();
        }

        locations.add(loc);
    }
}
