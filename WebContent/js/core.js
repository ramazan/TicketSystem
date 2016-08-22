var authenticatedUser;

var ticketTableCreateStatus=false;
var userTableCreateStatus=false;

// login to system
$.get("/Ticket_System/rest/session/login",function(){

	$(document).ready(function(){
		$.ajax({
				type: "POST",
				url: '/Ticket_System/rest/session/getAuthenticatedUser',
				contentType : "application/json",
				mimeType: "application/json",
				success : function(data){
					authenticatedUser = data;
					$("#nav_nickname").text(authenticatedUser.email);
				}
		});

		loadAllTickets();
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
	});
});


function logout(){
	$.get("/Ticket_System/rest/session/logout");
	window.location="/Ticket_System";
}

function showProfile(){

	loadProfileInf();
	$("#user_new_pass").keyup(validate);
	$("#user_new_pass_c").keyup(validate);


	$('#nav_users').removeClass("active");
	$("#nav_tickets").removeClass("active");

	$('#users_page').hide();
	$('#tickets_page').hide();
	$('#ticket_details_page').hide();
	$('#profile_page').show();
}

function showTicketDetails(){

	$("#nav_users").removeClass("active");
	$('#nav_tickets').removeClass("active");
	$("#nav_profile").addClass("active");

	$('#ticket_details_page').show();

}

function showTickets(){

	loadAllTickets();

	$("#")

	$("#nav_profile").removeClass("active");
	$('#nav_users').removeClass("active");
	$("#nav_tickets").addClass("active");
	$('#users_page').hide();
	$('#ticket_details_page').hide();
	$('#profile_page').hide();
	$('#tickets_page').show();
}

function showUsers(){
	loadAllUsers();

	$("#nav_profile").removeClass("active");
	$('#nav_tickets').removeClass("active");
	$("#nav_users").addClass("active");
	$('#users_page').show();
	$('#ticket_details_page').hide();
	$('#profile_page').hide();
	$('#tickets_page').hide();
}
