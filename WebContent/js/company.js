loadCompanies();
function loadCompanies() {

  $('#userCompany').append(
      $("<option></option>").attr("value", 0).text("No Company"));
  $.ajax({
    type : "POST",
    url : '/Ticket_System/rest/company/getAllCompanies',
    contentType : "application/json",
    mimeType : "application/json",
    success : function(companies) {
      $.each(companies, function(key, value) {
        // key == value.id db te id sutunu tutuyoruz...
        $('#userCompany').append(
            $("<option></option>").attr("value", value.id).text(value.name));
      });
    },
    error : function() {
      console.log("getAllCompanies error");
    }
  });
}

function addCompany() {

	var cEmail = $("#compEmail").val();
	var cName = $("#compName").val();
	var cPhone = $("#compPhone").val();
	var cFax = $("#compFax").val();
	var cAddress = $("#compAddress").val();

	if (cEmail == "" || cName == "") {
		$("#addCompanyModalMessage").text("You have to fill required places");
	} else {

		var company = {
			email : cEmail,
			name : cName,
			phone : cPhone,
			fax : cFax,
			address : cAddress
		};

		$.ajax({
			type : "POST",
			url : '/Ticket_System/rest/company/addCompany',
			contentType : "application/json",
			mimeType : "application/json",
			data : JSON.stringify(company),
			success : function(result) {
          $("#addCompanyModalMessage").text("Company added. Close Window.");
  				$('#userCompany').append(
  						$("<option></option>").attr("value", result.id).attr(
  								"selected", true).text(result.name));
			},
			error : function() {
				alert("adding error");
			}
		});
	}
}
