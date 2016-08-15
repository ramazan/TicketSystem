
function getAllDepartments(){
  $.ajax({
        type : "POST",
        url : '/Ticket_System/rest/department/getAllDepartments',
        contentType : "application/json",
        mimeType : "application/json",
        success : function(departments) {
          $.each(departments, function(key, value) {
            // key == value.id db te id sutunu tutuyoruz...
            $('#userDepartment').append(
                $("<option></option>").attr("value", value.id).text(value.name));
          });
        },

        error : function() {
          alert("Deparment cannot added please try again. ");
        }
      });

}



function addDepartment() {

	var dName = $("#departmentName").val();

	if (dName == "") {
		$("#addDepartmentModalMessage").text("You have to fill required places");
	} else {

		var company = {
				name : dName,
	
		};

		$.ajax({
			type : "POST",
			url : '/Ticket_System/rest/department/addDepartment',
			contentType : "application/json",
			mimeType : "application/json",
			data : JSON.stringify(company),
			success : function(result) {
          $("#addDepartmentModalMessage").text("Department added. Closing Window..");
  				$('#userDepartment').append(
  						$("<option></option>").attr("value", result.id).attr(
  								"selected", true).text(result.name));

  				setTimeout(function() { $('#myModalAddDepartment').modal('hide'); }, 2000);
			},
			error : function() {
		          $("#addDepartmentModalMessage").text("Department can't added. Please try again..");
			}
		});
	}
}

