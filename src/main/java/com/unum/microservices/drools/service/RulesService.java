package com.unum.microservices.drools.service;

import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.StatelessKieSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.unum.microservices.persistence.model.Adjusters;
import com.unum.microservices.persistence.model.Claim;
import com.unum.microservices.persistence.model.Managers;

/*
 * @author Rajesh Iyer
 */

@Service
public class RulesService {

	private static final Logger logger = LoggerFactory.getLogger(RulesService.class);

	@Autowired
	private KieContainer kieContainer;

	@Autowired
	Managers managers;
	
	@Autowired
	Adjusters adjusters;
	
	/*
	 * Method assigns the manager to the claim
	 */
	public String assignClaimsToManager(boolean assignClaim) {

		managers.setAssignClaim(assignClaim);

		StatelessKieSession statelessKieSession = kieContainer.newStatelessKieSession();
		statelessKieSession.execute(managers);

		return managers.get_managerAssigned();

	}

	/*
	 * Method assigns the adjuster to the claim, based on the claim data, and Workday HR data
	 */
	public String assignClaimsToAdjuster(Claim claim) {
		
		adjusters.setClaim(claim);

		StatelessKieSession statelessKieSession = kieContainer.newStatelessKieSession();
		statelessKieSession.execute(adjusters);

		return adjusters.get_adjusterAssigned();

	}
	
	
	
}
