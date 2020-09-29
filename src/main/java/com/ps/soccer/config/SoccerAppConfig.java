package com.ps.soccer.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
public class SoccerAppConfig {

	@Value("${soccer.hostUrl}")
	private String hostUrl;

	@Value("${soccer.apiKey}")
	private String apiKey;

	@Value("${soccer.apiCountries}")
	private String apiCountries;

	@Value("${soccer.apiLeagues}")
	private String apiLeagues;

	@Value("${soccer.apiStandings}")
	private String apiStandings;
}