package com.adidas.dwx.microservice.demo;

import com.adidas.dwx.microservice.demo.conf.ApiProperties;
import com.google.common.cache.CacheBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.guava.GuavaCacheManager;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;

@SpringBootApplication
@EnableEurekaClient
@EnableConfigurationProperties(ApiProperties.class)
@EnableCircuitBreaker
@EnableCaching
public class WeatherApplication {

	public static void main(String[] args) {
		SpringApplication.run(WeatherApplication.class, args);
	}

	@Bean
	public RestTemplate getRestTemplate() {
		return new RestTemplate();
	}

	@Bean
	public CacheManager getCacheManager() {
		GuavaCacheManager gcm = new GuavaCacheManager();
		gcm.setCacheBuilder(CacheBuilder.newBuilder().expireAfterWrite(30, TimeUnit.SECONDS));
		return gcm;
	}
}
