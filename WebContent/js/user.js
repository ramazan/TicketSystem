var selectedUser;

// users tablosu için tüm kullanıcıları getirme işlemi
function loadAllUsers() {

	$("#users_jqGrid").GridUnload();
	$("#users_jqGrid").jqGrid({
		caption : "USER LIST",
		url : "/Ticket_System/rest/user/getAllUsers",
		mtype : "GET",
		datatype : "json",
		colModel : [ {
			label : "ID",
			name : 'id',
			width : 35,
			sortable : true,
			sorttype : "int",
			search : true,
			align : "center"
		}, {
			label : "Name",
			name : 'name',
			width : 85
		}, {
			label : "E-Mail",
			name : 'email',
			width : 140
		}, {
			label : "Company",
			name : 'company.name',
			width : 85
		}, {
			label : "Department",
			name : 'department.name',
			width : 95
		}, {
			label : "Roles",
			name : 'userRoles',
			width : 125
		}, {
			label : "Detail",
			name : 'id',
			width : 40,
			formatter : addUserLink
		} ],
		viewrecords : true,
		height : 400,
		width : 890,
		// loadonce: true,
		rowNum : 100,
		styleUI : 'Bootstrap',
		pager : "#users_jqGridPager",
		emptyrecords : "Nothing to display"
	});

	$('#users_jqGrid').navGrid('#users_jqGridPager', {
		edit : false,
		add : false,
		del : false,
		search : false,
		refresh : true,
		view : false,
		position : "left",
		cloneToTop : false
	}).navButtonAdd('#users_jqGridPager', {
		caption : "Add",
		buttonicon : "ui-icon-add",
		onClickButton : function() {
			prepareAddUserArea();
		},
		position : "last"
	});

}

function prepareAddUserArea() {
	loadAllDeparments("new_user_dep");
	loadAllCompanies("new_user_company");

	$("#departmentFade").hide();
	$("#add_user_modal_btn").prop("disabled", false);
	$("#add_user_modal_msg").text("");
	$('#add_user_roles input').prop('checked', false);
	$('#new_user_name').val('');
	$('#new_user_email').val('');
	$('#new_user_password').val(''); // inputları temizle.

	$("#add_user_modal").modal("show");
}

// yeni kullanıcı ekleme işlemi
function addUser() {

	var newName = $("#new_user_name").val();
	var newEmail = $('#new_user_email').val();
	var newCompanyID = $('#new_user_company').val();
	var newPassword = $('#new_user_password').val();

	var pattern = /^\b[A-Z0-9._%-]+@[A-Z0-9.-]+\.[A-Z]{2,4}\b$/i;

	var newRoles = [];
	var userDepartmentID = 0;
	if ($('#adminRole').is(':checked')) {
		newRoles.push("admin");
	}
	if ($('#supporterRole').is(':checked')) {
		newRoles.push("supporter");
		userDepartmentID = $("#new_user_dep").val();
	}
	if ($('#clientRole').is(':checked')) {
		newRoles.push("client");
	}

	if (newRoles.length == 0 || newName == "" || newEmail == ""
			|| newCompanyID == "" || newPassword == "") {
		console.log("error: fill all boxes");
		$("#add_user_modal_msg").text("Please fill required(*) boxes");
	} else if (!pattern.test(newEmail)) {
		$("#add_user_modal_msg").text("Invalid e-mail address!");
	} else {

		var person = {
			name : newName,
			email : newEmail,
			password : newPassword,
			company : {
				id : newCompanyID
			},
			userRoles : newRoles,
			department : {
				id : userDepartmentID
			}
		};

		$
				.ajax({
					type : "POST",
					url : '/Ticket_System/rest/user/addUser',
					contentType : "application/json",
					mimeType : "application/json",
					data : JSON.stringify(person),
					success : function() {
						$("#add_user_modal_btn").prop("disabled", true);
						$("#add_user_modal_msg").text(
								"User added. Closing Window in 2sec..");
						// reload jqgrid
						$('#users_jqGrid').trigger('reloadGrid');
						getBadges();
						setTimeout(function() {
							$('#add_user_modal').modal('hide');
						}, 2000);
					},
					error : function(jqXHR, textStatus, errorThrown) {
						if (jqXHR.status == 409) {
							$("#add_user_modal_msg")
									.text(
											"There is an existing account associated with this mail");
							// hatali user varsa modal kapanmadan bilgi
							// guncellemesi yapilmalı
							// NOT: modal acilmadan once tum veriler zaten
							// temizleniyor
							// tekrar temizlemeye gerek yok!
						} else {
							$("#add_user_modal_msg").text(
									"Unresolved error! Send ticket!");
						}
					}
				});
	}
}

