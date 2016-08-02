<!DOCTYPE HTML5>
<html>
  <head>
    <meta charset="utf-8" />

    <title>Ticket System</title>
    <link rel="stylesheet" type="text/css" href="../css/bootstrap.min.css" />
    <link rel="stylesheet" type="text/css" href="../css/ui.jqgrid-bootstrap.css"/>
    <!-- JAVASCRIPT SOURCES -->
    <script type="text/javascript" src="../js/jquery-3.1.0.min.js"></script>
    <script type="text/javascript" src="../js/bootstrap.min.js"></script>
    <script type="text/javascript" src="../js/admin.js"></script>

    <style>
      form {
        background: #B7AFA3;
        width: 350px;
        background-position:center;
        padding: 40px 30px;
        margin-top: 75px;
        margin-left: 250px;
        border-radius: 10px;
      }
    </style>
  </head>

  <body>
    <div class="container-fluid">
      <nav class="navbar navbar-default">
        <div class="navbar-header">
          <a class="navbar-brand" href="dashboard.jsp">TicketSystem</a>
        </div>
        <div class="collapse navbar-collapse" >
          <ul class="nav navbar-nav">
            <li><a href="dashboard.jsp">Dashboard</a></li>
            <li><a href="tickets.jsp">Tickets</a></li>
            <li class="active"><a href="users.jsp">Users</a></li>
          </ul>
          <ul class="nav navbar-nav navbar-right">
            <li class="dropdown">
              <a href="#" class="dropdown-toggle" data-toggle="dropdown"
              role="button" aria-haspopup="true"
              aria-expanded="false"><b id="user_email" >user_email</b><span class="caret"></span></a>
              <ul class="dropdown-menu">
                <li><a href="profile.jsp">My Profile</a></li>
                <li role="separator" class="divider"></li>
                <li><a href="logout.jsp">Logout</a></li>
              </ul>
            </li>
          </ul>
        </div> <!--navbar collapse -->
      </nav>

      <nav class="navbar navbar-default">
        <div class="container-fluid collapse navbar-collapse">
          <ul class="nav navbar-nav">
            <li><a id="allUsers" href="users.jsp">All Users</a></li>
            <li class="active"><a id="addUser" href="users_add.jsp">Add User</a></li>
          </ul>
        </div>
      </nav> <!-- mini ticket navbar-->
      <div class="row">
        <div class="col-md-8 col-md-offset-2">
          <p id="message-box"></p>
          <form id="form">
            <div class="form-group">
              <label>E-Mail:</label>
              <input class="form-control" type="text" id="users_email" required/>
            </div>
            <div class="form-group">
              <label>Name:</label>
              <input class="form-control" type="text" id="user_name" required/>
            </div>
            <div class="form-group">
              <label>Surname:</label>
              <input class="form-control" type="text" id="user_surname" required/>
            </div>
            <div class="form-group">
              <label>Company:</label>
              <input class="form-control" type="text" id="user_company" required/>
            </div>

            <div class="form-group">
              <label>Password:</label>
              <input class="form-control" type="text" id="user_password" required/>
            </div>
			      <div class="checkbox">
              <label class="checkbox-inline" ><input type="checkbox" name="role" id="adminRole" checked>Admin</label>
              <label class="checkbox-inline" ><input type="checkbox" name="role" id="supporterRole" >Supporter</label>
              <label class="checkbox-inline" ><input type="checkbox" name="role" id="clientRole">Client</label>
        	  </div>
            <button type="submit" onclick="addUser()" class="btn btn-default" >Submit</button>
          </form>
        </div>
      </div><!-- row -->
    </div> <!-- main container fluid -->
    <script>
       var arr ="${sessionScope.LOGIN_USER.email}";
       $("#user_email").html(arr);
    </script>
  </body>
</html>
