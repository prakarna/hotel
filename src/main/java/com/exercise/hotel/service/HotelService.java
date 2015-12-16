package com.exercise.hotel.service;

import java.io.Serializable;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public interface HotelService extends Serializable {
	
	@POST
	@Path("/hotel")
	@Produces({MediaType.APPLICATION_JSON})
	public Response searchByCity(@FormParam("city") String city, @FormParam("sort") String sort);
}
