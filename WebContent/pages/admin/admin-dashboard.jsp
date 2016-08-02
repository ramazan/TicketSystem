<%@ page import="com.j32bit.ticket.bean.User"%>

<!DOCTYPE HTML5>
<html>
  <head>
    <meta charset="utf-8" />

    <title>Ticket System</title>
    <link href="../../css/bootstrap.min.css" rel="stylesheet" />
      <!--     Fonts and icons     -->
    <link href="http://maxcdn.bootstrapcdn.com/font-awesome/4.2.0/css/font-awesome.min.css" rel="stylesheet">

  </head>
     <style>
  body {
	background: url("http://i.istockimg.com/file_thumbview_approve/53484464/3/stock-photo-53484464-light-striped-brown-pattern-repeat-background.jpg");
}

  </style>
  <body>
    <div class="container-fluid">
      <nav class="navbar navbar-default">
        <div class="container-fluid">
          <div class="navbar-header">
            <a class="navbar-brand" href="#">TicketSystem</a>
          </div>
          <div class="collapse navbar-collapse" >
            <ul class="nav navbar-nav">
              <li class="active"><a href="admin-dashboard.html">Dashboard</a></li>
              <li><a href="admin-tickets.html">Tickets</a></li>
              <li><a href="admin-users.html">Users</a></li>
            </ul>
            <ul class="nav navbar-nav navbar-right">
              <li class="dropdown">
                <a href="#" class="dropdown-toggle" data-toggle="dropdown"
                role="button" aria-haspopup="true"
                aria-expanded="false"><span id="userName">UserName</span><span class="caret"></span></a>
                <ul class="dropdown-menu">
                  <li><a href="admin-controlpanel.html">My Preferences</a></li>
                  <li role="separator" class="divider"></li>
                  <li><a href="logout.jsp">Logout</a></li>
                </ul>
              </li>
            </ul>
          </div> <!--navbar collapse -->
        </div><!-- navbar container -->
      </nav>
    </div>

  </body>
  <script type="text/javascript" src="../../js/jquery-3.0.0.min.js"></script>
  <script src="../../js/bootstrap.min.js" type="text/javascript"></script>
  <script>
    <%  
    	User user = (User)session.getAttribute("LOGIN_USER"); 
    	String[] temp = user.getUserRoles();
    	for(String str : temp)
    		out.print(str);
  			//var name = <%= user.getUserRoles()
    %> 
     var arr ="${sessionScope.LOGIN_USER.getUserRoles()[0]}";
     
  
    $("#userName").html(arr);
  </script>
</html>
