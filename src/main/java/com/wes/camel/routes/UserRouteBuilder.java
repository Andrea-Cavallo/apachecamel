package com.wes.camel.routes;

import static com.wes.camel.utils.Constants.CHECK_AVAILABILITY;
import static com.wes.camel.utils.Constants.DIRECT_CREATE_USER;
import static com.wes.camel.utils.Constants.FIRST_LOG;
import static com.wes.camel.utils.Constants.ORCHESTRATOR_API;
import static com.wes.camel.utils.Constants.ORDER_BY_PRODUCT_NAME_AND_USERNAME;
import static com.wes.camel.utils.Constants.ORDER_CREATED_IS_$_BODY;
import static com.wes.camel.utils.Constants.ORDER_REFUSED_PRODUCT_IS_NOT_IN_STOCK;
import static com.wes.camel.utils.Constants.ORDER_TO_PROCESS_IS_$_BODY;
import static com.wes.camel.utils.Constants.POST_DESCRIPTION;
import static com.wes.camel.utils.Constants.PRETTY_PRINT;
import static com.wes.camel.utils.Constants.PROCESSING_ORDER;
import static com.wes.camel.utils.Constants.REST_DESCRIPTION;
import static com.wes.camel.utils.Constants.START_OF_ROUTE;
import static com.wes.camel.utils.Constants.TRUE;
import static com.wes.camel.utils.Constants.UNDERTOW;
import static com.wes.camel.utils.Constants.WORKER_THREADS;
import static com.wes.camel.utils.Constants._50;

import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import com.wes.camel.dto.request.UserRequest;
import com.wes.camel.dto.response.RestResponse;
import com.wes.camel.processor.CreateOrderProcessor;
import com.wes.camel.processor.GetProductInfoProcessor;
import com.wes.camel.processor.ProcessOrderProcessor;
import com.wes.camel.processor.ResponseProcessor;
import com.wes.camel.processor.headers.HeaderGetMapperProcessor;
import com.wes.camel.processor.headers.HeaderPostMapperProcessor;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserRouteBuilder extends RouteBuilder {


	private final ProcessOrderProcessor bodyRequestProcessor;
	private final HeaderPostMapperProcessor headerPostMapperProcessor;
	private final GetProductInfoProcessor getProductInfoProcessor;
	private final HeaderGetMapperProcessor headerGetMapperProcessor;
   	private final CreateOrderProcessor createOrderProcessor;
	private final ResponseProcessor responseProcessor;

	@Override
	public void configure() throws Exception {
		 
		onException(Exception.class)
         .handled(true)
         .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(500))
         .process(new Processor() {
             public void process(Exchange exchange) throws Exception {
                 Exception e = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class);
                 String message = e.getMessage();
                 exchange.getIn().setBody(message);
             }
         });
		
		
		restConfiguration()
		  .component(UNDERTOW)
		  .bindingMode(RestBindingMode.json)
		  .dataFormatProperty(PRETTY_PRINT, TRUE)
		  .enableCORS(true)
		  .contextPath(ORCHESTRATOR_API)
		  .port(8081)
		  .endpointProperty(WORKER_THREADS, _50);

		rest(ORDER_BY_PRODUCT_NAME_AND_USERNAME)
			.description(REST_DESCRIPTION)
			.consumes(MediaType.APPLICATION_JSON_VALUE)
			.produces(MediaType.APPLICATION_JSON_VALUE)
			.post()
			.description(POST_DESCRIPTION)
			.type(UserRequest.class)
			.outType(RestResponse.class)
			.responseMessage()
			.code(HttpStatus.OK.value())
			.endResponseMessage()
				.to(DIRECT_CREATE_USER);

		from(DIRECT_CREATE_USER)
			.log(LoggingLevel.INFO, START_OF_ROUTE)
			.log(LoggingLevel.INFO, FIRST_LOG)
			.process(headerGetMapperProcessor)
			.process(getProductInfoProcessor)
			.process(headerPostMapperProcessor)
			.process(bodyRequestProcessor)
			.log(LoggingLevel.INFO, ORDER_TO_PROCESS_IS_$_BODY)
			.choice()
		        .when().simple(CHECK_AVAILABILITY)
		            .log(PROCESSING_ORDER)
		            .process(createOrderProcessor)
		            .log(LoggingLevel.INFO, ORDER_CREATED_IS_$_BODY)
		            .process(responseProcessor)
		            .endChoice()
		        .otherwise()
		            .log(LoggingLevel.INFO,ORDER_REFUSED_PRODUCT_IS_NOT_IN_STOCK)
		            .process(createOrderProcessor)
		            .endChoice()
	         .end();

	}

	
}
