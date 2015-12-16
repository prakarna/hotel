package com.exercise.common.fliter;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import com.exercise.auth.authenticator.Authenticator;
import com.exercise.common.ratelimit.RateLimit;

@Provider
@PreMatching
public class ServiceRequestFilter implements ContainerRequestFilter {

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		Authenticator authenticator = Authenticator.getInstance();
		String clientId = requestContext.getHeaderString(Authenticator.CLIENT_ID);
		
		//Validate clientId & token
		if (!authenticator.isClientIdValid(clientId)) {
			requestContext.abortWith(Response.status(401).build());
			return;
		}
		
		String token = requestContext.getHeaderString(Authenticator.AUTH_TOKEN);
		if (!requestContext.getUriInfo().getPath().contains("/login")) {
			if (!authenticator.isAuthTokenValid(clientId, token)) {
				requestContext.abortWith(Response.status(401).build());
				return;
			}
		}
		
		if (requestContext.getUriInfo().getPath().contains("/hotel")) {
			RateLimit limit = authenticator.getRateLimit(token);
			if (limit.getSuspendedFlag()) {
				if (limit.isSuspended()) {
					requestContext.abortWith(Response.status(429).build());
					return;
				}
			} else {
				if (limit.isExceeded()) {
					//Too many request
					requestContext.abortWith(Response.status(429).build());
					return;
				}
			}
		}
	}

}
