package com.exercise.auth.service;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public interface AuthService {
	@POST
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(@Context HttpHeaders httpHeaders, @FormParam("username") String username, @FormParam("password") String password);
	
	@POST
    @Path( "logout" )
    public Response logout(@Context HttpHeaders httpHeaders);
}
