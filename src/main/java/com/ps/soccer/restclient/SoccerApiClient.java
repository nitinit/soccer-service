package com.ps.soccer.restclient;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.ps.soccer.config.SoccerAppConfig;
import com.ps.soccer.exception.ServiceNotAvailableException;
import com.ps.soccer.model.Country;
import com.ps.soccer.model.League;
import com.ps.soccer.model.LeagueTeamStanding;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SoccerApiClient {

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	private SoccerAppConfig appConfig;

	private final String COUNTRY_CACHE = "country-cache";
	public static final String REMOTE_API_CALL_ERROR = "Remote API Call Error- Service not Availble";

	@Retryable(maxAttempts = 3, value = HttpClientErrorException.class, backoff = @Backoff(delay = 3000, multiplier = 2))
	@Cacheable(cacheNames = COUNTRY_CACHE)
	public Map<String, Country> getAllCountries() {
		log.debug("getAllCountries-> Invoked");
		String url = new StringBuilder().append(appConfig.getHostUrl()).toString();

		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
				.queryParam("action", appConfig.getApiCountries()).queryParam("APIkey", appConfig.getApiKey());

		ParameterizedTypeReference<List<Country>> typeReference = new ParameterizedTypeReference<List<Country>>() {
		};

		ResponseEntity<List<Country>> countries = restClientCall(builder, typeReference);
		log.debug("getAllCountries Response Status: {}", countries.getStatusCode());

		return countries.getBody().stream().collect(Collectors.toMap(Country::getCountryName, Function.identity()));
	}

	@Retryable(maxAttempts = 35, value = HttpClientErrorException.class, backoff = @Backoff(delay = 3000, multiplier = 2))
	public List<League> getLeageForCountry(String country) {
		log.debug("getLeageForCountry -> country:{}", country);

		String url = new StringBuilder().append(appConfig.getHostUrl()).toString();

		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
				.queryParam("action", appConfig.getApiLeagues()).queryParam("country_id", country)
				.queryParam("APIkey", appConfig.getApiKey());

		ParameterizedTypeReference<List<League>> typeReference = new ParameterizedTypeReference<List<League>>() {
		};
		ResponseEntity<List<League>> leagues = restClientCall(builder, typeReference);

		log.debug("getLeageForCountry Response Status: {}", leagues.getStatusCode());
		return leagues.getBody();
	}

	@Retryable(maxAttempts = 35, value = HttpClientErrorException.class, backoff = @Backoff(delay = 3000, multiplier = 2))
	public List<LeagueTeamStanding> getStandingForCountry(String league) {
		log.debug("getStandingForCountry for League: {}", league);

		String url = new StringBuilder().append(appConfig.getHostUrl()).toString();

		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
				.queryParam("action", appConfig.getApiStandings()).queryParam("league_id", league)
				.queryParam("APIkey", appConfig.getApiKey());

		ParameterizedTypeReference<List<LeagueTeamStanding>> typeReference = new ParameterizedTypeReference<List<LeagueTeamStanding>>() {
		};

		ResponseEntity<List<LeagueTeamStanding>> standingsForLeagues = restClientCall(builder, typeReference);
		log.debug("getLeageForCountry Response Status: {}", standingsForLeagues.getStatusCode());

		return standingsForLeagues.getBody();
	}

	private <T> ResponseEntity<List<T>> restClientCall(UriComponentsBuilder builder,
			ParameterizedTypeReference<List<T>> typeReference) {
		try {
			return restTemplate.exchange(builder.toUriString(), HttpMethod.GET, null, typeReference);
		} catch (HttpStatusCodeException ex) {
			log.error(REMOTE_API_CALL_ERROR.concat(builder.toUriString()).concat(ex.getStatusCode().toString()), ex);
			throw new ServiceNotAvailableException(REMOTE_API_CALL_ERROR);
		}
	}
}
