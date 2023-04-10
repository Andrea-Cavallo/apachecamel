package com.wes.camel.utils;

public class Constants {

	private Constants() {
	}

	public static final String POST_DESCRIPTION = "given an user name, a surname a name of a product it create an order if the product is in stock, Otherwise the order will be rejected";
	public static final String CREATE_USER_ROUTE = "createUserRoute";
	public static final String DIRECT_CREATE_USER = "direct:createUser";
	public static final String REST_DESCRIPTION = "An example of orchestration made with Apache Camel";
	public static final String ORDER_BY_PRODUCT_NAME_AND_USERNAME = "/orders";
	public static final String ORCHESTRATOR_API = "/orchestrator";
	public static final String TRUE = "true";
	public static final String PRETTY_PRINT = "prettyPrint";
	public static final String JETTY = "jetty";
	public static final String USER_REQUEST = "userRequest";
	public static final String FETCH_PRODUCT_BY_NAME_URI = "http://localhost:8082/products?bridgeEndpoint=true&productName=";
	public static final String CREATE_ORDER = "http://localhost:8082/orders?bridgeEndpoint=true";
	public static final String _50 = "50";
	public static final String WORKER_THREADS = "worker-threads";
	public static final String UNDERTOW = "undertow";
	public static final String CHECK_AVAILABILITY = "${body.orderStatus} == 'PROCESSING'";
	public static final String ORDER_REFUSED_PRODUCT_IS_NOT_IN_STOCK = "Order refused, product is not in stock!";
	public static final String PROCESSING_ORDER = "Processing order.....";
	public static final String ORDER_CREATED_IS_$_BODY = "Order created is : ${body}";
	public static final String ORDER_TO_PROCESS_IS_$_BODY = "Order to process is : ${body}";
	public static final String FIRST_LOG = "User Request received: ${headers}, ${body}";
	public static final String START_OF_ROUTE = "**** START OF ROUTE *****";
}
