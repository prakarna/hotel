package com.exercise.hotel.model;

import java.io.Serializable;


public class Hotel implements Comparable<Hotel>, Serializable  {
	private static final long serialVersionUID = -719379119647982830L;
	
	private String city;
	private int hotelId;
	private String room;
	private double price;
	
	public Hotel() {
	}
	
	public Hotel(String city, int hotelId, String room, double price) {
		this.city = city;
		this.hotelId = hotelId;
		this.room = room;
		this.price = price;
	}
	
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public int getHotelId() {
		return hotelId;
	}
	public void setHotelId(int hotelId) {
		this.hotelId = hotelId;
	}
	public String getRoom() {
		return room;
	}
	public void setRoom(String room) {
		this.room = room;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}

	@Override
	public int compareTo(Hotel o) {
		return (this.price > o.price)?1:-1;
	}

}

