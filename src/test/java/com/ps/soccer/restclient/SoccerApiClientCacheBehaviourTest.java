package com.ps.soccer.restclient;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import com.ps.soccer.BaseTest;
import com.ps.soccer.config.SoccerAppConfig;
import com.ps.soccer.model.Country;

@RunWith(SpringRunner.class)
@TestPropertySource("/test.properties")
public class SoccerApiClientCacheBehaviourTest extends BaseTest {

	@TestConfiguration
	@EnableCaching
	static class getSoccerApiClient {

		@Bean
		SoccerApiClient getSoccerApiClient() {
			return new SoccerApiClient();
		}

		@Bean
		SoccerAppConfig getSoccerAppConfig() {
			return new SoccerAppConfig();
		}

		@Bean
		public CacheManager cacheManager() {
			return new ConcurrentMapCacheManager("country-cache");
		}
	}

	@Autowired
	SoccerApiClient soccerApiClient;

	@Autowired
	SoccerAppConfig soccerAppConfig;

	@MockBean
	RestTemplate restTemplate;

	private ResponseEntity<List<Country>> countryListResponse;

	@Before
	public void setup() throws IOException {
		MockitoAnnotations.initMocks(this);
		countryListResponse = new ResponseEntity<>(buildCountryList(), HttpStatus.OK);
	}

	@Test
	public void getAllCountriesCachingBehaviour() {
		Mockito.when(restTemplate.exchange(ArgumentMatchers.anyString(), ArgumentMatchers.any(HttpMethod.class),
				ArgumentMatchers.<HttpEntity<?>>any(),
				ArgumentMatchers.<ParameterizedTypeReference<List<Country>>>any())).thenReturn(countryListResponse);

		soccerApiClient.getAllCountries();

		verify(restTemplate, times(1)).exchange(ArgumentMatchers.anyString(), ArgumentMatchers.any(HttpMethod.class),
				ArgumentMatchers.<HttpEntity<?>>any(),
				ArgumentMatchers.<ParameterizedTypeReference<List<Country>>>any());

		/*
		 * In Second method invocation -> Verify call is not going inside method by
		 * asserting that restTemplate.exchange is invoked only once
		 */
		soccerApiClient.getAllCountries();
		verify(restTemplate, times(1)).exchange(ArgumentMatchers.anyString(), ArgumentMatchers.any(HttpMethod.class),
				ArgumentMatchers.<HttpEntity<?>>any(),
				ArgumentMatchers.<ParameterizedTypeReference<List<Country>>>any());
	}
}
