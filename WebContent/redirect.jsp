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
	String email = request.getRemoteUser();
	response.setHeader("Refresh", "1;url=/Ticket_System/rest/session/login?email="+email);
%>
</body>
</html>
