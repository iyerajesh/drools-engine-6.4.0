package com.unum.microservices.drools.service;

import java.io.IOException;

import org.json.JSONObject;
import org.json.XML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/*
 * @author Rajesh Iyer
 */

@Service
public class XmlToJsonTransformer {

	@Autowired
	private Environment env;

	private static final Logger logger = LoggerFactory.getLogger(WorkdayService.class);

	public String transform(String xml) throws JsonParseException, JsonMappingException, IOException {

		JSONObject jObject = XML.toJSONObject(xml);
		ObjectMapper mapper = new ObjectMapper();
		mapper.enable(SerializationFeature.INDENT_OUTPUT);

		Object json = mapper.readValue(jObject.toString(), Object.class);
		String output = mapper.writeValueAsString(json);
		logger.debug("JSON output:" + output);

		return output;

	}
}
