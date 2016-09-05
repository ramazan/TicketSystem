var selectedTicket;

function prepareTicketDetailArea() {
  $("#ticket_detail_edit_btn").show();
  $("#ticket_detail_delete_btn").show();
  $("#ticket_detail_close_btn").show();
}

function getTicket(ticketID) {
  prepareTicketDetailArea();
  $.ajax({
    type: "POST",
    url: '/Ticket_System/rest/ticket/getTicketDetails',
    contentType: "application/json",
    mimeType: "application/json",
    data: JSON.stringify(ticketID),
    success: function(ticket) {
      selectedTicket = ticket;

      showTicketDetails(selectedTicket);

      $("#ticket_title").text(ticket.title);
      var msg = ticket.message.replace(/\n/g, "<br />");
      $("#ticket_message").html(msg);
      $("#ticket_date").text(ticket.time);
      $("#ticket_sender").text(ticket.sender.name);
      $("#ticket_department").text(ticket.department.name);
      $("#ticket_sender_mail").text(ticket.sender.email);

      if (ticket.status == true) {
        $("#ticket_status").text("OPEN");
        $("#ticket_status").css("color", "green");
        $("#close_ticket_btn").show();
      } else {
        $("#ticket_status").text("CLOSED");
        $("#ticket_status").css("color", "red");
        $("#close_ticket_btn").hide();
        $("#respArea").hide();
      }

      loadAllResponses(selectedTicket.id);
      $('html, body').animate({
        scrollTop: $("#ticket_details_page").offset().top
      }, 1000);
    },
    error: function() {
      alert("Ticket details cannot get please try again. TicketID:  " +
        selectedTicket.id);
    }
  });
}

function addTitleLink(cellvalue, options, rowObject) {
  var ticketID = rowObject.id;
  var ticketTitle = rowObject.title;
  var clickLink = "<a href='#' style='height:25px;width:120px; color:#AC2323;' type='button' title='Select'";
  clickLink += " onclick=\"getTicket(" + ticketID + ")\" >" + ticketTitle + "</a>"
  return clickLink;
}

function addIDLink(cellvalue, options, rowObject) {
  var ticketID = rowObject.id;
  var clickLink = "<a href='#' style='height:25px;width:120px; color:#AC2323;' type='button' title='Select'";
  clickLink += " onclick=\"getTicket(" + ticketID + ")\" >" + ticketID + "</a>"
  return clickLink;
}

function deleteTicket() {
  $("#deleteTicketButton").prop("disabled", true);
  $.ajax({
    url: "/Ticket_System/rest/ticket/deleteTicket",
    type: "POST",
    mimeType: "application/json",
    contentType: "application/json",
    data: JSON.stringify(selectedTicket.id),
    success: function() {
      getBadges();
      $("#deleteTicketButton").prop("disabled", false);
      $('#tickets_jqGrid').trigger('reloadGrid');
      $("#delete_ticket_modal_msg")
        .text("Ticket Deleted. Window closing");
      $('#ticket_details_page').hide();
      setTimeout(function() {
        $("#delete_ticket_modal_msg").text("");
        $('#delete_ticket_modal').modal('hide');
      }, 2000);
      showTickets();
    },
    error: function(jqXHR, textStatus, errorThrown) {
      $("#deleteTicketButton").prop("disabled", false);
      $("#delete_ticket_modal_msg").text(
        "You are not authorized to delete tickets");
    }
  });
}

function sendTicketResponse() {

  var responseMsg = $("#ticket_response_msg").val();
  if (responseMsg == "") {
    $('#addResponseAlertMessage').show();
  } else {
    $('#addResponseAlertMessage').hide();
    $("#sendTicketResponse").prop("disabled", true);
    var responseTicket = {
      message: responseMsg,
      ticketID: selectedTicket.id,
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
        $('.responseMessageCountdown').text(
          '500 characters remaining.');
        $("#sendTicketResponse").prop("disabled", false);
        loadAllResponses();
      }
    });
  }
}

