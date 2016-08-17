	$(document).ready(function() {
	getAllTickets();
	getAllDepartments("#ticketDepartment");
});

function getTicket(ticketID){

	console.log("Ticket ID:"+ticketID);
	console.log(typeof ticketID);
	showTicketDetails();

	$.ajax({
		type : "POST",
		url : '/Ticket_System/rest/ticket/getTicketDetails',
		contentType : "application/json",
		mimeType : "application/json",
		data:JSON.stringify(ticketID),
		success : function(ticket) {
			$("#ticket_title").text(ticket.title);
			$("#ticket_message").text(ticket.message);
			$("#ticket_status").text(ticket.status);
			$("#ticket_date").text(ticket.time);
			$("#ticket_sender").text(ticket.sender.name);
			$("#ticket_department").text(ticket.department.name);
			console.log("Ticket: "+ticket);
		},
		error : function() {
			alert("Ticket details cannot get please try again. TicketID:  " + ticketID);
		}
	});
}

function addLink(cellvalue, options, rowObject){
	var ticketID= rowObject.id;
	var clickLink = "<a href='#' style='height:25px;width:120px;' type='button' title='Select'";
	clickLink +=" onclick=\"getTicket("+ticketID+")\" >"+ticketID+"</a>"
	console.log(" clickLink :  "+ clickLink);
	return clickLink;
}


function getAllTickets() {
	$("#ticket_jqGrid").jqGrid({
		caption : "Ticket List",
		url : "/Ticket_System/rest/ticket/getAllTickets",
		datatype : "json",
		mtype : 'GET',
		colModel : [ {
			label : "ID",
			name : 'id',
			width : 25,
			formatter: addLink
		}, {
			label : "Date",
			name : 'time',
			width : 60,
			formatter : 'date',
			formatoptions : {
      srcformat: 'Y-m-d H:i:s', newformat:'d/m/Y H:i:s'
      }
		}, {
			label : "Title",
			name : 'title',
			width : 100
		}, {
			label : "From",
			name : 'sender.name',
			width : 100
		}, {
			label : "Department",
			name : 'department.name',
			width : 80
		} ],
		responsive : true,
		multiselect : true,
		viewrecords : true,
		height : 450,
		width : 850,
		styleUI : 'Bootstrap',
		rowNum : 10,
		pager : "#ticket_jqGridPager",
		emptyrecords : "Nothing to display",
	});

	$('#ticket_jqGrid').navGrid('#ticket_jqGridPager', {
		edit : false,
		add : false,
		del : false,
		search : true,
		refresh : true,
		view : true,
		position : "left",
		cloneToTop : false
	}).navButtonAdd('#ticket_jqGridPager', {
		caption : "Add",
		buttonicon : "ui-icon-add",
		onClickButton : function() {
			$('#modalAddTicket').modal('show');
		}
	});
}

function addTicket() {
	var ticketTitle = $("#ticketTitle").val();
	var ticketMessage = $("#ticketMessage").val();
	if (ticketTitle == "" || ticketMessage == "") {
		$("#modalAddTicketMessage").text("Please fill all boxes");
	} else {
		var ticketDepartmentID = $("#ticketDepartment").val();
		var ticketPriority = $("#ticketPriority").val();

		console.log("Selected DepID:" + ticketDepartmentID + " Prio:"
				+ ticketPriority);

		var ticket = {
			sender : {
				id : authenticatedUserID
			},
			title : ticketTitle,
			message : ticketMessage,
			department : {
				id : ticketDepartmentID
			},
			priority : ticketPriority
		};
		$.ajax({
			type : "POST",
			url : '/Ticket_System/rest/ticket/addTicket',
			contentType : "application/json",
			mimeType : "application/json",
			data : JSON.stringify(ticket),
			success : function() {
				$("#modalAddTicketMessage").text(
						"Ticket sended. Closing Window..");
				$('#ticket_jqGrid').trigger('reloadGrid'); // jqGridi reload
															// ediyorum
				$('input:checkbox').removeAttr('checked'); // check boxların
															// check'ini kaldır
				$('input').val(''); // inputları temizle.
				setTimeout(function() {
					$('#modalAddTicket').modal('hide');
				}, 2000);
			},
			error : function() {
				alert("Ticket cannot added please try again. ");
			}
		});

	}
}
