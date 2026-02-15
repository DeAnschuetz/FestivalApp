package com.ffb.model.response;

import java.util.List;

import com.ffb.model.response.order.OrderSimple;

public class OrderResponse {

	private int status;
	private String message;
	private List<OrderSimple> data;

	public OrderResponse(int status, String message, List<OrderSimple> data) {
		super();
		this.status = status;
		this.message = message;
		this.data = data;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<OrderSimple> getData() {
		return data;
	}

	public void setData(List<OrderSimple> data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "OrderResponse [status=" + status + ", message=" + message + ", data=" + data + "]";
	}

}