function loadAllResponses() {
  $("#addResponseAlertMessage").hide();
  $.ajax({
    type: "POST",
    url: '/Ticket_System/rest/ticket/getAllResponses',
    contentType: "application/json",
    mimeType: "application/json",
    data: JSON.stringify(selectedTicket.id),
    success: function(responses) {

      if (responses.length == 0) {
        $('#ResponseAlertMessage').show();
        $('#response_list').text("");
      } else {
        $('#ResponseAlertMessage').hide();
        $("#response_list").html("");
        $.each(responses, function(key, value) {
          var msg = value.message.replace(
            /\n/g, "<br />");
          $('#response_list').append(
            "<li class='media'>" +
            "<div class='media-body'><div class='media'>" +
            "<div class='media-body'><p class='response_message'>" +
            msg +
            "</p><p class = 'text-muted' >" +
            value.sender.name +
            "  |  " +
            value.date +
            " </p><hr>" +
            "</div> </div > </div> </li>");
        });
      }
    }
  });
}

function closeTicket() {
  $("#closeTicketButton").prop("disabled", true);
  $.ajax({
    type: "POST",
    url: "/Ticket_System/rest/ticket/closeTicket",
    contentType: "application/json",
    mimeType: "application/json",
    data: JSON.stringify(selectedTicket.id),
    success: function() {
      getBadges();
      $("#close_ticket_modal_msg").text("Ticket Closed. Window closing");
      loadPostedTickets(1);
      $('#ticket_details_page').hide();

      setTimeout(function() {
        $("#close_ticket_modal_msg").text("");
        $('#close_ticket_modal').modal('hide');
        $("#closeTicketButton").prop("disabled", false);
      }, 2000);
    },
    error: function() {
      $("close_ticket_modal_msg").text("An Error Occured! Try Later");
      setTimeout(function() {
        $("#close_ticket_modal_msg").text("");
        $("#closeTicketButton").prop("disabled", false);
        $('#close_ticket_modal').modal('hide');
      }, 2000);
    }
  });
}

// status boolean : open, closed
function loadAllTickets(status) {

  $("#tickets_jqGrid").GridUnload();
  $("#tickets_jqGrid").jqGrid({
    caption: "Ticket List",
    url: "/Ticket_System/rest/ticket/getAllTickets?status=" + status,
    datatype: "json",
    mtype: 'GET',
    colModel: [{
      label: "ID",
      name: 'id',
      align: "center",
      width: 30,
      formatter: addIDLink
    }, {
      label: "Date",
      name: 'time',
      width: 70,
      formatter: 'date',
      formatoptions: {
        srcformat: 'Y-m-d H:i:s',
        newformat: 'd/m/Y  H:i'
      },
      align: "center"
    }, {
      label: "Title",
      name: 'title',
      width: 150,
      align: "center",
      formatter: addTitleLink
    }, {
      label: "From",
      name: 'sender.name',
      width: 70,
      align: "center"
    }, {
      label: "Department",
      name: 'department.name',
      width: 70,
      align: "center"
    }],
    responsive: true,
    viewrecords: true,
    height: 450,
    width: 850,
    styleUI: 'Bootstrap',
    rowNum: 100,
    pager: "#tickets_jqGridPager",
    emptyrecords: "Nothing to display",
  });

  $('#tickets_jqGrid').navGrid('#tickets_jqGridPager', {
    edit: false,
    add: false,
    del: false,
    search: false,
    refresh: true,
    view: false,
    position: "left",
    cloneToTop: false
  });
}

// status boolean : open, closed
function loadPostedTickets(status) {

  $("#tickets_jqGrid").GridUnload();
  $("#tickets_jqGrid").jqGrid({
    caption: "Ticket List",
    url: "/Ticket_System/rest/ticket/getPostedTickets?status=" + status,
    datatype: "json",
    mtype: 'GET',
    colModel: [{
      label: "ID",
      name: 'id',
      width: 30,
      align: "center",
      formatter: addIDLink
    }, {
      label: "Date",
      name: 'time',
      width: 70,
      formatter: 'date',
      formatoptions: {
        srcformat: 'Y-m-d H:i:s',
        newformat: 'd/m/Y  H:i'
      },
      align: "center"
    }, {
      label: "Title",
      name: 'title',
      width: 150,
      formatter: addTitleLink
    }, {
      label: "From",
      name: 'sender.name',
      width: 70,
      align: "center"
    }, {
      label: "Department",
      name: 'department.name',
      width: 70,
      align: "center"
    }],
    responsive: true,
    multiselect: true,
    viewrecords: true,
    height: 450,
    width: 850,
    styleUI: 'Bootstrap',
    loadonce: true,
    rowNum: 100,
    pager: "#tickets_jqGridPager",
    emptyrecords: "Nothing to display",
  });

  $('#tickets_jqGrid').navGrid('#tickets_jqGridPager', {
    edit: false,
    add: false,
    del: false,
    search: false,
    refresh: true,
    view: false,
    position: "left",
    cloneToTop: false
  });
}

