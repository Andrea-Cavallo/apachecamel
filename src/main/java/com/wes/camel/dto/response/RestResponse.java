package com.wes.camel.dto.response;

import java.util.List;

import com.wes.camel.dto.OrderDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestResponse {

	private OrderDTO output;
	private List<String> errorMessages;

}
