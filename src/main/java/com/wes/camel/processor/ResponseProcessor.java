package com.wes.camel.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wes.camel.dto.OrderDTO;
import com.wes.camel.dto.response.RestResponse;

import lombok.SneakyThrows;

@Component
public class ResponseProcessor implements Processor {

	private final ObjectMapper objectMapper;

	public ResponseProcessor(ObjectMapper objectMapper) {
		super();
		this.objectMapper = objectMapper;
	}

	@SneakyThrows
	@Override
	public void process(Exchange exchange) {
		RestResponse restResp = new RestResponse();

		var jsonPayload = exchange.getIn().getBody(String.class);
		OrderDTO order = objectMapper.readValue(jsonPayload, new TypeReference<OrderDTO>() {
		});

		restResp.setOutput(order);
		exchange.getIn().setBody(restResp);
		exchange.setProperty("RestResponse", restResp);

	}

}