


$(document).ready(function(){


	var authenticatedUser ="";
	// getAuthenticatedUser(authenticatedUser);

	$.get("/Ticket_System/rest/session/getAuthenticatedUser",function(data,status){
		console.log("getAuthenticatedUser : "+data.name+"Status:"+status);
		$("#nickname").text(data.name);
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



/*
function getAuthenticatedUser(){

	$.ajax({
			type: "GET",
			url: '/Ticket_System/rest/session/getAuthenticatedUser',
			contentType : "application/json",
			mimeType: "application/json",
			success : function(retVal){
				var user = {email:retVal.email, name:retVal.name, password:retVal.password, userRoles:retVal.userRoles};
				console.log("test"+user.email+user.name)
				console.log("getAuthenticatedUser succesfull");
				return user;
			}
		});
}*/
