package com.adidas.dwx.microservice.demo.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class Location implements Serializable {

    private String country;

    private String city;

}
