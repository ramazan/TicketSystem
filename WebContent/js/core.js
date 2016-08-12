$(document).ready(function(){

	var authenticatedUser;

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
});

function logout(){
	$.get("/Ticket_System/rest/session/logout");
	window.location="/Ticket_System";
}
