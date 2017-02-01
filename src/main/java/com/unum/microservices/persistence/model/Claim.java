package com.unum.microservices.persistence.model;

import org.springframework.stereotype.Component;

/*
 * @author Rajesh Iyer
 */

@Component
public class Claim {

	private String _id;

	private long claimNumber;

	private String ssn;

	private String medCode;

	private String state;

	private String company;

	private String category;

	private String priority;

	private String openDate;

	private int daysOverAging;

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public long getClaimNumber() {
		return claimNumber;
	}

	public void setClaimNumber(long claimNumber) {
		this.claimNumber = claimNumber;
	}

	public String getSsn() {
		return ssn;
	}

	public void setSsn(String ssn) {
		this.ssn = ssn;
	}

	public String getMedCode() {
		return medCode;
	}

	public void setMedCode(String medCode) {
		this.medCode = medCode;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public long getPriority() {

		try {
			return Long.parseLong(priority);
		} catch (NumberFormatException e) {

		}
		return -1;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getOpenDate() {
		return openDate;
	}

	public void setOpenDate(String openDate) {
		this.openDate = openDate;
	}

	public int getDaysOverAging() {
		return daysOverAging;
	}

	public void setDaysOverAging(int daysOverAging) {
		this.daysOverAging = daysOverAging;
	}

}