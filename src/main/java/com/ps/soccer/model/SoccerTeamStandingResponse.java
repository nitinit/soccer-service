package com.ps.soccer.model;

import lombok.Data;

@Data
public class SoccerTeamStandingResponse {
	private String countryId;
	private String countryName;

	private String leagueId;
	private String leagueName;

	private String teamId;
	private String teamName;

	private String standingPosition;

}
