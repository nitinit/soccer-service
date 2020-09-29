package com.ps.soccer.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class Country {
	@JsonProperty("country_id")
	private String countryId;
	@JsonProperty("country_name")
	private String countryName;
}
