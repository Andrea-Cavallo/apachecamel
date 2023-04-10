package com.wes.camel.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Builder(toBuilder = true)
@Getter
@EqualsAndHashCode
@NoArgsConstructor
@Setter
@ToString

public class OrderDTO {
	
	private OrderStatus orderStatus;

	private OrderInfo orderInfo;

	private UserDTO userInfo;

	private ProductDTO productInfo;

	@AllArgsConstructor
	@Getter
	@EqualsAndHashCode
	@NoArgsConstructor
	@Setter
	@ToString
	public class OrderInfo {
		private String orderId;
		private String orderedAt;
		private String deliveredAt;

	}
	public enum OrderStatus {
        PROCESSING, REFUSED, CREATED
    }
}
