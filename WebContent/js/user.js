function getAllUsers() {
	$("#jqGrid").jqGrid({
		caption : "USER LIST",
		url : "/Ticket_System/rest/user/getAllUsers",
		mtype : "GET",
		datatype : "json",
		colModel : [ {
			label : "Name",
			name : 'name',
			width : 80
		}, {
			label : "E-Mail",
			name : 'email',
			width : 120
		}, {
			label : "Password",
			name : 'password',
			width : 60
		}, {
			label : "CompanyID",
			name : 'companyID',
			width : 60
		}, ],
		viewrecords : true,
		height : 550,
		width : 750,
		styleUI : 'Bootstrap',
		rowNum : 10,
		pager : "#jqGridPager"
	});

	$('#jqGrid').navGrid('#jqGridPager', {
		edit : true,
		add : true,
		del : true,
		search : true,
		refresh : true,
		view : true,
		position : "left",
		cloneToTop : false
	});
}

function addUser() {

	var newName = $("#userFullName").val();
	var newEmail = $('#userEmail').val();
	var newCompany = $('#userCompany').val();
	var newPassword = $('#userPassword').val();

	var newRoles = [];
	if ($('#adminRole').is(':checked')) {
		newRoles.push("admin");
	}
	if ($('#supporterRole').is(':checked')) {
		newRoles.push("supporter");
	}
	if ($('#clientRole').is(':checked')) {
		newRoles.push("client");
	}

	if (newRoles.length == 0 || newName == "" || newEmail == ""
			|| newCompany == "" || newPassword == "") {
		console.log("error: fill all boxes");
		$("#modalAddUserMessage").html("Please fill all boxes");
	} else {

		var person = {
			name : newName,
			email : newEmail,
			password : newPassword,
			companyID : newCompany,
			userRoles : newRoles
		};

		$.ajax({
					type : "POST",
					url : '/Ticket_System/rest/user/addUser',
					contentType : "application/json",
					mimeType : "application/json",
					data : JSON.stringify(person),
					success : function(status) {
						if (status == "SUCCESS") {
							$("#modalAddUserMessage").text(
									"User added. Close Window.");

							// Kullancı başarıyla eklendiğinde gridi refresh
							// ediyorum.
							$('#jqGrid').trigger('reloadGrid');

							console.log("grid reloadeded")
						} else if (status == "USER_EXIST") {
							$("#modalAddUserMessage").text(
									"User exist!! Email must be unique.");
						} else {
							$("#modalAddUserMessage").text(
									"Error occured!. Open a ticket.");
						}
					},

					error : function() {
						alert("User cannot added please try again. ");
					}
				});
	}
}

$(document).ready(function() {
	
	  $('#supporterRole').change(function(){
	        if(this.checked)
	            $('#departmentFade').fadeIn();
	        else
	            $('#departmentFade').fadeOut();

	    });
	  
	getAllUsers();
});
