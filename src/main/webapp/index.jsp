<html>
<head>
<title>login form</title>
<style type="text/css">
body {
	font-family:verdana,arial,sans-serif;
	font-size:10pt;
	margin:10px;
	background-color:#ffffff;
}
</style>
<script language="javaScript">
	function login() {
		var username = document.getElementById("txtUsername").value; 
		if (username.trim() == '') {
			alert("Username is required")
			document.getElementById("txtUsername").value = '';
			return;
		}
		var password = document.getElementById("txtPassword").value; 
		if (password.trim() == '') {
			alert("Password is required")
			document.getElementById("txtPassword").value = '';
			return;
		}
		document.forms["formLogin"].submit();
	}
</script>
</head>
<body>
<form method="post" action="login" id="formLogin">
	<table>
		<tr>
	    	<td width="80px" align="left">Username:</td>
	    	<td width="120px" align="left"><input type="text" id="txtUsername" name="txtUsername" /></td>
	    </tr>
	    <tr>
	    	<td width="80px" align="left">Password:</td>
	    	<td width="120px" align="left"><input type="password" id="txtPassword" name="txtPassword"/></td>
	    </tr>
	    <tr>
	    	<td width="80px" align="left">Client ID:</td>
	    	<td width="120px" align="left"><input type="text" id="txtClientId" name="txtClientId"/></td>
	    </tr>
	    <tr>
	    	<td width="100px">&nbsp;</td>
			<td><input type="button" value="login" onclick="login()"></td>
	    </tr>
	</table>
    <br/>
    <div style="font-weight: bold">Note:</div>
    <div style="font-weight: bold">
    for the sake of testing, there are three users as follows:
    </div>
    </br>
    <table border="1">
	    <tr>
	    	<td width="120px" align="center">Username</td>
	    	<td width="120px" align="center">Password</td>
	    	<td width="120px" align="center">Client ID</td>
	    </tr>
	    <tr>
	    	<td>user1</td>
	    	<td>password</td>
	    	<td>client_id_1</td>
	    </tr>
	    <tr>
	    	<td>user2</td>
	    	<td>password</td>
	    	<td>client_id_2</td>
	    </tr>
	    <tr>
	    	<td>user3</td>
	    	<td>password</td>
	    	<td>client_id_3</td>
	    </tr>
    </table>
    </br>
    <div style="font-weight: bold">
    The client id is a pre-defined value used to indicate that the client who is going to use the system
    </div>
    <div style="font-weight: bold">
    is a authorized client. It will be put into the HTTP header and send to the login service along with username and password.
    </div>
    <div style="font-weight: bold">
    If the client id is invalid or username or password is incorrect, HTTP Status 401 will be responded by the server.
    </div>
    <div style="font-weight: bold">
    For the testing purpose, user has to input client id manually. 
    </div>
    </br>
    <div style="font-weight: bold">
    Once the user has successfully logged in, the server will return an authorizarion token, which will be used  
    </div>
    <div style="font-weight: bold">
    in the rest of the communication to ensure that the user has been authenticated and is authorized.
    </div>
    </br>
    <div style="font-weight: bold">
    The authorization token is also used as an API Key to separate the rate limit for each user.
    </div>
    <div style="font-weight: bold">
    By default, the rate limit is 1 request per 10 seconds, and if exceeded, the client will not be able to
    </div>
    <div style="font-weight: bold">
    call the service for 30 seconds.
    </div>
    </br>
    <div style="font-weight: bold">
    The rate limit for each user can be configured via the properties file.
    </div>
    </br>
    <div style="font-weight: bold;">
	The rate limit is implemented using simple Token Bucket algorithm.
	</div>
	</br>
    <div style="font-weight: bold">
    The pre-defined values in the properties are as follows:
    </div>
    <table border="1">
	    <tr>
	    	<td width="120px" align="center">User</td>
	    	<td width="120px" align="center">Number of Calls</td>
	    	<td width="120px" align="center">Time Window (second)</td>
	    	<td width="120px" align="center">Suspended TIme (second)</td>
	    </tr>
	    <tr>
	    	<td>user1</td>
	    	<td>1</td>
	    	<td>10</td>
	    	<td>30</td>
	    </tr>
	    <tr>
	    	<td>user2</td>
	    	<td>2</td>
	    	<td>10</td>
	    	<td>15</td>
	    </tr>
	    <tr>
	    	<td>user3</td>
	    	<td>5</td>
	    	<td>20</td>
	    	<td>60</td>
	    </tr>
    </table>
</form>
</body>
</html>