// profile sayfasındaki bilgilerin set edilmesi
function loadProfileInf() {
	var user = authenticatedUser;
	$("#user_email").text(user.email);
	$("#user_department").text(user.department.name);
	$("#user_name").text(user.name);
	$("#user_roles").text(user.userRoles);
	$("#user_company").text(user.company.name);
}

// Profile sayfasındaki şifre eşleşme kontrolü
function validate() {
	var password1 = $("#user_new_pass").val();
	var password2 = $("#user_new_pass_c").val();

	if (password1 == "" && password2 == "") {
		$("#profile_update_alert").hide();

	} else if (password1 == password2 && password1 != "" && password2 != "") {
		$("#profile_update_alert").show();
		$("#profile_update_alert").css("display", "none");
		$("#user_update_btn").removeAttr('disabled');

	} else {
		$("#profile_update_alert").show();
		$("#pass_validate").text("Passwords don't match!");
		$("#user_update_btn").prop("disabled", true);
	}
}

// profile sayfasındaki kullanıcın şifre değiştirme işlemi
function updateProfile() {

	var password = $("#user_new_pass").val();

	$.ajax({
		type : "PUT",
		url : '/Ticket_System/rest/user/updateProfile',
		contentType : "application/json",
		mimeType : "application/json",
		data : password,
		success : function() {
			$("#pass_validate").text("Password changed!");
			$("#user_update_btn").prop("disabled", true);
		},
		error : function() {
			$("#pass_validate").text("An error occured!");
		}
	});
}

// users tablosundaki detail butonlarının hazırlanması
function addUserLink(cellvalue, options, rowObject) {
	var userID = rowObject.id;
	var clickLink = "<button onclick='getUser(" + userID + ")'"
			+ "class='btn btn-warning btn-xs'>Detail</button>"
	return clickLink;
}

// detail buutonuna tıklandıgında ilgili kullanıcının bilgilerinin getirilmesi
function getUser(userID) {
	loadAllDeparments("selectedPersonDepartment");
	loadAllCompanies("selectedPersonCompany");

	$("#selectedPersonPassword").prop("type", "password");
	$("#selectedUserRoles input").prop("checked", false);
	$("#departmentInput").hide();

	$.ajax({
		type : "GET",
		url : '/Ticket_System/rest/user/getUserInfo/' + userID,
		contentType : "application/json",
		mimeType : "application/json",
		success : function(user) {
			selectedUser = user;
			$("#selectedPersonName").val(user.name);
			$("#selectedPersonPassword").val(user.password);
			$("#selectedPersonCompany").val(user.company.id);
			$("#selectedPersonEmail").val(user.email);

			$.each(user.userRoles, function(key, value) {
				if (value == "admin") {
					$("#selectedPersonRoleAdmin").prop("checked", true);
				} else if (value == "supporter") {
					$("#selectedPersonRoleSup").prop("checked", true);
					$("#departmentInput").show();
					$("#selectedPersonDepartment").val(user.department.id);
				} else if (value == "client") {
					$("#selectedPersonRoleClient").prop("checked", true);
				}
			});
			$('#user_modal_detail').modal('show');
		},
		error : function() {
			alert("user details cannot get please try again. userID:  "
					+ userID);
		}
	});
}

function prepareDeleteUserArea() {
	$("#delete_user_modal_msg").text("");
	$("#delete_user_modal").modal("show");
}

