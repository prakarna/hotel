package com.exercise.hotel.model;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

//@XmlRootElement
@JsonInclude(Include.NON_EMPTY)
@JsonRootName("HotelResult")
public class HotelResult implements Serializable {

	private static final long serialVersionUID = -4258777367398961321L;
	private List<Hotel> list;
	
	public HotelResult() {
	}
	
	//@XmlElement(name="hotel")
	@JsonProperty("hotel")
	public List<Hotel> getList() {
		return list;
	}
	
	public void setList(List<Hotel> list) {
		this.list = list;
	}
	
}
