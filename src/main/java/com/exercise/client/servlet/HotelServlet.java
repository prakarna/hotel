package com.exercise.client.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

public class HotelServlet extends HttpServlet{

	private static final long serialVersionUID = 7117233032137408239L;
	private static final String HOST_URL = "http://localhost:8080";
	private static final String HOTEL_SERVICE_URL = "/rest/services/hotel";
	private static final String LOGOUT_SERVICE_URL = "/auth/services/logout";
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        Client client = ClientBuilder.newClient();

		String clientId = request.getParameter("hidClientId");
        String token = request.getParameter("hidToken");
        String city = request.getParameter("txtCity");
        String sort = request.getParameter("radioSort");
        
        String action = request.getParameter("ACTION");
        if (action.equalsIgnoreCase("LOGOUT")) {
        	WebTarget target = client.target(HOST_URL + request.getContextPath() + LOGOUT_SERVICE_URL);
        	Response serviceResponse = target
        			.request()
        			.header("client_id", clientId)
        			.header("auth_token", token)
        			.post(Entity.entity(null, MediaType.APPLICATION_FORM_URLENCODED));
        	out.println("<H2>LOGGED OUT</H2>");
        	return;
        } else {
        
	        WebTarget target = client.target(HOST_URL + request.getContextPath() + HOTEL_SERVICE_URL);
	        
	        Form form = new Form();
			form.param("city", city);
			form.param("sort", sort);
			
			Response serviceResponse = target
			.request()
			.header("client_id", clientId)
			.header("auth_token", token)
			.post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED));
			
			request.setAttribute("auth_token", token);
			request.setAttribute("client_id", clientId);
			request.setAttribute("city", city);
			request.setAttribute("sortType", sort);
			
			int statusCode = serviceResponse.getStatus();
			if (statusCode == Response.Status.OK.getStatusCode()) {
				JSONObject jsonOutput = (JSONObject) JSONSerializer.toJSON(serviceResponse.readEntity(String.class));
				RequestDispatcher rs = request.getRequestDispatcher("/secured/search.jsp");
				request.setAttribute("result", jsonOutput.toString());
				rs.include(request, response);
			} else if (statusCode == 429) {
				out.println("<H2>Rate limit exceeded</H2>");
			} else if (statusCode == Response.Status.UNAUTHORIZED.getStatusCode()) {
				out.println("<H2>Unauthorized</H2>");
			} else {
				RequestDispatcher rs = request.getRequestDispatcher("/secured/search.jsp");
				request.setAttribute("result", "");
				rs.include(request, response);
			}
        }
	}
}
