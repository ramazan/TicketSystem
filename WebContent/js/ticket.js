var selectedTicketID;

function getTicket(ticketID) {

  selectedTicketID = ticketID;
  showTicketDetails();

  $.ajax({
    type: "POST",
    url: '/Ticket_System/rest/ticket/getTicketDetails',
    contentType: "application/json",
    mimeType: "application/json",
    data: JSON.stringify(ticketID),
    success: function(ticket) {
      $("#ticket_title").text(ticket.title);
      $("#ticket_message").text(ticket.message);
      $("#ticket_date").text(ticket.time);
      $("#ticket_sender").text(ticket.sender.name);
      $("#ticket_department").text(ticket.department.name);

      if (ticket.status == true) {
        $("#ticket_status").text("OPEN");
        $("#ticket_status").css("color", "green");
      } else {
        $("#ticket_status").text("CLOSED");
        $("#ticket_status").css("color", "red");
      }

      loadAllResponses(selectedTicketID);
      $('html, body').animate({
        scrollTop: $("#ticket_details_page").offset().top
      }, 1000);
    },
    error: function() {
      alert("Ticket details cannot get please try again. TicketID:  " + selectedTicketID);
    }
  });
}

function addLink(cellvalue, options, rowObject) {
  var ticketID = rowObject.id;
  var clickLink = "<a href='#' style='height:25px;width:120px;' type='button' title='Select'";
  clickLink += " onclick=\"getTicket(" + ticketID + ")\" >" + ticketID + "</a>"
  return clickLink;
}

function deleteTicket() {
  $.ajax({
    url: "/Ticket_System/rest/ticket/deleteTicket",
    type: "POST",
    mimeType: "application/json",
    contentType: "application/json",
    data: JSON.stringify(selectedTicketID),
    success: function() {
      $('#tickets_jqGrid').trigger('reloadGrid');
      $("#delete_ticket_label").text("Ticket Deleted. Window closing...");
      setTimeout(function() {
        $('#delete_ticket_modal').modal('hide');
        $('#delete_ticket_msg').text("Are you sure want to delete ticket?");
        // bilgi mesajini temizle
        $("#delete_ticket_label").text("");
      }, 1500);
      showTickets();
    },
    error: function(jqXHR, textStatus, errorThrown) {
      console.log("error :" + errorThrown);
    }
  });
}

function sendTicketResponse() {

  var responseMsg = $("#ticket_response_msg").val();
  if (responseMsg == "") {
    $("#add_resp_msg").text("Please add a response!");
  } else {
    var responseTicket = {
      message: responseMsg,
      ticketID: selectedTicketID,
      sender: {
        id: authenticatedUser.id,
        name: authenticatedUser.name,
        email: authenticatedUser.email
      }
    };

    $.ajax({
      type: "POST",
      url: '/Ticket_System/rest/ticket/addResponse',
      contentType: "application/json",
      mimeType: "application/json",
      data: JSON.stringify(responseTicket),
      success: function(response) {
        $("#ticket_response_msg").val("");
        loadAllResponses();
      }
    });
  }
}

function loadAllResponses() {
  $.ajax({
    type: "POST",
    url: '/Ticket_System/rest/ticket/getAllResponses',
    contentType: "application/json",
    mimeType: "application/json",
    data: JSON.stringify(selectedTicketID),
    success: function(responses) {

      $("#ticket_responses > tbody").html("");
      $.each(responses, function(key, value) {
        $('#ticket_responses > tbody:last-child')
          .append("<tr><th>" + value.date + " / " + value.sender.name + "</th></tr>")
          .append("<tr><td>" + value.message + "</td></tr>");
      });
    }
  });
}

