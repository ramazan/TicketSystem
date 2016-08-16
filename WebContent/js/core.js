var authenticatedUserID;

$(document).ready(function(){

	$.ajax({
			type: "POST",
			url: '/Ticket_System/rest/session/getAuthenticatedUser',
			contentType : "application/json",
			mimeType: "application/json",
			success : function(data){
				console.log("user email:" + data.email+" name:"+data.name);
				userID = data.id;
				$("#nickname").text(data.email);
				authenticatedUserID = data.id;
				$("#user_email").text(data.email);
				$("#user_name").val(data.name);
				$("#user_company").text(data.company.name);
				$("#user_roles").text(data.userRoles);
				}
		});

	var isAdmin=true; // TODO: sessiondan check yapılacak

	if(isAdmin==false){
	  console.log("nav-user is hided");
	  $("#nav_users").hide();
	}

	$('#supporterRole').change(function() {
		if (this.checked)
			$('#departmentFade').fadeIn();
		else
			$('#departmentFade').fadeOut();
	});

	$("#user_new_pass2").keyup(validate);

});


function login(){
}

function logout(){
	$.get("/Ticket_System/rest/session/logout");
	window.location="/Ticket_System";
}


function hideTickets(){
	getAllUsers();
	getAllDepartments("#userDepartment");
	getAllCompanies();

	$('#userLink').addClass("active");
	$("#ticketLink").removeClass("active");
	$('#users').show();
	$('#tickets').hide();
}

function hideUsers(){
	getAllTickets();
	getAllDepartments("#ticketDepartments");

	$('#userLink').removeClass("active");
	$("#ticketLink").addClass("active");
	$('#users').hide();
	$('#tickets').show();
}

// / Profile sayfasındaki şifre eşleşme kontrolü

function validate() {
	var password1 = $("#user_new_pass").val();
	var password2 = $("#user_new_pass2").val();

	if (password1 == password2 && password1 != "" && password2 != "") {
		$("#validate-status").text("Şifreler eşleşti!");
		// document.getElementById("ProfileSaveButton").enabled = true;
		$("#ProfileSaveButton").removeAttr('disabled');

	} else {
		$("#validate-status").text("Şifreler eşleşmiyor!");
		$("#ProfileSaveButton").prop("disabled", true);

	}

}

function updateProfile() {

	var password = $("#user_new_pass").val();
	alert("şifre " + password);

	$.ajax({
		type : "POST",
		url : '/Ticket_System/rest/user/updateUser',
		contentType : "application/json",
		mimeType : "application/json",
		data :JSON.stringify(password),
		success : function(data) {
//			alert("Şifre değişimi başarılı" + data);

		},
		error : function() {
			alert("User cannot added please try again. ");
		}
	});

}
