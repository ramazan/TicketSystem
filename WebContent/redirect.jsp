<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.j32bit.ticket.bean.User"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Redirect</title>
</head>
<body>


<%
	String id = request.getRemoteUser();

	response.setHeader("Refresh", "1;url=./rest/session/login?email="+id);

	/*

	if(request.isUserInRole("admin"))
		response.setHeader("Refresh", "1;url=./pages/admin/admin-dashboard.html");
	else if(request.isUserInRole("supporter"))
		response.setHeader("Refresh", "2;url=./pages/supporter/supporter-dashboard.html");
	else if(request.isUserInRole("client"))
		response.setHeader("Refresh", "2;url=./pages/client/client-dashboard.html");*/
%>

</body>
</html>
