package com.wes.camel.processor;

import static com.wes.camel.utils.Constants.USER_REQUEST;

import java.time.LocalDateTime;
import java.util.UUID;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.wes.camel.dto.OrderDTO;
import com.wes.camel.dto.ProductDTO;
import com.wes.camel.dto.UserDTO;
import com.wes.camel.dto.request.UserRequest;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ProcessOrderProcessor implements Processor {

	@Override
	public void process(Exchange exchange) throws JsonProcessingException {
		UserRequest userRequest = exchange.getProperty(USER_REQUEST, UserRequest.class);
		ProductDTO product = exchange.getProperty("productFound", ProductDTO.class);
		log.info("Received body of UserRequest: {}", userRequest);

		UserDTO userDTO = createUserDTO(userRequest);
		OrderDTO orderDTO = createOrderDTO(userDTO, product, userRequest);

		exchange.getIn().setBody(orderDTO);
		exchange.setProperty("orderInput", orderDTO);
		log.info("Transformed body into orderDto: {}", orderDTO);
	}

	private UserDTO createUserDTO(UserRequest userRequest) {
		String name = userRequest.getFirstName().toLowerCase();
		String surname = userRequest.getLastName().toLowerCase();

		UserDTO userDTO = new UserDTO();
		userDTO.setFirstName(name);
		userDTO.setLastName(surname);
		String email = String.format("%s.%s@protonmail.com", name, surname);
		userDTO.setEmail(email);

		return userDTO;
	}

	private OrderDTO createOrderDTO(UserDTO userDTO, ProductDTO productDTO, UserRequest userRequest) {
		if (userDTO == null || productDTO == null || userRequest == null) {
			return null;
		}

		OrderDTO orderDTO = new OrderDTO();
		OrderDTO.OrderInfo orderInfo = orderDTO.new OrderInfo();
		String randomUUId = UUID.randomUUID().toString();

		ProductDTO product = new ProductDTO();
		product.setProductId(productDTO.getProductId());
		product.setIsInStock(productDTO.getIsInStock());
		product.setProductName(productDTO.getProductName());
		product.setPrice(productDTO.getPrice());
		product.setQuantity(Math.max(0, productDTO.getQuantity() - userRequest.getQuantity()));
		product.setIsInStock(product.getQuantity() > 0);

		orderDTO.setUserInfo(userDTO);
		orderDTO.setProductInfo(product);

		orderInfo.setOrderId(randomUUId);
		orderInfo.setOrderedAt(LocalDateTime.now().toString());
		orderInfo.setDeliveredAt(LocalDateTime.now().plusDays(15).toString());

		if (product.getIsInStock()) {
			orderDTO.setOrderStatus(OrderDTO.OrderStatus.PROCESSING);
		} else {
			orderDTO.setOrderStatus(OrderDTO.OrderStatus.REFUSED);
		}

		orderDTO.setOrderInfo(orderInfo);
		return orderDTO;
	}

}