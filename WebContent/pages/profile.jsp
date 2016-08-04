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
            <a class="navbar-brand" href="dashboard.jsp">TicketSystem</a>
          </div>
          <div class="collapse navbar-collapse" >
            <ul class="nav navbar-nav">
              <li id="nav_dash" ><a href="dashboard.jsp">Dashboard</a></1i>
              <li id="nav_tickets"><a href="tickets.jsp">Tickets</a></1i>
              <li id="nav_users"><a href="users.jsp">Users</a></li>
            </ul>
            <ul class="nav navbar-nav navbar-right">
              <li class="dropdown">
                <a href="#" class="dropdown-toggle" data-toggle="dropdown"
                role="button" aria-haspopup="true"
                aria-expanded="false"><b id="nickname" >nickname</b><span class="caret"></span></a>
                <ul class="dropdown-menu">
                  <li><a href="profile.jsp">My Profile</a></li>
                  <li role="separator" class="divider"></li>
                  <li><a href="logout.jsp">Logout</a></li>
                </ul>
              </li>
            </ul>
          </div> <!--navbar collapse -->
      </nav> <!-- navbar -->

      <div class="table-responsive">
        <table class="table table-condensed table-bordered">
          <thead>
            <tr>
              <th colspan="2" >Account Informations</th>
            </tr>
          </thead>
          <tbody>
            <tr>
              <td>E-mail:</td>
              <td><b id="user_email">TestEmail</b></td>
            </tr>
            <tr>
              <td>Roles:</td>
              <td><b id="user_roles">rolesHere</b></td>
            </tr>
            <tr>
              <td>Name:</td>
              <td><input id="user_name" type="text" value="testName"></td>
            </tr>
            <tr>
              <td>Surname:</td>
              <td><input id="user_surname" type="text" value="testSurname"></td>
            </tr>
            <tr>
              <td>Phone:</td>
              <td><input id="user_phone" type="text" value="testPhone"></td>
            </tr>
            <tr>
              <th colspan="2"><b>Password</b></th>
            </tr>
            <tr>
              <td>Current Password:</td>
              <td><input id="user_curr_pass" type="password"></td>
            </tr>
            <tr>
              <td>New Password:</td>
              <td><input id="user_new_pass" type="password"></td>
            </tr>
            <tr>
              <td>Confirm New Password:</td>
              <td><input id="user_new_pass2" type="password"></td>
            </tr>
          </tbody>
        </table>
    </div> <!-- main container-fluid -->
    <div class="row">
      <div class="col-md-3 col-md-offset-4">
        <button class="btn btn-success btn-block" onclick="changeUserInf()">Save</button>
      </div>
    </div>
    <!-- prepare page -->
    <script type="text/javascript" src="../js/ticket_security.js"></script>
    <script type="text/javascript">
      var arr ="${sessionScope.LOGIN_USER.email}";
      $("#nickname").html(arr);
    </script>
  </body>
</html>
