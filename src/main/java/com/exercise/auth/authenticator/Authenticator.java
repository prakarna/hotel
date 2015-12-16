package com.exercise.auth.authenticator;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.security.auth.login.LoginException;

import com.exercise.common.ratelimit.RateLimit;
import com.exercise.common.utils.PropertiesUtils;

public class Authenticator {
	
	public final static String CLIENT_ID = "client_id";
	public final static String AUTH_TOKEN = "auth_token";
	
	private static Authenticator authenticator = null;
    private final Map<String, String> userDetails = new HashMap<String, String>();
    //Each user will have his rate limit.
    private final Map<String, RateLimit> apiKeyRepo = new HashMap<String, RateLimit>();
    private final Map<String, String> clientIdsRepo = new HashMap<String, String>();
    //for keeping the authorization token (per user) after successfully logged in.
    //also, use token as a api key for the rate limit
    private final Map<String, String> tokenRepo = new HashMap<String, String>();  
 
    private Authenticator() {
        initUserDetails();
        initClientIdRepo();
    }
 
    public static Authenticator getInstance() {
        if (authenticator == null) {
            authenticator = new Authenticator();
        }
        return authenticator;
    }
 
    public String login(String clientId, String username, String password) throws LoginException, IOException {
    	String clientInfo = clientIdsRepo.get(clientId);
        if (clientInfo != null && !clientInfo.isEmpty()) {
            if (clientInfo.equals(username)) {
            	String userDetailsPassword = userDetails.get(username);
            	if (userDetailsPassword != null && !userDetailsPassword.isEmpty() && userDetailsPassword.equals(password)) {
            		
            		//prevent duplicate token to be generated
            		if (tokenRepo.containsValue(username)) {
            			for (String k : tokenRepo.keySet()) {
            				if (tokenRepo.get(k).equals(username)) {
            					return k;
            				}
            			}
            		}
            		//store token
            		String token = generateToken();
                    tokenRepo.put(token, username);
                    
                    PropertiesUtils prop = PropertiesUtils.getInstance();
                    //get rate limit per client Id  
                    apiKeyRepo.put(token, new RateLimit(prop.getNumberOfCall(clientId), prop.getTimeWindow(clientId), prop.getSuspenedTime(clientId)));
                    
                    return token;
            	}
            }
        }
        throw new LoginException("Unauthorized");
    }
 
    public boolean isAuthTokenValid(String clientId, String token) {
       
    	String userInfo = clientIdsRepo.get(clientId);
    	if (userInfo != null && !userInfo.trim().isEmpty()) {

    		//tokenInfo is username
    		String tokenInfo = tokenRepo.get(token);
    		if (tokenInfo != null && !tokenInfo.isEmpty()) {
    			
                if (userInfo.equals(tokenInfo)) {
                	return true;
                }
            }
    	}
        return false;
    }
 
    public boolean isClientIdValid(String serviceKey) {
        return clientIdsRepo.containsKey(serviceKey);
    }

    public void logout(String clientId, String token) {
    	String userInfo = clientIdsRepo.get(clientId);
    	if (userInfo != null && !userInfo.trim().isEmpty()) {
    		String tokenInfo = tokenRepo.get(token);
    		if (tokenInfo != null && !tokenInfo.isEmpty()) {
                if (userInfo.equals(tokenInfo)) {
                	tokenRepo.remove(token);
                }
            }
    	}
    }
    
    private String generateToken() {
    	return UUID.randomUUID().toString();
    }
    
    public RateLimit getRateLimit(String token) {
    	return apiKeyRepo.get(token);
    }
    
    private void initUserDetails() {
    	userDetails.put("user1", "password");
        userDetails.put("user2", "password");
        userDetails.put("user3", "password");
    }
    
    private void initClientIdRepo() {
    	//for valid client id (pre-defined)
        clientIdsRepo.put("client_id_1", "user1");
        clientIdsRepo.put("client_id_2", "user2");
        clientIdsRepo.put("client_id_3", "user3");
    }
}
