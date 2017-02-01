package com.unum.microservices.persistence.model;

import java.io.IOException;
import java.time.Year;
import java.util.HashMap;
import java.util.Map;

import javax.xml.datatype.DatatypeConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jayway.jsonpath.JsonPath;
import com.unum.microservices.drools.service.WorkdayService;

/*
 * @author Rajesh Iyer
 */

@Component
public class Adjusters {

	private static final Logger logger = LoggerFactory.getLogger(Adjusters.class);

	private String medCode;
	private long casePriority;
	private int adjusterExperience;
	private String _adjusterAssigned = "";

	@Autowired
	private WorkdayService wdService;

	private static Map<String, String> adjusterEmpIds = new HashMap<String, String>();

	static {
		adjusterEmpIds.put("kareem", "64491");
		adjusterEmpIds.put("april", "23876");
		adjusterEmpIds.put("aimee", "57024");
		adjusterEmpIds.put("brittany", "89593");
		adjusterEmpIds.put("lee", "21086");
		adjusterEmpIds.put("wendy", "49608");
	}

	public void setClaim(Claim claim) {

		this.setMedCode(claim.getMedCode());
		this.setCasePriority(claim.getPriority());
	}

	public String get_adjusterAssigned() {
		return _adjusterAssigned;
	}

	public void set_adjusterAssigned(String _adjusterAssigned) {
		this._adjusterAssigned = _adjusterAssigned;
	}

	public String getMedCode() {
		return medCode;
	}

	public void setMedCode(String medCode) {
		this.medCode = medCode;
	}

	public static Map<String, String> getAdjusterEmpIds() {
		return adjusterEmpIds;
	}

	public void setAdjusterEmpIds(Map<String, String> adjusterEmpIds) {
		this.adjusterEmpIds = adjusterEmpIds;
	}

	public void assignWellnessClaimsToAdjuster() {

		String[] wellnessAdjusters = new String[] { "kareem", "april", "aimee" };
		_adjusterAssigned = wellnessAdjusters[0];

		logger.debug("wellness adjuster assigned:" + this.get_adjusterAssigned());
	}

	public void assignCancerClaimsToAdjuster() {

		String[] cancerAdjusters = new String[] { "brittany", "lee", "wendy" };
		_adjusterAssigned = cancerAdjusters[0];

		logger.debug("cancer adjuster assigned:" + this.get_adjusterAssigned());

	}

	public void assignCancerClaimsToAdjusterWithExperience()
			throws JsonProcessingException, DatatypeConfigurationException, IOException {

		String[] cancerAdjusters = new String[] { "wendy", "lee" };
		_adjusterAssigned = cancerAdjusters[0];

		
		/* Workday Integration - to get the employee details  */
				String empJson = wdService.getEmployeeInfo(_adjusterAssigned);

		String seniorityDate = JsonPath.read(empJson,
				"$.wd:Employee.wd:Employee_Data.wd:Worker_Status_Data.wd:Seniority_Date");

		int currentYear = Year.now().getValue();
		int seniorityYear = Integer.parseInt(seniorityDate.substring(0, seniorityDate.indexOf("-", 0)));

		logger.debug("current year:" + currentYear);
		logger.debug("Seniority Year:" + seniorityYear);
		logger.debug("Adjuster Experience:" + getAdjusterExperience());

		if ((currentYear - seniorityYear) < 15)
			_adjusterAssigned = cancerAdjusters[1];
			
		

		logger.debug("cancer adjuster assigned:" + this.get_adjusterAssigned());

	}

	public long getCasePriority() {
		return casePriority;
	}

	public void setCasePriority(long casePriority) {
		this.casePriority = casePriority;
	}

	public int getAdjusterExperience() {
		return adjusterExperience;
	}

	public void setAdjusterExperience(int adjusterExperience) {
		this.adjusterExperience = adjusterExperience;
	}

}
