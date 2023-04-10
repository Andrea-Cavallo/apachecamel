package com.wes.camel.processor;

import java.io.IOException;

import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.springframework.stereotype.Component;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class SendToEndpointProcessor {

	/**
	 * Sends a message to the specified endpoint URI and returns the result.
	 *
	 * @param endpointURI the URI of the endpoint to send the message to
	 * @param body        the body of the message to send, or null if the message
	 *                    has no body
	 * @return the result of sending the message, or null if no result was received
	 * @throws IOException
	 * @throws Exception   if an error occurs while sending the message,
	 *                     SneakyThrows is for the IO Exceptions
	 */

	@SneakyThrows
	public void sendToEndpoint(String endpointURI, Exchange exchange) {
		try (ProducerTemplate createProducerTemplate = exchange.getContext().createProducerTemplate()) {
			logEndpoint(endpointURI);
			Exchange send = createProducerTemplate.send(endpointURI, exchange);
			exchange.getIn().setBody(send.getIn().getBody());
			exchange.getIn().removeHeaders("*");

		}
	}

	private void logEndpoint(String endpointURI) {
		log.info("Chiamo l'endpoint: {}", endpointURI);
	}

}