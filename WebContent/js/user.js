function getAllUsers() {
  $("#jqGrid").jqGrid({
    caption: "USER LIST",
    url: "/Ticket_System/rest/user/getAllUsers",
    mtype: "GET",
    datatype: "json",
    colModel:[
      {label:"Name", name:'name', width:80},
      {label:"E-Mail", name:'email', width:100},
      {label:"Password", name:'password', width:100},
      {label:"Company", name:'company.name', width:100},
    ],
    viewrecords: true,
    height: 150,
    width:780,
    styleUI: 'Bootstrap',
    rowNum: 10,
    pager: "#jqGridPager"
  });

  $('#jqGrid').navGrid('#jqGridPager',
   {
       edit: false,
       add: false,
       del: false,
       search: true,
       refresh: true,
       view: true,
       position: "left",
       cloneToTop: false
   });
}

function addUser(){

	var newName= $("#user_name").val();
	var newSurname = $('#user_surname').val();
	var newEmail = $('#user_email').val();
	var newCompany = $('#user_company').val();
	var newPassword = $('#user_password').val();

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

	if(newRoles.length==0 || newName=="" || newSurname=="" ||
        newEmail=="" || newCompany=="" || newPassword==""){
    console.log("error: fill all boxes");
		$("#message-box").html("Please fill all boxes");
	}else{

		var person = {name:newName,
					surname:newSurname,
					email: newEmail,
					password: newPassword,
					company: newCompany,
					userRoles: newRoles};

		$.ajax({
			  type: "POST",
			  url: '/Ticket_System/rest/user/addUser',
			  contentType : "application/json",
			  mimeType: "application/json",
			  data : JSON.stringify(person),
			  success : function(){
				  
			alert("User Added Succesfully")	  
          console.log("user added")
		      }
			});
	}
}



loadCompanies();
function loadCompanies(){

  // TODO: Bu veriler db ten cekilecek !!!
  var comps = [{id:0,name:"32bit"},{id:1,name:"akbank"},{id:2,name:"isbank"}];

  $.each(comps,function(key,value){
    // key == value.id db te id sutunu tutuyoruz...
    $('#user_company')
       .append($("<option></option>")
       .attr("value",key)
       .text(value.name));
  });
}

function addNewCompany(){

  var cEmail=$("#comp_email").val();
  var cName=$("#comp_name").val();
  var cPhone=$("#comp_phone").val();
  var cFax=$("#comp_fax").val();
  var cAddress=$("#comp_address").val();

  if(cEmail=="" || cName==""){
    $("#modal_message").text("You have to fill required places");
  }else{

    var company = {email:cEmail,name:cName,phone:cPhone,fax:cFax,address:cAddress};

    $.ajax({
        type: "POST",
        url: '/Ticket_System/rest/company/addNewCompany',
        contentType : "application/json",
        mimeType: "application/json",
        data : JSON.stringify(company),
        success : function(){
            $("#modal_message").text("Company added. Close Window.");
            $('#user_company')
               .append($("<option></option>")
               .attr("value",cName)
               .attr("selected",true)
               .text(cName)
              );
            
            alert("Company added succesfully!");

        },
        error: function(){
          alert("adding error");
        }
      });
  }
}
