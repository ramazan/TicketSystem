$(document).ready(function() {
	getAllTickets();
	getAllDepartments("#ticketDepartment");
});

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
			formatter : 'showlink',
			formatoptions : {
				baseLinkUrl : '###',
				idName : 'ticketID'
			}
		}, {
			label : "Date",
			name : 'date',
			width : 60,
			formatter : 'date',
			formatoptions : {
				srcformat : 'U',
				newformat : 'd/m/Y'
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
		emptyrecords : "Nothing to display"
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
			}
		});

	}
}
