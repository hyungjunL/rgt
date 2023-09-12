package com.rgt.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class OrderDTO {
	@JsonProperty("order_id")
	private String orderId;
	
	@JsonProperty("product_name")
	private String productName;
	
	@JsonProperty("options")
	private String options;
	
	@JsonProperty("table_no")
	private String tableNo;
	
	@JsonProperty("quantity")
	private String quantity;
	
	@JsonProperty("order_date")
	private String orderDate;
	
	@JsonProperty("order_time")
	private String orderTime;

	@JsonProperty("date_time")
	private String dateTime;
	
	@JsonProperty("robot_status")
	private String robotStatus;
	
	@JsonProperty("dong")
	private String dong;
	
	@JsonProperty("ho")
	private String ho;
	
	@JsonProperty("seq")
	private String seq;
	
	@JsonProperty("orderer_name")
	private String ordererName;
}
