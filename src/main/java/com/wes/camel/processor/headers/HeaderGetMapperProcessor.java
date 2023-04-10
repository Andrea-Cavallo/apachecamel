package com.wes.camel.processor.headers;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import lombok.SneakyThrows;

@Component
public class HeaderGetMapperProcessor implements Processor {

	@SneakyThrows
	@Override
	public void process(Exchange exchange) {

		exchange.getIn().removeHeader(Exchange.CONTENT_TYPE);
		setHeader(exchange, Exchange.HTTP_METHOD, "GET");
		setHeader(exchange, Exchange.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

	}

	public static void setHeader(Exchange exchange, String name, Object value) {
		exchange.getIn().removeHeader(name);
		exchange.getIn().setHeader(name, value);
	}

}
