<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<html>
<head>
<title>Search Hotel</title>
<style type="text/css">
body {
	font-family:verdana,arial,sans-serif;
	font-size:10pt;
	margin:10px;
	background-color:#ffffff;
}
</style>
<%
	String auth_token = (String) request.getAttribute("auth_token");
	String client_id = (String) request.getAttribute("client_id");
	String result = (String) request.getAttribute("result");
	if (result == null) result = "";
	String city = (String) request.getAttribute("city");
	if (city == null) city = "";
	String sortType = (String) request.getAttribute("sortType");
	if (sortType == null) sortType = "";
	boolean asc = false;
	boolean desc = false;
	if (sortType.equalsIgnoreCase("ASC")) asc=true;
	if (sortType.equalsIgnoreCase("DESC")) {
		desc=true;
		System.out.println(desc);
	}
%>
<script language="javaScript">
	function searchClick() {
		var username = document.getElementById("txtCity").value; 
		if (username.trim() == '') {
			alert("city is required")
			document.getElementById("txtCity").value = '';
			return;
		}
		document.getElementById("ACTION").value = 'SEARCH';
		document.forms["formSearch"].submit();
	}
	function logout() {
		document.getElementById("ACTION").value = 'LOGOUT';
		document.forms["formSearch"].submit();
	}
	function load() {
		var radioCheck = document.getElementById("hidSort").value;
		if (radioCheck == 'DESC') {
			document.getElementById("DESC").checked=true;
		} else if (radioCheck == 'ASC') {
			document.getElementById("ASC").checked=true;
		}
	}
</script>
</head>
<body onLoad="load()">
<form method="post" action="hotel" id="formSearch">
<input name="hidToken" id="hidToken" type="hidden" value="<%=auth_token%>">
<input name="hidClientId" id="hidClientId" type="hidden" value="<%=client_id%>">
<input name="hidSort" id="hidSort" type="hidden" value="<%=sortType%>">
<input name="ACTION" id="ACTION" type="hidden" value="">
<table border="0">
	<tr>
		<td width="50" align="left">City<td>
		<td width="150" align="left"><input type="text" name="txtCity" id="txtCity" value="<%=city%>"></td>
		<td width="200" align="left">
			<input type="radio" name="radioSort" id="ASC" value="ASC" />
			<label for="ASC">Sort price by ASC</label>
		</td>
		<td width="200" align="left">
			<input type="radio" name="radioSort" id="DESC" value="DESC" />
			<label for="DESC">Sort price by DESC</label>
		</td>
		<td align="left"><input type="button" value="search" onclick="searchClick()"></td>
	</tr>
</table>
<div>
City eg. Bangkok, Amsterdam, Ashburn
</div>
<div>
By default, the sort is in ascending order
</div>
</br>
<div style="font-weight: bold;">
Note:
</div>
<div style="font-weight: bold;">
In order to call the hotel search service, token will be put into the HTTP header.
</div>
<div style="font-weight: bold;">
Once the request has arrived at the server, the container filter will intercept it and validate the token and the rate limit.
</div>
<div style="font-weight: bold;">
If the token is invalid, the unauthorized page will display.
</div>
<div style="font-weight: bold;">
If the rate limit is exceeded, the rate limit exceeded page will display and the user has to wait until the suspended period ends.
</div>
</br>
<input type="button" value="logout" onclick="logout()">
<div style="font-weight: bold;">RESULT</div>
<div>
<%=result %>
</div>
</br>
</br>
</form>
</body>
</html>