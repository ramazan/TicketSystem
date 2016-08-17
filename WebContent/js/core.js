var authenticatedUserID;

$(document).ready(function(){

	$.ajax({
			type: "POST",
			url: '/Ticket_System/rest/session/getAuthenticatedUser',
			contentType : "application/json",
			mimeType: "application/json",
			success : function(data){
				console.log("user email:" + data.email+" name:"+data.name +" company name  :" + data.company.name+" roller: "+data.userRoles   );
				userID = data.id;
				$("#userProfileEmail").text(data.email);
				$("#userProfileName").text(data.name);
				$("#userProfileCompany").text(data.company.name);
				$("#userProfileRoles").text(data.userRoles);
				$("#nickname").text("Welcome " + data.name);
				authenticatedUserID = data.id;
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


	$("#userNewPass").keyup(validate);
	$("#userNewPassConfirm").keyup(validate);

});


function login(){
}

function logout(){
	$.get("/Ticket_System/rest/session/logout");
	window.location="/Ticket_System";
}


function showUsers(){
	getAllUsers();
	getAllDepartments("#userDepartment");
	getAllCompanies();

	$('#userLink').addClass("active");
	$("#ticketLink").removeClass("active");
	$('#users').show();
	$('#tickets').hide();
	$('#ticket_details').hide();
	$('#profile').hide();
}

function showTicketDetails(){
	$('#userLink').removeClass("active");
	$("#ticketLink").addClass("active");
	$('#users').hide();
	$('#profile').hide();
	$('#ticket_details').show();
	$('#tickets').hide();
}

function showTickets(){
	getAllTickets();
	getAllDepartments("#ticketDepartments");

	$('#userLink').removeClass("active");
	$("#ticketLink").addClass("active");
	$('#users').hide();
	$('#ticket_details').hide();
	$('#profile').hide();
	$('#tickets').show();
}

function showProfile(){

	$('#userLink').removeClass("active");
	$("#ticketLink").addClass("active");
	$('#users').hide();
	$('#ticket_details').hide();
	$('#profile').show();
	$('#tickets').hide();
}
