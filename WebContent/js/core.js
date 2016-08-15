$(document).ready(function(){

	$.ajax({
			type: "POST",
			url: '/Ticket_System/rest/session/getAuthenticatedUser',
			contentType : "application/json",
			mimeType: "application/json",
			success : function(data){
				console.log("user email:" + data.email+" name:"+data.name);
				$("#nickname").text(data.email);
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


});


function login(){
}

function logout(){
	$.get("/Ticket_System/rest/session/logout");
	window.location="/Ticket_System";
}


function hideTickets(){
	getAllUsers();
	getAllDepartments();
	getAllCompanies();

	$('#userLink').addClass("active");
	$("#ticketLink").removeClass("active");
	$('#users').show();
	$('#tickets').hide();
}

function hideUsers(){
	getAllTickets();

	$('#userLink').removeClass("active");
	$("#ticketLink").addClass("active");
	$('#users').hide();
	$('#tickets').show();
}
