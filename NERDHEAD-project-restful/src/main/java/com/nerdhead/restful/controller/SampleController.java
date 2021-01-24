package com.nerdhead.restful.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;



@Controller
public class SampleController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@RequestMapping(value = "index.do")
	public String index() {
		return "index";
	}
}
