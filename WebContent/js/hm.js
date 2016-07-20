
function getPerson(){
	var tc = $("#inTC").val();
	
	//var person={tc:111,name:"temp",phone:"temp"};
	
	$.ajax({
		  type: "POST",
		  url: 'rest/getPerson',
		  contentType : "application/json",
		  mimeType: "application/json",
		  data : tc,
		  success : function(retVal){			
			var mydata = [{tc:retVal.tc,name:retVal.name,phone:retVal.phone}];
				
			/* update test2 */
				$("#gridTable").jqGrid({
					datatype: "local",
					height: 100,
					colNames:['tc','name','phone'],
					colModel:[
			 		   {name:'tc', width:80},
			 		   {name:'name', width:80,},
			 		   {name:'phone', width:80}
					],
					caption: "Manipulating Array Data", 
					data: mydata
				});

			/*
			 * var person=retVal;
			  $("#text-tc").html("TC: "+person.tc);
			  $("#text-name").html("Name: "+person.name);
			  $("#text-phone").html("Phone: "+person.phone);*/
	        }
		});
	
}

function addPerson(){
	var inTC = $("#inTC").val();
	var inName = $("#inName").val();
	var inPhone = $("#inPhone").val();
	
	var person = {tc:inTC,name:inName,phone:inPhone};
	
	$.ajax({
		  type: "POST",
		  url: 'rest/addPerson',
		  contentType : "application/json",
		  mimeType: "application/json",
		  data : JSON.stringify(person),
		  success : function(retVal){
			  console.log("person added");
	        }
		});	
}

function deletePerson(){
	var tc = $("#inTC").val();
	
	$.ajax({
		  type: "DELETE",
		  url: 'rest/deletePerson',
		  contentType : "application/json",
		  mimeType: "application/json",
		  data : tc,
		  success : function(retVal){
			  console.log("person deleted");
	        }
		});		
}

function updatePerson(){
	
	var inTC = $("#inTC").val();
	var inName = $("#inName").val();
	var inPhone = $("#inPhone").val();
	
	var person = {tc:inTC,name:inName,phone:inPhone};
	
	$.ajax({
		  type: "PUT",
		  url: 'rest/updatePerson',
		  contentType : "application/json",
		  mimeType: "application/json",
		  data : JSON.stringify(person),
		  success : function(retVal){
			  console.log("person updated");
	        }
		});	
}