package com.ps.soccer.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.ps.soccer.BaseTest;
import com.ps.soccer.config.SoccerAppConfig;
import com.ps.soccer.exception.DataNotFoundException;
import com.ps.soccer.model.SoccerTeamStandingResponse;
import com.ps.soccer.service.SoccerLeagueService;

@RunWith(SpringRunner.class)
@WebMvcTest(SoccerLeagueController.class)
public class SoccerLeagueControllerTest extends BaseTest {

	@Autowired
	private MockMvc mockMVC;

	@MockBean
	SoccerLeagueService soccerLeagueService;

	@MockBean
	SoccerAppConfig appConfig;

	@Before
	public void setUp() {

	}

	@Test
	public void whenSoccerLeagueStandingAPIWithEmptyResponse() throws Exception {
		SoccerTeamStandingResponse response = new SoccerTeamStandingResponse();

		when(soccerLeagueService.getTeamStandings(any(), any(), any()))
				.thenReturn(response);

		mockMVC.perform(
				get("/v1/soccer/getStandings?countryName=England&&leagueName=Championship&&teamName=Stoke"))
				.andExpect(status().isOk());
	}

	@Test
	public void whenSoccerLeagueStandingAPIWithResonse() throws Exception {

		when(soccerLeagueService.getTeamStandings(any(), any(), any())).thenReturn(buildSoccerTeamStandingResponse());

		mockMVC.perform(get("/v1/soccer/getStandings?countryName=England&&leagueName=Championship&&teamName=Stoke"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.countryId").value("41"))
				.andExpect(jsonPath("$.countryName").value("England")).andExpect(jsonPath("$.leagueId").value("149"))
				.andExpect(jsonPath("$.leagueName").value("Championship"))
				.andExpect(jsonPath("$.standingPosition").value("17"));
	}

	@Test
	public void whenSoccerLeagueStandingAPIWithInvalidValues() throws Exception {

		when(soccerLeagueService.getTeamStandings(any(), any(), any())).thenReturn(buildSoccerTeamStandingResponse());

		mockMVC.perform(get("/v1/soccer/getStandings"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.messages[0]").value("No country provided(please do verify)"))
				.andExpect(jsonPath("$.messages[1]").value("No league provided(please do verify)"))
				.andExpect(jsonPath("$.messages[2]").value("No team provided(please do verify)"));

		mockMVC.perform(get("/v1/soccer/getStandings?countryName=England")).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.messages[0]").value("No league provided(please do verify)"))
				.andExpect(jsonPath("$.messages[1]").value("No team provided(please do verify)"));

		mockMVC.perform(get("/v1/soccer/getStandings?countryName=England&&leagueName=Championship"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.messages[0]").value("No team provided(please do verify)"));
	}

	@Test
	public void whenSoccerLeagueStandingAPIWithNoDataFound() throws Exception {

		when(soccerLeagueService.getTeamStandings(any(), any(), any())).thenThrow(new DataNotFoundException());

		mockMVC.perform(get("/v1/soccer/getStandings?countryName=England&&leagueName=Championship&&teamName=Stoke"))
				.andExpect(status().isNotFound());
	}
}
