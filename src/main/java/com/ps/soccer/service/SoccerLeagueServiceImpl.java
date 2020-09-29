package com.ps.soccer.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ps.soccer.exception.DataNotFoundException;
import com.ps.soccer.model.Country;
import com.ps.soccer.model.League;
import com.ps.soccer.model.LeagueTeamStanding;
import com.ps.soccer.model.SoccerTeamStandingResponse;
import com.ps.soccer.restclient.SoccerApiClient;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SoccerLeagueServiceImpl implements SoccerLeagueService {

	@Autowired
	private SoccerApiClient soccerApiClient;

	@Override
	public SoccerTeamStandingResponse getTeamStandings(String countryName, String leagueName, String teamName) {

		SoccerTeamStandingResponse response = new SoccerTeamStandingResponse();
		Country c = findCountryByName(countryName);
		assertInputFieldAndThrowException(COUNTRY_NAME, c);

		response.setCountryName(countryName);
		response.setCountryId(c.getCountryId());

		League league = findLeagueByCountryId(response.getCountryId(), leagueName);
		assertInputFieldAndThrowException(LEAGUE_NAME, league);

		response.setLeagueId(league.getLeagueId());
		response.setLeagueName(leagueName);

		List<LeagueTeamStanding> standings = findStandingForTeam(response.getLeagueId());

		LeagueTeamStanding leagueStanding = findStandingForTeam(countryName, teamName, standings);

		assertAndthrowDataNotFoundException("No country/team in this League(Please check your Input)", leagueStanding);

		response.setTeamId(leagueStanding.getTeamId());
		response.setTeamName(leagueStanding.getTeamName());
		response.setStandingPosition(leagueStanding.getOverallLeaguePosition());
		return response;
	}

	private LeagueTeamStanding findStandingForTeam(String countryName, String teamName,
			List<LeagueTeamStanding> standings) {
		Optional<LeagueTeamStanding> leagueStanding = standings.stream().filter(
				standing -> standing.getCountryName().equals(countryName) && standing.getTeamName().equals(teamName))
				.findFirst();
		return leagueStanding.isPresent() ? leagueStanding.get() : null;
	}

	public Country findCountryByName(String name) {
		return soccerApiClient.getAllCountries().get(name);
	}

	public League findLeagueByCountryId(String countryId, String leagueName) {
		List<League> allLeagues = soccerApiClient.getLeageForCountry(countryId);

		Optional<League> leagueVal = allLeagues.stream()
				.filter(league -> countryId.equals(league.getCountryId()) && leagueName.equals(league.getLeagueName()))
				.findFirst();
		return leagueVal.isPresent() ? leagueVal.get() : null;
	}

	public List<LeagueTeamStanding> findStandingForTeam(String leagueId) {
		return soccerApiClient.getStandingForCountry(leagueId);
	}

	private void assertInputFieldAndThrowException(String fieldName, Object value) {
		if (value == null) {
			log.error("No results found for this:", fieldName);
			throw new DataNotFoundException(
					"No record found for given " + fieldName + " (Plase check your Input)");
		}
	}

	private void assertAndthrowDataNotFoundException(String message, Object value) {
		if (value == null) {
			log.info(message);
			throw new DataNotFoundException(message);
		}
	}
}