// status boolean : open, closed
function loadAllTickets(status) {
  $("#tickets_jqGrid").jqGrid("clearGridData", true)
    .setGridParam({
      url: "/Ticket_System/rest/ticket/getAllTickets?status=" + status
    })
    .trigger("reloadGrid");
  $("#tickets_jqGrid").jqGrid({
    caption: "Ticket List",
    url: "/Ticket_System/rest/ticket/getAllTickets?status=" + status,
    datatype: "json",
    mtype: 'GET',
    colModel: [{
      label: "ID",
      name: 'id',
      width: 50,
      formatter: addLink
    }, {
      label: "Date",
      name: 'time',
      width: 90,
      formatter: 'date',
      formatoptions: {
        srcformat: 'Y-m-d H:i:s',
        newformat: 'd/m/Y  H:i'
      }
    }, {
      label: "Title",
      name: 'title',
      width: 100
    }, {
      label: "From",
      name: 'sender.name',
      width: 100
    }, {
      label: "Department",
      name: 'department.name',
      width: 80
    }],
    responsive: true,
    multiselect: true,
    viewrecords: true,
    height: 450,
    width: 850,
    styleUI: 'Bootstrap',
    rowNum: 10,
    pager: "#tickets_jqGridPager",
    emptyrecords: "Nothing to display",
  });

  $('#tickets_jqGrid').navGrid('#tickets_jqGridPager', {
    edit: false,
    add: false,
    del: false,
    search: true,
    refresh: true,
    view: false,
    position: "left",
    cloneToTop: false
  });
}

// status boolean : open, closed
function loadPostedTickets(status) {

  $("#tickets_jqGrid").jqGrid("clearGridData", true)
    .setGridParam({
      url: "/Ticket_System/rest/ticket/getPostedTickets?status=" + status
    })
    .trigger("reloadGrid");
  $("#tickets_jqGrid").jqGrid({
    caption: "Ticket List",
    url: "/Ticket_System/rest/ticket/getPostedTickets?status=" + status,
    datatype: "json",
    mtype: 'GET',
    colModel: [{
      label: "ID",
      name: 'id',
      width: 50,
      formatter: addLink
    }, {
      label: "Date",
      name: 'time',
      width: 90,
      formatter: 'date',
      formatoptions: {
        srcformat: 'Y-m-d H:i:s',
        newformat: 'd/m/Y  H:i'
      }
    }, {
      label: "Title",
      name: 'title',
      width: 100
    }, {
      label: "From",
      name: 'sender.name',
      width: 100
    }, {
      label: "Department",
      name: 'department.name',
      width: 80
    }],
    responsive: true,
    multiselect: true,
    viewrecords: true,
    height: 450,
    width: 850,
    styleUI: 'Bootstrap',
    rowNum: 10,
    pager: "#tickets_jqGridPager",
    emptyrecords: "Nothing to display",
  });

  $('#tickets_jqGrid').navGrid('#tickets_jqGridPager', {
    edit: false,
    add: false,
    del: false,
    search: true,
    refresh: true,
    view: false,
    position: "left",
    cloneToTop: false
  });
}

function sendTicket() {
  var ticketTitle = $("#new_ticket_title").val();
  var ticketMessage = $("#new_ticket_msg").val();

  if (ticketTitle == "" || ticketMessage == "") {
    $("#ticket_add_msg").text("Please fill all boxes");
  } else {
    var ticketDepartmentID = $("#new_ticket_dep").val();
    var ticketPriority = $("#new_ticket_prio").val();

    var ticket = {
      sender: {
        id: authenticatedUser.id
      },
      title: ticketTitle,
      message: ticketMessage,
      department: {
        id: ticketDepartmentID
      },
      priority: ticketPriority
    };
    $.ajax({
      type: "POST",
      url: '/Ticket_System/rest/ticket/addTicket',
      contentType: "application/json",
      mimeType: "application/json",
      data: JSON.stringify(ticket),
      success: function() {
        $("#ticket_add_msg").text(
          "Ticket sended. Closing Window..");
        // reload jqgrid
        $('#tickets_jqGrid').trigger('reloadGrid');
        // clear boxes
        $('input:checkbox').removeAttr('checked');
        $("#new_ticket_title").val("");
        $("#new_ticket_title").val("");
        $("#new_ticket_msg").val("");
        setTimeout(function() {
          $('#ticket_add_modal').modal('hide');
          $("#ticket_add_msg").text("");
        }, 2000);
      },
      error: function() {
        $("#ticket_add_msg").text("Ticket couldn't be sent! Later try again");
      }
    });
  }
}

function editTicket() {}

//TODO: bu kısımda hata var, düzenlenmeli
$('textarea').keypress(function() {
  if (this.value.length > 500) {
    return false;
  }
  $("#remainingC").text("Remaining characters : " + (500 - this.value.length));
});
