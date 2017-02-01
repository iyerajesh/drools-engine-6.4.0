package com.unum.microservices.drools.web;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.unum.microservices.drools.service.RulesService;
import com.unum.microservices.persistence.model.Claim;

@RestController
public class RulesAPIController {

	@Autowired
	private RulesService rulesService;

	private static final Logger logger = LoggerFactory.getLogger(RulesAPIController.class);

	@RequestMapping(value = "/assignments/claims/manager", method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.CREATED)
	public String getManagerAssignment(@RequestBody Map<String, String> facts) {

		return rulesService.assignClaimsToManager(true);
	}

	@RequestMapping(value = "/assignments/claims/adjuster", method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.CREATED)
	public String getAdjusterAssignment(@RequestBody Claim claim) {

		return rulesService.assignClaimsToAdjuster(claim);
	}

}