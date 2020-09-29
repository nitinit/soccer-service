package com.ps.soccer.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Collections;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import com.ps.soccer.BaseTest;
import com.ps.soccer.exception.DataNotFoundException;
import com.ps.soccer.model.SoccerTeamStandingResponse;
import com.ps.soccer.restclient.SoccerApiClient;

@RunWith(SpringRunner.class)
public class SoccerLeagueServiceTest extends BaseTest {

	@TestConfiguration
	static class getSoccerLeagueService {

		@Bean
		SoccerLeagueService getSoccerLeagueServiceImpl() {
			return new SoccerLeagueServiceImpl();
		}
	}

	@Autowired
	SoccerLeagueService soccerLeagueService;

	@MockBean
	SoccerApiClient soccerApiClient;

	@Rule
	public ExpectedException exceptionRule = ExpectedException.none();

	@Before
	public void setup() throws IOException {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void getTeamStanding_withValidInputs() {

		when(soccerApiClient.getAllCountries()).thenReturn(buildCountryMap());
		when(soccerApiClient.getLeageForCountry(any())).thenReturn(buildLeague());
		when(soccerApiClient.getStandingForCountry(any())).thenReturn(buildLeagueStanding());

		SoccerTeamStandingResponse teamStandingResponse = soccerLeagueService.getTeamStandings("England",
				"Championship", "Stoke");
		
		SoccerTeamStandingResponse expectedResponse = buildSoccerTeamStandingResponse();
		assertThat(teamStandingResponse).isNotNull();
		assertThat(teamStandingResponse.getCountryId()).isEqualTo(expectedResponse.getCountryId());
		assertThat(teamStandingResponse.getCountryName()).isEqualTo(expectedResponse.getCountryName());
		assertThat(teamStandingResponse.getLeagueId()).isEqualTo(expectedResponse.getLeagueId());
		assertThat(teamStandingResponse.getLeagueName()).isEqualTo(expectedResponse.getLeagueName());
		assertThat(teamStandingResponse.getStandingPosition()).isEqualTo(expectedResponse.getStandingPosition());
		assertThat(teamStandingResponse.getTeamId()).isEqualTo(expectedResponse.getTeamId());
		assertThat(teamStandingResponse.getTeamName()).isEqualTo(expectedResponse.getTeamName());
	}

	@Test
	public void getTeamStanding_withInvalidCountry() {
		exceptionRule.expect(DataNotFoundException.class);
		exceptionRule.expectMessage("No record found for given country (Plase check your Input)");

		when(soccerApiClient.getAllCountries()).thenReturn(Collections.emptyMap());
		soccerLeagueService.getTeamStandings("England", "Championship", "Stoke");
	}

	@Test
	public void getTeamStanding_withInvalidLeague() {
		exceptionRule.expect(DataNotFoundException.class);
		exceptionRule.expectMessage("No record found for given league (Plase check your Input)");

		when(soccerApiClient.getAllCountries()).thenReturn(buildCountryMap());
		when(soccerApiClient.getLeageForCountry(any())).thenReturn(Collections.emptyList());

		soccerLeagueService.getTeamStandings("England", "Championship", "Stoke");

	}

	@Test
	public void getTeamStanding_withNoTeamInLeague() {
		exceptionRule.expect(DataNotFoundException.class);
		exceptionRule.expectMessage("No country/team in this League");

		when(soccerApiClient.getAllCountries()).thenReturn(buildCountryMap());
		when(soccerApiClient.getLeageForCountry(any())).thenReturn(buildLeague());
		when(soccerApiClient.getStandingForCountry(any())).thenReturn(Collections.emptyList());

		soccerLeagueService.getTeamStandings("England", "Championship", "Stoke");
	}

}
