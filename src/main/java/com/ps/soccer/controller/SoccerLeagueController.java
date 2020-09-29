package com.ps.soccer.controller;

import static com.ps.soccer.service.SoccerLeagueService.COUNTRY_NAME;
import static com.ps.soccer.service.SoccerLeagueService.LEAGUE_NAME;
import static com.ps.soccer.service.SoccerLeagueService.TEAM_NAME;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ps.soccer.exception.ApiError;
import com.ps.soccer.exception.GenericInputException;
import com.ps.soccer.model.SoccerTeamStandingResponse;
import com.ps.soccer.service.SoccerLeagueService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/v1/soccer")
@Api(tags = "Soccer League", value = "Provides Soccer League Standing details")
@Slf4j
public class SoccerLeagueController {

	@Autowired
	SoccerLeagueService service;

	@RequestMapping(method = RequestMethod.GET, path = "/getStandings", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Get league standing of Team of Soccer")
	@ResponseBody
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully retrieved Product(s)"),
			@ApiResponse(code = 404, message = "No country/league/team in this League", response = ApiError.class),
			@ApiResponse(code = 400, message = "Invalid Request params", response = ApiError.class) })
	public SoccerTeamStandingResponse getStandingDetails(@RequestParam(required = false) String countryName,
			@RequestParam(required = false) String leagueName, @RequestParam(required = false) String teamName) {
		log.info("getStandingDetails for countryName :{}, leagueName :{}, teamName :{}", countryName, leagueName,
				teamName);

		validateInputs(countryName, leagueName, teamName);
		SoccerTeamStandingResponse response = new SoccerTeamStandingResponse();

		response = service.getTeamStandings(countryName, leagueName, teamName);
		return response;
	}

	private void validateInputs(String countryName, String leagueName, String teamName) {
		GenericInputException inputException = new GenericInputException();
		if (StringUtils.isEmpty(countryName)) {
			inputException.add(COUNTRY_NAME);
		}
		if (StringUtils.isEmpty(leagueName)) {
			inputException.add(LEAGUE_NAME);
		}
		if(StringUtils.isEmpty(teamName)) {
			inputException.add(TEAM_NAME);
		}
		if (!CollectionUtils.isEmpty(inputException.getMessages())) {
			throw inputException;
		}
	}

}