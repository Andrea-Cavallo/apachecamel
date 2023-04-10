package com.wes.camel.processor.headers;

import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wes.camel.dto.ProductDTO;

import lombok.SneakyThrows;

@Component
public class HeaderPostMapperProcessor implements Processor {

	private final ObjectMapper objectMapper;

	public HeaderPostMapperProcessor(ObjectMapper objectMapper) {
		super();
		this.objectMapper = objectMapper;
	}

	@SneakyThrows
	@Override
	public void process(Exchange exchange) {
		var jsonPayload = exchange.getIn().getBody(String.class);
		List<ProductDTO> products = objectMapper.readValue(jsonPayload, new TypeReference<List<ProductDTO>>() {
		});
		ProductDTO productDTO = products.get(0);

		exchange.setProperty("productFound", productDTO);
		exchange.getIn().removeHeader(Exchange.CONTENT_TYPE);
		setHeader(exchange, Exchange.HTTP_METHOD, "POST");
		setHeader(exchange, Exchange.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

	}

	public static void setHeader(Exchange exchange, String name, Object value) {
		exchange.getIn().removeHeader(name);
		exchange.getIn().setHeader(name, value);
	}

}
