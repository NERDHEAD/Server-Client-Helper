package com.nerdhead.restful.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestDto {
	private String seq;
	private String name;
	private String email;
	private String phon;
}
