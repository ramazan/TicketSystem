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
    <script type="text/javascript" src="../js/i18n/grid.locale-en.js"></script>
    <script type="text/javascript" src="../js/jquery.jqGrid.min.js"></script>

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
              <li id="nav_tickets" class="active"><a href="tickets.jsp">Tickets</a></1i>
              <li id="nav_users" ><a href="users.jsp">Users</a></1i>
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
      </nav>

      <nav class="navbar navbar-default">
        <div class="container-fluid collapse navbar-collapse">
          <ul class="nav navbar-nav">
            <li class="active"><a id="allTickets" href="tickets.jsp">All Tickets</a></li>
            <li><a id="openedTickets" href="#">Opened Tickets</a></li>
            <li><a id="closedTickets" href="#">Closed Tickets</a></li>
            <li><a id="newTicket" href="tickets_add.jsp">New Ticket</a></li>
          </ul>
        </div>
      </nav> <!-- mini ticket navbar-->

          <!-- JQGRID Test -->
      <div class="row">
          <div class="col-md-8 col-md-offset-2">
            <table id="jqGrid"></table>
            <div id="jqGridPager" style="height:36px"></div>
            <script type="text/javascript">
              $(document).ready(function () {
                $("#jqGrid").jqGrid({
                  caption: "Ticket List",
                  url: "/Ticket_System/rest/ticket/getAllTickets",
                  datatype: "json",
                  mtype: 'GET',
                  colModel:[
                    {label:"ID", name:'id', width:80},
                    {label:"Title", name:'title', width:80,},
                    {label:"Sender", name:'sender', width:100},
                    {label:"Department", name:'department', width:100}
                  ],
                  width:780,
                  styleUI: 'Bootstrap',
                  viewrecords: true,
                  height: 150,
                  rowNum: 10,
                  pager: "#jqGridPager"
                });

                $('#jqGrid').navGrid('#jqGridPager',
                 {
                   edit: false,
                   add: false,
                   del: false,
                   search: true,
                   refresh: true,
                   view: true,
                   position: "left",
                   cloneToTop: false
                 });
              });
            </script>
          </div><!-- com-md -->
      </div><!-- row -- >
    </div> <!-- main container fluid-->

    <!-- prepare page -->
    <script type="text/javascript" src="../js/ticket_security.js"></script>
    <script type="text/javascript">
      var arr ="${sessionScope.LOGIN_USER.email}";
      $("#nickname").html(arr);
    </script>
  </body>
</html>