// user detail modaldeki seçilen kullanıcının silinmesi
function deleteUserData() {
	$("#delete_user_modal_btn").prop("disabled", true);
	var deletedUser = {
		id : selectedUser.id,
		email : selectedUser.email
	}
	$.ajax({
		url : "/Ticket_System/rest/user/deleteUser/",
		type : "POST",
		mimeType : "application/json",
		contentType : "application/json",
		data : JSON.stringify(deletedUser),
		success : function() {
			$("#delete_user_modal_msg").text(
					"User deleted. Closing Window in 2sec..");
			$('#users_jqGrid').trigger('reloadGrid');
			getBadges();
			setTimeout(function() {
				$('#user_modal_detail').modal('hide');
				$('#delete_user_modal').modal('hide');
				$("#delete_user_modal_btn").prop("disabled", false);
			}, 2000);
		},
		error : function(jqXHR, textStatus, errorThrown) {
			$("#delete_user_modal_msg").text("Error! Try Later!");
			$("#delete_user_modal_btn").prop("disabled", false);
		}
	});
}

function prepareUpdateUserArea() {
	$("#update_user_modal_msg").text("");
	$("#update_user_modal").modal("show");
}
// user detail update kısmı
function updateUserData() {
	$("#update_user_modal_btn").prop("disabled", true);

	var newName = $("#selectedPersonName").val();
	var newEmail = $('#selectedPersonEmail').val();
	var newCompanyID = $('#selectedPersonCompany').val();
	var newPassword = $('#selectedPersonPassword').val();

	var pattern = /^\b[A-Z0-9._%-]+@[A-Z0-9.-]+\.[A-Z]{2,4}\b$/i;

	var newRoles = [];
	var userDepartmentID = 0;
	if ($('#selectedPersonRoleAdmin').is(':checked')) {
		newRoles.push("admin");
	}
	if ($('#selectedPersonRoleSup').is(':checked')) {
		newRoles.push("supporter");
		userDepartmentID = $("#selectedPersonDepartment").val();
	}
	if ($('#selectedPersonRoleClient').is(':checked')) {
		newRoles.push("client");
	}

	if (newRoles.length == 0 || newName == "" || newEmail == ""
			|| newCompanyID == "" || newPassword == "") {
		console.log("error: fill all boxes");
		$("#update_user_modal_msg").text("Please fill required(*) boxes");
		$("#update_user_modal_btn").prop("disabled", false);
		setTimeout(function() {
			$("#update_user_modal_msg").text("");
		}, 2000);
	} else if (!pattern.test(newEmail)) {
		$("#update_user_modal_btn").prop("disabled", false);
		$("#update_user_modal_msg").text("Invalid e-mail address!");
	} else {

		var person = {
			id : selectedUser.id,
			name : newName,
			email : newEmail,
			password : newPassword,
			company : {
				id : newCompanyID
			},
			userRoles : newRoles,
			department : {
				id : userDepartmentID
			}
		};

		$.ajax({
			type : "POST",
			url : '/Ticket_System/rest/user/updateUserData/',
			contentType : "application/json",
			mimeType : "application/json",
			data : JSON.stringify(person),
			success : function() {
				$("#update_user_modal_msg").text(
						"User updated. Closing Window in 2sec..");
				// reload jqgrid
				$('#users_jqGrid').trigger('reloadGrid');
				setTimeout(function() {
					$('#user_modal_detail').modal('hide');
					$('#update_user_modal').modal('hide');
					$("#update_user_modal_btn").prop("disabled", false);
				}, 2000);
			},
			error : function() {
				$("#update_user_modal_btn").prop("disabled", false);
				alert("User cannot updated please try again. ");
			}
		});
	}
}

function togglePasswordArea() {
	var pass = $("#selectedPersonPassword");
	console.log(pass.prop("type"));
	if (pass.prop("type") == "password") {
		pass.prop("type", "text");
	} else {
		pass.prop("type", "password");
	}
}
