
$(document).ready(function(){

	var isAdmin=true;

	if(isAdmin==false){
	  console.log("nav-user is hided");
	  $("#nav_users").hide();
	}
});


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

function getAuthenticatedUser(){

	


}
