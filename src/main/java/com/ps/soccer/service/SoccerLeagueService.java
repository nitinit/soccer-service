package com.ps.soccer.service;

import com.ps.soccer.model.SoccerTeamStandingResponse;

public interface SoccerLeagueService {
	String LEAGUE_NAME = "league";
	String COUNTRY_NAME = "country";
	String TEAM_NAME = "team";

	public SoccerTeamStandingResponse getTeamStandings(String countryName, String leagueName, String teamName);
}