function prepareAddTicketArea() {
  $("#add_ticket_modal_msg").text("");
  $("#sendTicketButton").prop("disabled", false);
  $('input:checkbox').removeAttr('checked');
  $("#new_ticket_title").val("");
  $("#new_ticket_msg").val("");
  $('.ticketMessageCountdown').text('500 characters remaining.');
  loadAllDeparments("new_ticket_dep");
  $("#add_ticket_modal").modal("show");
}

function sendTicket() {
  $("#sendTicketButton").prop("disabled", true);
  var ticketTitle = $("#new_ticket_title").val();
  var ticketMessage = $("#new_ticket_msg").val();

  if (ticketTitle == "" || ticketMessage == "") {
    $("#sendTicketButton").prop("disabled", false);
    $("#add_ticket_modal_msg").text("Please fill required inputs");
  } else {
    var ticketDepartmentID = $("#new_ticket_dep").val();

    var ticket = {
      sender: {
        id: authenticatedUser.id
      },
      title: ticketTitle,
      message: ticketMessage,
      department: {
        id: ticketDepartmentID
      }
    };
    $.ajax({
      type: "POST",
      url: '/Ticket_System/rest/ticket/addTicket',
      contentType: "application/json",
      mimeType: "application/json",
      data: JSON.stringify(ticket),
      success: function() {
        $("#add_ticket_modal_msg").text(
          "Ticket sended. Closing Window in 2sec..");
        getBadges();
        setTimeout(function() {
          $('#add_ticket_modal').modal('hide');
          $("#sendTicketButton").prop("disabled", false);
        }, 2000);
        // reload jqgrid
        if (isClient == true)
          loadPostedTickets(1);
        else loadAllTickets(1);
      },
      error: function() {
        $("#sendTicketButton").prop("disabled", false);
        $("#add_ticket_modal_msg").text("An Error Occured!");
      }
    });
  }
}

function prepareEditTicketArea() {
  loadAllDeparments("edit_ticket_dep", selectedTicket.department.id);
  $("#edit_ticket_modal_msg").text("");
  $("#edit_ticket_modal").modal("show");
  $("#editTicketButton").prop("disabled", false);
  $("#edit_ticket_dep").val(selectedTicket.department.id);
}

function editTicket() {

  var ticketDepartmentID = $("#edit_ticket_dep").val();

  var editedTicket = {
    id: selectedTicket.id,
    department: {
      id: ticketDepartmentID
    }
  };

  $.ajax({
    type: "POST",
    url: '/Ticket_System/rest/ticket/editTicket/',
    contentType: "application/json",
    mimeType: "application/json",
    data: JSON.stringify(editedTicket),
    success: function() {
      getTicket(selectedTicket.id);
      $("#editTicketButton").prop("disabled", true);
      $("#edit_ticket_modal_msg").text("Ticket Edited. Closing Window");
      $('#tickets_jqGrid').trigger('reloadGrid');
      setTimeout(function() {
        $('#edit_ticket_modal').modal('hide');
      }, 2000);
    },
    error: function() {
      alert("An Error Occured!");
    }
  });

}

function updateCountdownTicketResponse() {
  // 140 is the max message length
  var remaining = 500 - $('.responseMessage').val().length;
  $('.responseMessageCountdown').text(remaining + ' characters remaining.');
}

function updateCountdownTicket() {
  // 140 is the max message length
  var remaining = 500 - jQuery('#new_ticket_msg').val().length;
  jQuery('.ticketMessageCountdown')
    .text(remaining + ' characters remaining.');
}

$(document).ready(function($) {
  updateCountdownTicketResponse();
  $('.responseMessage').change(updateCountdownTicketResponse);
  $('.responseMessage').keyup(updateCountdownTicketResponse);

  updateCountdownTicket();
  $('#new_ticket_msg').change(updateCountdownTicket);
  $('#new_ticket_msg').keyup(updateCountdownTicket);
});
