function getAllUsers() {

	$("#users_jqGrid").jqGrid({
		caption : "USER LIST",
		url : "/Ticket_System/rest/user/getAllUsers",
		mtype : "GET",
		datatype : "json",
		colModel : [ {
			label : "ID",
			name : 'id',
			width : 40
		}, {
			label : "Name",
			name : 'name',
			width : 85
		}, {
			label : "E-Mail",
			name : 'email',
			width : 110
		}, {
			label : "Password",
			name : 'password',
			width : 70
		}, {
			label : "Company",
			name : 'company.name',
			width : 85
		}, {
			label : "Department",
			name : 'department.name',
			width : 85
		}, ],
		viewrecords : true,
		height : 400,
		width : 850,
		rowNum : 10,
		styleUI : 'Bootstrap',
		pager : "#users_jqGridPager",
		emptyrecords: "Nothing to display"

	});

	$('#users_jqGrid').navGrid('#users_jqGridPager', {
		edit : false,
		add : false,
		del : false,
		search : true,
		refresh : true,
		view : true,
		position : "left",
		cloneToTop : false
	}).navButtonAdd('#users_jqGridPager', {
		caption : "Add",
		buttonicon : "ui-icon-add",
		onClickButton : function() {
			$('#myUserModal').modal('show');
		},
		position : "last"
	}).navButtonAdd('#users_jqGridPager', {
		caption : "Del",
		buttonicon : "ui-icon-del",
		onClickButton : function() {
			alert("Deleting Row");
		},
		position : "last"
	});
}

function addUser() {

	var newName = $("#userFullName").val();
	var newEmail = $('#userEmail').val();
	var newCompanyID = $('#userCompany').val();
	var newPassword = $('#userPassword').val();

	var newRoles = [];
	var userDepartmentID = 0;
	if ($('#adminRole').is(':checked')) {
		newRoles.push("admin");
	}
	if ($('#supporterRole').is(':checked')) {
		newRoles.push("supporter");
		userDepartmentID = $("#userDepartment").val();
		console.log("department:" + userDepartmentID);
	}
	if ($('#clientRole').is(':checked')) {
		newRoles.push("client");
	}

	if (newRoles.length == 0 || newName == "" || newEmail == ""
			|| newCompanyID == "" || newPassword == "") {
		console.log("error: fill all boxes");
		$("#modalAddUserMessage").html("Please fill all boxes");
	} else {

		var person = {
			name : newName,
			email : newEmail,
			password : newPassword,
			company : { id:newCompanyID},
			userRoles : newRoles,
			department : {id:userDepartmentID}
		};

		$.ajax({
			type : "POST",
			url : '/Ticket_System/rest/user/addUser',
			contentType : "application/json",
			mimeType : "application/json",
			data : JSON.stringify(person),
			success : function() {
				$("#modalAddUserMessage").text("User added. Closing Window..");  // başarılı mesajını set et
				$('#users_jqGrid').trigger('reloadGrid');    						 // jqGridi reload ediyorum
				$('input:checkbox').removeAttr('checked'); 					// check boxların check'ini kaldır
				$('input').val('');   								       // inputları temizle.
  				setTimeout(function() { $('#myUserModal').modal('hide'); }, 2000);
			},
			error : function() {
				alert("User cannot added please try again. ");
			}
		});
	}
}

// / Profile sayfasındaki şifre eşleşme kontrolü

function validate() {
	var password1 = $("#userNewPass").val();
	var password2 = $("#userNewPassConfirm").val();

	if (password1 == password2 && password1 != "" && password2 != "") {
		$("#validateStatus").text("Şifreler eşleşti!");
		// document.getElementById("ProfileSaveButton").enabled = true;
		$("#ProfileSaveButton").removeAttr('disabled');

	} else {
		$("#validateStatus").text("Şifreler eşleşmiyor!");
		$("#ProfileSaveButton").prop("disabled", true);

	}

}

function updateProfile() {

	var password = $("#userNewPass").val();

	$.ajax({
		type : "POST",
		url : '/Ticket_System/rest/user/updateUser',
		contentType : "application/json",
		mimeType : "application/json",
		data :password,
		success : function(data) {
			$("#validateStatus").text("Şifre başarıyla değişti!");
		},
		error : function() {
			alert("User cannot added please try again. ");
		}
	});

}


$(document).ready(function(){
	$("#userNewPass").keyup(validate);
	$("#userNewPassConfirm").keyup(validate);
});
