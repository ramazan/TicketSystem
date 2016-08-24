function loadAllCompanies(selectID) {

  var compSelectID = "#" + selectID;

  console.log("compselect ıd " + compSelectID);

  $(compSelectID).html(" "); // kutucugu temizle

  $.ajax({
    type: "POST",
    url: '/Ticket_System/rest/company/getAllCompanies',
    contentType: "application/json",
    mimeType: "application/json",
    success: function(companies) {
      $.each(companies, function(key, value) {
        // key == value.id db te id sutunu tutuyoruz...
        $(compSelectID).append(
          $("<option></option>").attr("value", value.id).text(value.name));
      });
    },
    error: function() {
      console.log("getAllCompanies error");
    }
  });
}

function addCompany() {

  var cEmail = $("#new_company_email").val();
  var cName = $("#new_company_name").val();
  var cPhone = $("#new_company_phone").val();
  var cFax = $("#new_company_fax").val();
  var cAddress = $("#new_company_address").val();

  if (cEmail == "" || cName == "") {
    $("#add_company_msg").text("You have to fill required places");
  } else {

    var company = {
      email: cEmail,
      name: cName,
      phone: cPhone,
      fax: cFax,
      address: cAddress
    };

    $.ajax({
      type: "POST",
      url: '/Ticket_System/rest/company/addCompany',
      contentType: "application/json",
      mimeType: "application/json",
      data: JSON.stringify(company),
      success: function(result) {
        $("#add_company_msg").text("Company added. Closing Window..");
        $('#new_user_company').append(
          $("<option></option>").attr("value", result.id).attr(
            "selected", true).text(result.name));
        $('#add_company_msg').val('');
        $('#new_company_fax').val('');
        $('#new_company_phone').val('');
        $('#new_company_email').val('');
        $('#new_company_name').val('');
        $('#new_company_address').val('');
        setTimeout(function() {
          $('#add_company_modal').modal('hide');
          $("#add_company_msg").text("");
        }, 2000);
      },
      error: function() {
        $("#add_company_msg").text("Company can't added. Please try again..");
      }
    });
  }
}
