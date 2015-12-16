package com.exercise.client.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.sun.research.ws.wadl.Request;

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

public class LoginServlet extends HttpServlet {

	private static final long serialVersionUID = -181332670774870431L;
	private static final String HOST_URL = "http://localhost:8080";
	private static final String LOGIN_SERVICE_URL = "/auth/services/login";
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
		response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        String clientId = request.getParameter("txtClientId");
        String username = request.getParameter("txtUsername");
        String password = request.getParameter("txtPassword");
        
        Client client = ClientBuilder.newClient();
        
		WebTarget target = client.target(HOST_URL + request.getContextPath() + LOGIN_SERVICE_URL);
		Form form = new Form();
		form.param("username", username);
		form.param("password", password);
		
		Response serviceResponse = target
		.request()
		.header("client_id", clientId)
		.post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED));
		
		int statusCode = serviceResponse.getStatus();
		if (statusCode == Response.Status.OK.getStatusCode()) {
			JSONObject jsonOutput = (JSONObject) JSONSerializer.toJSON(serviceResponse.readEntity(String.class));
			//out.println("token received: " + jsonOutput.getString("auth_token"));
			RequestDispatcher rs = request.getRequestDispatcher("/secured/search.jsp");
			request.setAttribute("auth_token", jsonOutput.getString("auth_token"));
			request.setAttribute("client_id", clientId);
			rs.include(request, response);
		} else {
			out.println("Username or Password incorrect");
			//out.println(HOST_URL + request.getContextPath() + LOGIN_SERVICE_URL);
			RequestDispatcher rs = request.getRequestDispatcher("index.jsp");
			rs.include(request, response);
		}
	}
}
