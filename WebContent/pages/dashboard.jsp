<%@ page import="com.j32bit.ticket.bean.User"%>

<!DOCTYPE HTML5>
<html>
  <head>
    <meta charset="utf-8" />

    <title>Ticket System</title>

    <link rel="stylesheet" type="text/css" href="../css/bootstrap.min.css" />
    <!-- JAVASCRIPT SOURCES -->
    <script type="text/javascript" src="../js/jquery-3.1.0.min.js"></script>
    <script type="text/javascript" src="../js/bootstrap.min.js"></script>


  </head>

  <body>
    <div class="container-fluid">
      <nav class="navbar navbar-default">
          <div class="navbar-header">
            <a class="navbar-brand" href="#">TicketSystem</a>
          </div>
          <div class="collapse navbar-collapse" >
            <ul class="nav navbar-nav">
              <li class="active"><a href="dashboard.jsp">Dashboard</a></li>
              <li><a href="tickets.html">Tickets</a></li>
              <li><a href="users.html">Users</a></li>
            </ul>
            <ul class="nav navbar-nav navbar-right">
              <li class="dropdown">
                <a href="#" class="dropdown-toggle" data-toggle="dropdown"
                role="button" aria-haspopup="true"
                aria-expanded="false"><span id="userName">UserName</span><span class="caret"></span></a>
                <ul class="dropdown-menu">
                  <li><a href="profile.html">My Preferences</a></li>
                  <li role="separator" class="divider"></li>
                  <li><a href="logout.jsp">Logout</a></li>
                </ul>
              </li>
            </ul>
          </div> <!--navbar collapse -->
      </nav>
    </div>

  </body>

  <script>
    <%
    /*	User user = (User)session.getAttribute("LOGIN_USER");
    	String[] temp = user.getUserRoles();
    	for(String str : temp)
    		out.print(str);
  			//var name = <%= user.getUserRoles()
    */%>
     var arr ="${sessionScope.LOGIN_USER.getUserRoles()[0]}";


    $("#userName").html(arr);
  </script>
</html>
