package com.unum.microservices.drools.service;

import java.io.IOException;
import java.time.Year;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.jayway.jsonpath.JsonPath;
import com.unum.microservices.persistence.model.Adjusters;
import com.workday.hr.EmployeeDataType;
import com.workday.hr.EmployeeGetType;
import com.workday.hr.EmployeeReferenceType;
import com.workday.hr.EmployeeType;
import com.workday.hr.ExternalIntegrationIDReferenceDataType;
import com.workday.hr.IDType;
import com.workday.hr.WorkerStatusDataType;

/*
 * @author Rajesh Iyer
 */

@Service
@PropertySource({ "classpath:${envTarget:workday-service}.properties" })
public class WorkdayService {

	@Autowired
	private Environment env;

	@Autowired
	private XmlToJsonTransformer xmlToJsonTransformer;

	private static final Logger logger = LoggerFactory.getLogger(WorkdayService.class);

	/*
	 * Method to get the Employee Info from Workday for the selected Adjuster.
	 */
	public String getEmployeeInfo(String adjusterName) throws DatatypeConfigurationException, IOException {

		String employeeId = Adjusters.getAdjusterEmpIds().get(adjusterName);

		GregorianCalendar gc = new GregorianCalendar();
		gc.setTimeInMillis(new Date().getTime());

		XMLGregorianCalendar xGC = DatatypeFactory.newInstance().newXMLGregorianCalendar(gc);

		EmployeeGetType employeeInfo = new EmployeeGetType();
		employeeInfo.setAsOfDate(xGC);
		employeeInfo.setAsOfMoment(xGC);
		employeeInfo.setVersion("27.1");

		EmployeeReferenceType employeeReference = new EmployeeReferenceType();
		ExternalIntegrationIDReferenceDataType externalID = new ExternalIntegrationIDReferenceDataType();

		IDType idType = new IDType();
		idType.setSystemID(employeeId);
		idType.setValue("WD-EMPLID");

		externalID.setID(idType);
		externalID.setDescriptor("WD-EMPLID");
		employeeReference.setIntegrationIDReference(externalID);
		employeeInfo.setEmployeeReference(employeeReference);

		XmlMapper xmlMapper = new XmlMapper();
		String employeeXML = xmlMapper.writeValueAsString(employeeInfo);

		employeeXML = "<bsvc:Employee_Get " + "xmlns:bsvc=\"urn:com.workday/bsvc\" bsvc:As_Of_Date=\"2016-11-30\""
				+ " bsvc:As_Of_Moment=\"2016-11-30\" bsvc:version=\"v27.1\">" + "<bsvc:Employee_Reference>"
				+ "<bsvc:Integration_ID_Reference" + " bsvc:Descriptor=\"WD-EMPLID\">"
				+ "<bsvc:ID bsvc:System_ID=\"WD-EMPLID\">49608</bsvc:ID>" + "</bsvc:Integration_ID_Reference>"
				+ "</bsvc:Employee_Reference>" + "</bsvc:Employee_Get>";

		logger.debug("The request XML being sent to WD:" + employeeXML);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_XML);

		HttpEntity<String> request = new HttpEntity<String>(employeeXML, headers);

		RestTemplate restTemplate = new RestTemplate();
		String employeeStr = restTemplate.postForObject("http://" + env.getProperty("wd.host") + ":"
				+ env.getProperty("wd.port") + env.getProperty("wd.employee.uri"), request, String.class);

		return xmlToJsonTransformer.transform(employeeStr);
	}

}
