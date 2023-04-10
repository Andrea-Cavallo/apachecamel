package com.wes.camel.processor;

import static com.wes.camel.utils.Constants.FETCH_PRODUCT_BY_NAME_URI;
import static com.wes.camel.utils.Constants.USER_REQUEST;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

import com.wes.camel.dto.request.UserRequest;

@Component
public class GetProductInfoProcessor implements Processor {

	private final SendToEndpointProcessor sendToEndpointProcessor;

	public GetProductInfoProcessor(SendToEndpointProcessor sendToEndpointProcessor) {
		super();
		this.sendToEndpointProcessor = sendToEndpointProcessor;
	}

	@Override

	public void process(Exchange exchange) throws Exception {

		UserRequest body = exchange.getIn().getBody(UserRequest.class);
		exchange.setProperty(USER_REQUEST, body);

		String productName = body.getProductName();

		String fetchAProductByName = String.format("%s%s", FETCH_PRODUCT_BY_NAME_URI, productName);

		sendToEndpointProcessor.sendToEndpoint(fetchAProductByName, exchange);

	}

}
