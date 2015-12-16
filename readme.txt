Environment
- JAVA 8
- Tomcat 7
- JAX-RS (Jersey)
- JSON

Build
mvn clean install eclipse:clean eclipse:eclipse -Pmep -e

Run
After the maven command finishes, please copy the war file in the target folder into the webapp folder in Tomcat 7 and start tomcat.

Enter URL: http://localhost:8080/hotel-rs/

First of all, I would like to say sorry for the ugly UI. I am not a UI person, so I use only servlet and jsp for the sample UI. :(

Alternatively, Advanced Rest Client in Chrome could be used for the testing. (please see API Specification below)

----------------------------------------------------------------------------------
Implementation Details
----------------------------------------------------------------------------------
1. LOGIN

for the sake of testing, there are three pre-defined users as follows:

Username	Password	Client ID
--------------------------------------------
user1		password	client_id_1
user2		password	client_id_2
user3		password	client_id_3

I have implemented the simple token service, which has token, user, clientId, apiKey repositories.

The user and clientId are pre-defined for the exercise purposes.

The client id is a pre-defined value used to indicate that the client who is going to use the system is a authorized client. 
It will be put into the HTTP header and send to the login service along with username and password.
If the client id is invalid or username or password is incorrect, HTTP Status 401 will be responded by the server.
(For the testing purpose, user has to input client id manually. In reality, it will be sent/configured in the application and the users will not know it)

The token repository is used for keeping the authorizarion token generated per each user.
Once the user has successfully logged in, the server will return an authorizarion token, which will be used in the rest of the communication to ensure that the user has been authenticated and is authorized. 

The authorization token is also used as an API Key to separate the rate limit for each client, which will be stored in apiKey repository.

By default, the rate limit is 1 request per 10 seconds, and if exceeded, the client will not be able to call the service for 30 seconds.

Even though, the exercise needs to suspend for 5 minutes, but for the testing, waiting 5 minutes may be too long, so I reduce it to 30 seconds.

However, the rate limit value for global and per client can be configured in the properties file resides in /WEB-INF/ratelimit.properties.

The rate limit is implemented using simple Token Bucket algorithm.

The pre-defined values in the properties file are as follows:

User	Number of Calls	  Time Window (second)	Suspended TIme (second)
-----------------------------------------------------------------------
user1	1		  10			30
user2	2		  10			15
user3	5		  20			60


2. SEARCH

In order to call the hotel search service, token will be put into the HTTP header.
Once the request has arrived at the server, the container filter will intercept it and validate the token and the rate limit.
If the token is invalid, the unauthorized page will display.
If the rate limit is exceeded, the rate limit exceeded page will display and the user has to wait until the suspended period ends.
Otherwise; the search result will display.

For the sort algorithm, I have not implemented anything new, but used Collection.sort(), which is already O(nlogn) time.
For the searching, when the first request arrived, the CSV file will be read and the records will be organized in the HashMap, by using City as a key.
Thus, the next searching, will only retrieve the list of records by using a key and sort the list according to the sort criteria (ASC, DESC).

3. LOGOUT

After the logout is triggered, the token stored in the token repository will be removed. 
And if the user tries to call the search hotel service, HTTP status 401 UnAuthorized will be returned.

----------------------------------------------------------------------------------
API specification
----------------------------------------------------------------------------------

API for REST services

1. login(String client_id, String username, String password)

URL: http://localhost:8080/hotel-rs/auth/services/login
HTTP Method: POST
HTTP Request Header: 
- client_id
HTTP Request Body:	
- username
- password

Response:		
- authorization token in Json format, if client_id, username and password are valid
Example
{
	auth_token: "93d06076-866b-49d7-b629-ac09de4a0809"
}
- HTTP Status 401 Unauthorized, if client_id or username or password are invalid

--------------------------------------------------------------------------

2. search(String city, String sort)

URL: http://localhost:8080/hotel-rs/rest/services/hotel
HTTP Method: POST
HTTP Request Header:
- client_id
- auth_token
HTTP Request Body:	
- city
- sort (Optional)

Response:		
- List of the hotels in Json format, if hotel found.
- blank Json, if hotel not found.
- HTTP Status 400 Bad Request, if city is not provided.

---------------------------------------------------------------------------

3. logout()

URL: http://localhost:8080/hotel-rs/auth/services/logout
HTTP Method: POST
HTTP Request Header:
- client_id
- auth_token

Response:
- http status 200 OK
--------------------------------------------------------------------------


