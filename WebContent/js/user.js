

function loadAllUsers() {

	loadAllDeparments("new_user_dep");
	loadAllCompanies("new_user_company");

	$("#users_jqGrid").jqGrid({
		caption : "USER LIST",
		url : "/Ticket_System/rest/user/getAllUsers",
		mtype : "GET",
		datatype : "json",
		colModel : [ {
			label : "ID",
			name : 'id',
			width : 40,
			formatter: addUserLink
		}, {
			label : "Name",
			name : 'name',
			width : 85
		}, {
			label : "E-Mail",
			name : 'email',
			width : 110
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
			width :125
		},],
		viewrecords : true,
		height : 400,
		width : 890,
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
			$('#add_user_modal').modal('show');
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

	var newName = $("#new_user_name").val();
	var newEmail = $('#new_user_email').val();
	var newCompanyID = $('#new_user_company').val();
	var newPassword = $('#new_user_password').val();

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
		$("#add_user_msg").html("Please fill all boxes");
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
				$("#add_user_msg").text("User added. Closing Window..");
				// reload jqgrid
				$('#users_jqGrid').trigger('reloadGrid');
				// clear boxes
				$('input:checkbox').removeAttr('checked');
				$('#add_user_msg').val('');
				$('#new_user_name').val('');
				$('#new_user_email').val('');
				$('#new_user_password').val('');  								       // inputları temizle.
  				setTimeout(function() { $('#add_user_modal').modal('hide'); }, 2000);
			},
			error : function() {
				alert("User cannot added please try again. ");
			}
		});
	}
}

function loadProfileInf(){

	var user = authenticatedUser;
	$("#user_email").text(user.email);
	$("#user_name").text(user.name);
	$("#user_roles").text(user.userRoles);
	$("#user_company").text(user.company.name);

}

// / Profile sayfasındaki şifre eşleşme kontrolü

function validate() {
	var password1 = $("#user_new_pass").val();
	var password2 = $("#user_new_pass_c").val();

	if (password1 == password2 && password1 != "" && password2 != "") {
		$("#pass_validate").text("Şifreler eşleşti!");
		// document.getElementById("ProfileSaveButton").enabled = true;
		$("#user_update_btn").removeAttr('disabled');

	} else {
		$("#pass_validate").text("Şifreler eşleşmiyor!");
		$("#user_update_btn").prop("disabled", true);
	}
}

function updateProfile() {

	var password = $("#user_new_pass").val();

	$.ajax({
		type : "PUT",
		url : '/Ticket_System/rest/user/updateProfile',
		contentType : "application/json",
		mimeType : "application/json",
		data :password,
		success : function() {
			$("#pass_validate").text("Şifre başarıyla değişti!");
			$("#user_update_btn").prop("disabled", true);
		},
		error : function() {
			alert("User cannot added please try again. ");
		}
	});

}



function addUserLink(cellvalue, options, rowObject){
	var userID= rowObject.id;
	var clickLink = "<a href='#' style='height:25px;width:120px;' type='button' title='Select'";
	clickLink +=" onclick=\"getUser("+userID+")\" >"+userID+"</a>"
	return clickLink;
}

function getUser(userID){


	console.log("user ID:"+userID);
	$('#user-detail-modal').modal('show');

	$.ajax({
		type : "GET",
		url : '/Ticket_System/rest/user/getUserInfo/' + userID,
		contentType : "application/json",
		mimeType : "application/json",
		success : function(user){
			$("#selectedPersonName").val(user.name);
			$("#selectedPersonEmail").val(user.email);
			$("#selectedPersonPassword").val(user.password);
			console.log("#selectedPerson.Name : " +user.name + "#selectedPerson.email: " +user.email);
//			$("#ticket_date").text(user.time);
//			$("#ticket_sender").text(user.sender.name);
//			$("#ticket_department").text(user.department.name);
//		
//			loadAllResponses(clickedTicketID);
//			$('html, body').animate({
//			        scrollTop: $("#ticket_detail_table").offset().top
//			    }, 1000);


		},
		error : function() {
			alert("user details cannot get please try again. userID:  " + userID);
		}
	});
}

//
//var app = angular.module('app', []);
//
//app.controller('PersonListCtrl', function($scope, $http) {
//
//	$http.get('/Ticket_System/rest/user/getUserInfo').success(
//			function(data) {
//				$scope.selectedPerson= data;
//			});
//
//	$scope.selectedPerson={};
//	
//	$scope.showPerson = function($person){
//		
//		$scope.selectedPerson=$person;
//	}
//	
//	
//	
//	
//	
//	$scope.insertData = function() {
//		$http.post('/CrudApp/webapi/persons/', {
//			'tckn' : $scope.tckn,
//			'firstname' : $scope.firstname,
//			'surname' : $scope.surname,
//			'adress' : $scope.adress
//		})
//
//		.success(function(data, status, headers, config) {
//			$http.get('/CrudApp/webapi/persons/').success(
//					function(data) {
//						$scope.persons = data;
//					});
//			console.log("Veri başarıyla kaydedildi kardeş.");
//		});
//	}
//	
//	
//	$scope.putData = function() {
//		$http.put('/CrudApp/webapi/persons/', {
//			'tckn' : $scope.selectedPerson.tckn,
//			'firstname' : $scope.selectedPerson.firstname,
//			'surname' : $scope.selectedPerson.surname,
//			'adress' : $scope.selectedPerson.adress,
//			'id' : $scope.selectedPerson.id
//
//		})
//
//		.success(function(data, status, headers, config) {
//			$http.get('/CrudApp/webapi/persons/').success(
//					function(data) {
//						$scope.persons = data;
//					});
//			console.log("Veri başarıyla güncellendi kardeş.");
//		});
//	}
//	
//	
//	
//	
//	
//	$scope.deleteData = function() {
//		$http.delete('/CrudApp/webapi/persons/'+$scope.selectedPerson.id, {
//					'id' : $scope.selectedPerson.id
//
//		})
//
//		.success(function(data, status, headers, config) {
//			$http.get('/CrudApp/webapi/persons/').success(
//					function(data) {
//						$scope.persons = data;
//					});
//			console.log("Veri başarıyla silindi kardeş.");
//		});
//	}
//	
//	
//	$scope.reloadPage = function() {
//		$http.get('/CrudApp/webapi/persons/').success(
//				function(data) {
//					$scope.persons = data;
//				console.log("Veri başarıyla re-render edildi kardeş.");
//				});
//	}
//	
//	
//});