package com.unum.microservices.persistence.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/*
 * @author Rajesh Iyer
 */

@Component
public class Managers {
	
	private static final Logger logger = LoggerFactory.getLogger(Managers.class);

	private int assignmentCount = 0;
	private boolean assignClaim = false;
	private String _managerAssigned = "";

	String[] managers = new String[] { "sekhar", "kareem", "sharon" };

	public int getAssignmentCount() {
		return assignmentCount;
	}

	public void setAssignmentCount(int assignmentCount) {
		this.assignmentCount = assignmentCount;
	}

	public boolean isAssignClaim() {
		return assignClaim;
	}

	public void setAssignClaim(boolean assignClaim) {
		this.assignClaim = assignClaim;
	}

	public String[] getManagers() {
		return managers;
	}

	public void setManagers(String[] managers) {
		this.managers = managers;
	}

	public void assignManagerToClaim() {

		_managerAssigned = managers[assignmentCount];
		
		logger.debug("value of the assignmentCount:" + assignmentCount);

		if (assignmentCount == 2)
			assignmentCount = 0;
		else
			++assignmentCount;
	}

	public String get_managerAssigned() {
		return _managerAssigned;
	}

	public void set_managerAssigned(String _managerAssigned) {
		this._managerAssigned = _managerAssigned;
	}
	
	
}
