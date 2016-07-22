<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Redirect</title>
</head>
<body>

<h1>Welcome : <%=request.getRemoteUser()%> </h1>
<h1> Redirecting... </h1>

<%

	if(request.isUserInRole("admin"))
		 response.setHeader("Refresh", "5;url=./rest/admin/ControlPanel.html");	
	else if(request.isUserInRole("supporter"))
		response.sendRedirect("./rest/supporter/ControlPanel.html");
	else if(request.isUserInRole("client"))
		response.sendRedirect("./rest/client/ControlPanel.html");

%>

</body>
</html>