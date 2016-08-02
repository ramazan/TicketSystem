<!DOCTYPE HTML5>
<html>
  <head>
    <meta charset="utf-8" />

    <title>TicketSystem - Add Ticket</title>
    <link rel="stylesheet" type="text/css" href="../css/bootstrap.min.css" />
    <!-- JAVASCRIPT SOURCES -->
    <script type="text/javascript" src="../js/jquery-3.1.0.min.js"></script>
    <script type="text/javascript" src="../js/bootstrap.min.js"></script>

    <style>
      .new-ticket{
        width:700px;
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
            <li><a href="dashboard.jsp">Dashboard</a></1i>
            <li class="active"><a href="tickets.jsp">Tickets</a></1i>
            <li><a href="users.jsp">Users</a></1i>
          </ul>
          <ul class="nav navbar-nav navbar-right">
            <li class="dropdown">
              <a id="user_email" href="#" class="dropdown-toggle" data-toggle="dropdown"
              role="button" aria-haspopup="true"
              aria-expanded="false">user_email<span class="caret"></span></a>
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
            <li><a id="allTickets" href="tickets.jsp">All Tickets</a></li>
            <li><a id="openedTickets" href="#">Opened Tickets</a></li>
            <li><a id="closedTickets" href="#">Closed Tickets</a></li>
            <li class="active"><a id="newTicket" href="tickets_add.jsp">New Ticket</a></li>
          </ul>
        </div>
      </nav> <!-- mini ticket navbar-->
      <div class="row">
        <div class="col-md-6 col-md-offset-2">
          <div class="new-ticket table-responsive">
            <table class="table table-bordered">
              <thead>
                <th colspan="2">New Ticket</th>
              </thead>
              <tbody>
                <tr>
                  <td>Deparment:</td>
                  <td>
                    <select id="ticket-deparment">
                      <option value="dep_sale">Sale</option>
                      <option value="dep_support">Support</option>
                      <option value="dep_accounting">Accounting</option>
                    </select>
                  </td>
                </tr>
                <tr>
                  <td>Priority:</td>
                  <td>
                    <select id="ticket_priority">
                      <option value="prio_low">Low</option>
                      <option value="prio_normal">Normal</option>
                      <option value="prio_high">High</option>
                      <option value="prio_emergency">Emergency</option>
                    </select>
                  </td>
                </tr>
                <th colspan="2">Issue:</th>
                <tr>
                  <td>Subject:</td>
                  <td>
                    <textarea rows="1" cols="50" id="ticket_subject"></textarea>
                  </td>
                </tr>
                <tr>
                  <td>Message:</td>
                  <td>
                    <textarea rows="3" cols="50" id="ticket_message"></textarea>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
          <button class="btn btn-success center-block" onclick="#">Send Ticket</button>
        </div>
      </div>




    </div>
  </body>
</html>
