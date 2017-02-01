package com.unum.microservices.drools.test;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kie.api.runtime.KieSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.unum.microservices.drools.config.DroolsAutoConfiguration;
import com.unum.microservices.drools.config.DroolsEngine;
import com.unum.microservices.persistence.model.Address;
import com.unum.microservices.persistence.model.Managers;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { DroolsEngine.class, DroolsAutoConfiguration.class })
public class DroolsApplicationTest {

	private static final Logger LOG = LoggerFactory.getLogger(DroolsApplicationTest.class);

	@Autowired
	private KieSession kieSession;

	@Test
	public void contextLoads() {

		Managers managers = new Managers();
		managers.setAssignClaim(true);

		// When
		// Let´s give the Drools Knowledge-Base an Object, we can then apply
		// rules on
		kieSession.insert(managers);
		int ruleFiredCount = kieSession.fireAllRules();
		
		LOG.debug("--- The manager assigned to the claim:---" + managers.get_managerAssigned());

		// Then
		assertEquals("there´s 1 rule, so there should be 1 fired", 1, ruleFiredCount);
		LOG.debug("Rules checked: {}" + ruleFiredCount);
	}

}