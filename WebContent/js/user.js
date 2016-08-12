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
			width : 100
		}, {
			label : "Password",
			name : 'password',
			width : 100
		}, {
			label : "Company",
			name : 'company.name',
			width : 100
		}, ],
		viewrecords : true,
		height : 150,
		width : 780,
		styleUI : 'Bootstrap',
		rowNum : 10,
		pager : "#jqGridPager"
	});

	$('#jqGrid').navGrid('#jqGridPager', {
		edit : false,
		add : false,
		del : false,
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
			success : function() {

				alert("User Added Succesfully")
				console.log("user added")
			},

			error : function() {
				alert("User cannot added please try again. ");
			}
		});
	}
}

loadCompanies();
function loadCompanies() {

	// TODO: Bu veriler db ten cekilecek !!!
	var comps = [ {
		id : 0,
		name : "no company"
	}, {
		id : 1,
		name : "32bit"
	}, {
		id : 2,
		name : "akbank"
	} ];

	$.each(comps, function(key, value) {
		// key == value.id db te id sutunu tutuyoruz...
		$('#userCompany').append(
				$("<option></option>").attr("value", key).text(value.name));
	});
}

function addNewCompany() {

	var cEmail = $("#comp_email").val();
	var cName = $("#comp_name").val();
	var cPhone = $("#comp_phone").val();
	var cFax = $("#comp_fax").val();
	var cAddress = $("#comp_address").val();

	if (cEmail == "" || cName == "") {
		$("#modal_message").text("You have to fill required places");
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
			url : '/Ticket_System/rest/company/addNewCompany',
			contentType : "application/json",
			mimeType : "application/json",
			data : JSON.stringify(company),
			success : function() {
				$("#modal_message").text("Company added. Close Window.");
				$('#user_company').append(
						$("<option></option>").attr("value", cName).attr(
								"selected", true).text(cName));

				alert("Company added succesfully!");

			},
			error : function() {
				alert("adding error");
			}
		});
	}
}
