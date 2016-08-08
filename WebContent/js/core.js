


$(document).ready(function(){


	var authenticatedUser ="";
	// getAuthenticatedUser(authenticatedUser);

	$.get("/Ticket_System/rest/session/getAuthenticatedUser",function(data,status){
		console.log("getAuthenticatedUser : "+data.name+"Status:"+status);
		$("#nickname").text(data.name);
	});




	//$("#username").text(authenticatedUser.email);
	/*var isAdmin=true;

	if(isAdmin==false){
	  console.log("nav-user is hided");
	  $("#nav_users").hide();
	}*/
});

function logout(){
	$.get("/Ticket_System/rest/session/logout");
	window.location="/Ticket_System";
}

function addUser(){
	var newName= $("#name").val();
	var newSurname = $('#surname').val();
	var newEmail = $('#email').val();
	var newCompany = $('#company').val();
	var newPassword = $('#password').val();

	if(newName=="" || newSurname=="" || newEmail=="" || newCompany=="" || newPassword==""){
		$("#message-box").text("Please fill all boxes");
	}else{
		var newRoles = [];
		if($('#adminRole').is(':checked')){
			newRoles.push("admin");
		}
		if($('#supporterRole').is(':checked')){
			newRoles.push("supporter");
		}
		if($('#clientRole').is(':checked')){
			newRoles.push("client");
		}

		var person = {name:newName,
					surname:newSurname,
					email: newEmail,
					password: newPassword,
					company: newCompany,
					userRoles: newRoles};

		$.ajax({
			  type: "POST",
			  url: '/Ticket_System/rest/admin/addUser',
			  contentType : "application/json",
			  mimeType: "application/json",
			  data : JSON.stringify(person),
			  success : function(){
				  alert("User added");
		      }
			});
	}
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
