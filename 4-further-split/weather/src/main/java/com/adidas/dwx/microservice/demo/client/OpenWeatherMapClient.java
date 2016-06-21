package com.adidas.dwx.microservice.demo.client;

import com.adidas.dwx.microservice.demo.conf.ApiProperties;
import com.adidas.dwx.microservice.demo.model.Weather;
import com.adidas.dwx.microservice.demo.model.WeatherForecast;
import com.adidas.dwx.microservice.demo.service.WeatherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriTemplate;

import java.net.URI;

@Service
public class OpenWeatherMapClient implements WeatherService {

    private static final String BASE_URL = "http://api.openweathermap.org/data/2.5/{action}?q={city},{country}&APPID={key}&units=metric&lang=de";
    private static final String WEATHER_ACTION = "weather";
    private static final String FORECAST_ACTION = "forecast";

    private static final Logger LOG = LoggerFactory.getLogger(OpenWeatherMapClient.class);

    private RestTemplate restTemplate;
    private String apiKey;

    @Autowired
    public OpenWeatherMapClient(RestTemplate restTemplate, ApiProperties apiProps) {
        this.restTemplate = restTemplate;
        this.apiKey = apiProps.getKey();
    }

    @Override
    public Weather getWeather(String country, String city) {
        LOG.info("Requesting current weather conditions for {}, {}", city, country);
        URI uri = new UriTemplate(BASE_URL).expand(WEATHER_ACTION, city, country, apiKey);
        return invoke(uri, Weather.class);
    }

    @Override
    public WeatherForecast getWeatherForecast(String country, String city) {
        LOG.info("Requesting weather forecast for {}, {}", city, country);
        URI uri = new UriTemplate(BASE_URL).expand(FORECAST_ACTION, city, country, apiKey);
        return invoke(uri, WeatherForecast.class);
    }

    private <T> T invoke(URI uri, Class<T> responseType) {
        RequestEntity<?> req = RequestEntity.get(uri).accept(MediaType.APPLICATION_JSON_UTF8).build();
        ResponseEntity<T> resp = restTemplate.exchange(req, responseType);
        return resp.getBody();
    }

}
