package com.adidas.dwx.microservice.demo.controller;

import com.adidas.dwx.microservice.demo.model.Weather;
import com.adidas.dwx.microservice.demo.model.WeatherForecast;
import com.adidas.dwx.microservice.demo.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/weather")
public class WeatherController {

    private WeatherService weatherService;

    @Autowired
    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @RequestMapping("/now/{country}/{city}")
    public Weather getWeather(@PathVariable String country, @PathVariable String city) {
        return weatherService.getWeather(country, city);
    }

    @RequestMapping("/forecast/{country}/{city}")
    public WeatherForecast getWeatherForecast(@PathVariable String country, @PathVariable String city) {
        return weatherService.getWeatherForecast(country, city);
    }
}
