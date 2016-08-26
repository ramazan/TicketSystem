function loadAllDeparments(selectID) {

  var selectBoxId = "#" + selectID;

  $(selectBoxId).html(" ");

  $.ajax({
    type: "POST",
    url: '/Ticket_System/rest/department/getAllDepartments',
    contentType: "application/json",
    mimeType: "application/json",
    success: function(departments) {
      $.each(departments, function(key, value) {
        // key == value.id db te id sutunu tutuyoruz...
        $(selectBoxId).append(
          $("<option></option>").attr("value", value.id).text(value.name));
      });
    },
    error: function() {
      alert("Deparment cannot added please try again. ");
    }
  });
}

var selectedDeparmentAreaID;

function prepareAddDepArea(depAddAreaID) {
  selectedDeparmentAreaID = depAddAreaID;
  console.log("selectedDeparmentAreaID: " + selectedDeparmentAreaID);
}

function addDepartment() {
  var dName = $("#new_dep_name").val();
  console.log("new_dep_name: " +
    dName);
  if (dName == "") {
    $("#add_dep_msg").text("You have to fill required places");
  } else {

    var company = {
      name: dName,
    };

    $.ajax({
      type: "POST",
      url: '/Ticket_System/rest/department/addDepartment',
      contentType: "application/json",
      mimeType: "application/json",
      data: JSON.stringify(company),
      success: function(result) {
        $("#add_dep_msg").text("Department added. Closing Window..");
        $("#" + selectedDeparmentAreaID).append(
          $("<option></option>").attr("value", result.id).attr(
            "selected", true).text(result.name));
        $("#new_dep_name").val("");
        $("#add_dep_msg").val("");
        setTimeout(function() {
          $('#add_dep_modal').modal('hide');
          $("#add_dep_msg").text("");
        }, 2000);
      },
      error: function() {
        console.log("add department error");
        $("#add_dep_msg").text("Department can't added. Please try again..");
      }
    });
  }
}
