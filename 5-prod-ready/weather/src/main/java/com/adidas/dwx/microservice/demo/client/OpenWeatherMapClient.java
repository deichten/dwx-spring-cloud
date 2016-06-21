package com.adidas.dwx.microservice.demo.client;

import com.adidas.dwx.microservice.demo.conf.ApiProperties;
import com.adidas.dwx.microservice.demo.model.Weather;
import com.adidas.dwx.microservice.demo.model.WeatherEntry;
import com.adidas.dwx.microservice.demo.model.WeatherForecast;
import com.adidas.dwx.microservice.demo.service.WeatherService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriTemplate;

import javax.validation.constraints.Max;
import java.net.URI;
import java.util.LinkedList;
import java.util.List;

@Service
public class OpenWeatherMapClient implements WeatherService {

    private static final String BASE_URL = "http://api.openweathermap.org/data/2.5/{action}?q={city},{country}&APPID={key}&units=metric&lang=de";
    private static final String WEATHER_ACTION = "weather";
    private static final String FORECAST_ACTION = "forecast";

    private static final double FAILURE_PROBABILITY = 0.1;
    private static final double DELAY_PROBABILITY = 0.4;
    private static final int MIN_DELAY = 300;
    private static final int MAX_DELAY = 3000;

    private static final Logger LOG = LoggerFactory.getLogger(OpenWeatherMapClient.class);

    private RestTemplate restTemplate;
    private String apiKey;

    @Autowired
    public OpenWeatherMapClient(RestTemplate restTemplate, ApiProperties apiProps) {
        this.restTemplate = restTemplate;
        this.apiKey = apiProps.getKey();
    }

    @Override
    @Cacheable("current")
    @HystrixCommand
    public Weather getWeather(String country, String city) {
        LOG.info("Requesting current weather conditions for {}, {}", city, country);
        URI uri = new UriTemplate(BASE_URL).expand(WEATHER_ACTION, city, country, apiKey);
        return invoke(uri, Weather.class);
    }

    @Override
    @Cacheable("forecast")
    @HystrixCommand
    public WeatherForecast getWeatherForecast(String country, String city) {
        LOG.info("Requesting weather forecast for {}, {}", city, country);
        URI uri = new UriTemplate(BASE_URL).expand(FORECAST_ACTION, city, country, apiKey);
        return invoke(uri, WeatherForecast.class);
    }

    private <T> T invoke(URI uri, Class<T> responseType) {

        // check for random failure
        randomFailure();

        // cause random delay
        randomDelay();

        RequestEntity<?> req = RequestEntity.get(uri).accept(MediaType.APPLICATION_JSON_UTF8).build();
        ResponseEntity<T> resp = restTemplate.exchange(req, responseType);
        return resp.getBody();
    }

    private void randomFailure() {
        if (Math.random() < FAILURE_PROBABILITY) {
            throw new RuntimeException("Simulated backend failure.");
        }
    }

    private void randomDelay() {
        if (Math.random() < DELAY_PROBABILITY) {
            int delay = (int) ((Math.random() * (MAX_DELAY - MIN_DELAY)) + MIN_DELAY);
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                LOG.error("Exception happened in delay method.", e);
            }
        }
    }

}
