package com.adidas.dwx.microservice.demo.service;

import com.adidas.dwx.microservice.demo.model.Weather;
import com.adidas.dwx.microservice.demo.model.WeatherForecast;

public interface WeatherService {

    Weather getWeather(String country, String city);

    WeatherForecast getWeatherForecast(String country, String city);

}
