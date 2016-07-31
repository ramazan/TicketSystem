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
	
	

		
		
	</div>
	</html>