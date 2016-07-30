function addUser(){
	var newName= $("#name").val();
	var newSurname = $('#surname').val();
	var newEmail = $('#email').val();
	var newCompany = $('#company').val();
	var newPassword = $('#password').val();

	if(newName=="" || newSurname=="" || newEmail=="" || newCompany=="" || newPassword==""){
		$("#message-box").text("Please fill all boxes");
	}else{
		var newRole;
		if($('#adminRole').is(':checked')){
			newRole="admin";
		}else if($('#supporterRole').is(':checked')){
			newRole="supporter";
		}else{
			newRole="client";
		}

		var person = {name:newName,
					surname:newSurname,
					email: newEmail,
					password: newPassword,
					company: newCompany,
					role: newRole};

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
