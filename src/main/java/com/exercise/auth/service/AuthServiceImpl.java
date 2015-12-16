package com.exercise.auth.service;

import java.io.IOException;

import javax.security.auth.login.LoginException;
import javax.ws.rs.Path;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import net.sf.json.JSONObject;

import com.exercise.auth.authenticator.Authenticator;

@Path("/services")
public class AuthServiceImpl implements AuthService {
	
	@Override
	public Response login(HttpHeaders httpHeaders, String username, String password) {
		Authenticator authenticator = Authenticator.getInstance();
        String apiKey = httpHeaders.getHeaderString(Authenticator.CLIENT_ID);
        try {
            String authToken = authenticator.login(apiKey, username, password);
            JSONObject jsonObj = new JSONObject();
            jsonObj.put(Authenticator.AUTH_TOKEN, authToken);
            return Response.status(200).entity(jsonObj.toString()).build();
        } catch (LoginException e) {
        	return Response.status(401).build(); 
        } catch (IOException e) {
        	return Response.status(500).build(); 
        }
	}

	@Override
	public Response logout(HttpHeaders httpHeaders) {
		Authenticator authenticator = Authenticator.getInstance();
		String clientId = httpHeaders.getHeaderString(Authenticator.CLIENT_ID);
		String token = httpHeaders.getHeaderString(Authenticator.AUTH_TOKEN);
		authenticator.logout(clientId, token);
		return Response.status(200).build(); 
	}

}
