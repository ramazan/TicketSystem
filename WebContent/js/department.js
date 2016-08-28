/*
  addPlaceID: departmanlarin ekleneceği select tag idsi
  selectVal: girilmesi durumunda eşleşen value default seçilir
*/
function loadAllDeparments(addPlaceID, selectVal) {
  var selectBoxId = "#" + addPlaceID;
  $(selectBoxId).html(" ");
  $.ajax({
    type: "POST",
    url: '/Ticket_System/rest/department/getAllDepartments',
    contentType: "application/json",
    mimeType: "application/json",
    success: function(departments) {
      $.each(departments, function(key, value) {
        // key == value.id db te id sutunu tutuyoruz...
        if (value.id == selectVal) {
          $(selectBoxId).append(
            $("<option></option>").attr("selected", true).attr("value", value.id).text(value.name));
        } else {
          $(selectBoxId).append(
            $("<option></option>").attr("value", value.id).text(value.name));
        }
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
  $("#new_dep_name").val("");
  $("#add_dep_modal_msg").text("");
  $("#add_dep_modal").modal("show");
  $("#add_dep_modal_btn").prop("disabled", false);
}

function addDepartment() {
  var dName = $("#new_dep_name").val();
  if (dName == "") {
    $("#add_dep_modal_msg").text("You have to fill required(*) places");
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
        $("#add_dep_modal_btn").prop("disabled", true);
        $("#add_dep_modal_msg").text("Department added. Closing Window in 2sec..");
        $("#" + selectedDeparmentAreaID).append(
          $("<option></option>").attr("value", result.id).attr(
            "selected", true).text(result.name));
        setTimeout(function() {
          $('#add_dep_modal').modal('hide');
        }, 2000);
      },
      error: function() {
        $("#add_dep_modal_msg").text("addDepartment | An Error Occured!");
      }
    });
  }
}
