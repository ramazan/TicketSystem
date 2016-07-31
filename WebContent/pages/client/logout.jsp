<html>
<head>
	<link rel="stylesheet" type="text/css" href="main.css">

</head>
<body>

	<div id="sayfa" align="center">

		'<%=request.getRemoteUser()%>' logged out.

		<%
		session.invalidate();
	%>
	
		<a href="http://localhost:8080/Ticket_System">Click Here to Login</a>

		
		
	</div>
	</html>