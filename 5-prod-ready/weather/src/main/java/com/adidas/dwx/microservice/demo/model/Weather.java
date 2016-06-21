package com.adidas.dwx.microservice.demo.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class Weather extends WeatherEntry {

    private String name;
    private String id;

}