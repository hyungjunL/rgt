package com.rgt.repository;

import com.rgt.dto.OrderDTO;

public interface RgtMapper {

	public int countOrderId(String orderId);
	
	public int insertOrderInfo(OrderDTO orderDTO);

	public int updateProductName();
}
