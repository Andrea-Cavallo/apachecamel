package com.wes.camel.processor;

import static com.wes.camel.utils.Constants.CREATE_ORDER;

import java.util.ArrayList;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wes.camel.dto.OrderDTO;
import com.wes.camel.dto.OrderDTO.OrderStatus;
import com.wes.camel.dto.response.RestResponse;

@Component
public class CreateOrderProcessor implements Processor {

	public CreateOrderProcessor(SendToEndpointProcessor sendToEndpointProcessor, ObjectMapper objectMapper) {
		super();
		this.sendToEndpointProcessor = sendToEndpointProcessor;
		this.objectMapper = objectMapper;
	}

	private final SendToEndpointProcessor sendToEndpointProcessor;
	private final ObjectMapper objectMapper;

	@Override

	public void process(Exchange exchange) throws Exception {

		OrderDTO body = exchange.getProperty("orderInput", OrderDTO.class);

		if (body.getOrderStatus() != OrderStatus.REFUSED) {

			String bodySerialized = objectMapper.writeValueAsString(body);
			exchange.getIn().setBody(bodySerialized);
			String createOrderURI = String.format("%s", CREATE_ORDER);

			sendToEndpointProcessor.sendToEndpoint(createOrderURI, exchange);
		} else {
			RestResponse restResp = new RestResponse();
			List<String> err = new ArrayList<>();
			String error = "Product is not in stock we can't process your order now";
			err.add(error);
			restResp.setErrorMessages(err);

			restResp.setOutput(body);
			exchange.getIn().setBody(restResp);
		}
	}

}
