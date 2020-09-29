package com.ps.soccer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ps.soccer.model.Country;
import com.ps.soccer.model.League;
import com.ps.soccer.model.LeagueTeamStanding;
import com.ps.soccer.model.SoccerTeamStandingResponse;

public class BaseTest {

	protected Map<String, Country> buildCountryMap() {
		Map<String, Country> countryMap = new HashMap<>();

		Country country = buildCountry();
		countryMap.put(country.getCountryName(), country);
		return countryMap;
	}

	protected Country buildCountry() {
		Country country = new Country();
		country.setCountryId("41");
		country.setCountryName("England");
		return country;
	}

	protected List<Country> buildCountryList() {
		List<Country> countrylist = new ArrayList<>();

		Country country = new Country();
		country.setCountryId("41");
		country.setCountryName("England");
		countrylist.add(country);
		return countrylist;
	}

	protected List<League> buildLeague() {
		List<League> leagueList = new ArrayList<>();

		League league = new League();
		league.setCountryId("41");
		league.setLeagueId("149");
		league.setLeagueName("Championship");
		leagueList.add(league);
		return leagueList;
	}

	protected List<LeagueTeamStanding> buildLeagueStanding() {
		List<LeagueTeamStanding> leagueList = new ArrayList<>();

		LeagueTeamStanding leagueStanding = new LeagueTeamStanding();
		leagueStanding.setCountryName("England");
		leagueStanding.setTeamId("2624");
		leagueStanding.setTeamName("Stoke");
		leagueStanding.setOverallLeaguePosition("17");
		leagueStanding.setLeagueId("149");
		leagueStanding.setLeagueName("Championship");
		leagueList.add(leagueStanding);
		return leagueList;
	}

	protected SoccerTeamStandingResponse buildSoccerTeamStandingResponse() {
		SoccerTeamStandingResponse soccerTeamStandingResponse = new SoccerTeamStandingResponse();

		soccerTeamStandingResponse.setCountryId("41");
		soccerTeamStandingResponse.setCountryName("England");
		soccerTeamStandingResponse.setLeagueId("149");
		soccerTeamStandingResponse.setLeagueName("Championship");
		soccerTeamStandingResponse.setStandingPosition("17");
		soccerTeamStandingResponse.setTeamId("2624");
		soccerTeamStandingResponse.setTeamName("Stoke");

		return soccerTeamStandingResponse;
	}

}
