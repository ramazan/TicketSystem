function loadDepartmentsPage() {
	/* deneme */

	$("#deps_jqGrid").GridUnload();
	$("#deps_jqGrid").jqGrid({
		caption : "DEPARTMENTS LIST",
		url : "/Ticket_System/rest/department/getAllDepartments",
		mtype : "GET",
		datatype : "json",
		colModel : [ {
			label : "ID",
			name : 'id',
			width : 35,
			sortable : true,
			sorttype : "int",
			search : true,
			align : "center"
		}, {
			label : "Department",
			name : 'name',
			width : 95
		}, {
			label : "Delete",
			name : 'id',
			width : 40,
			formatter : addDepartmentLink
		} ],
		viewrecords : true,
		height : 400,
		width : 890,
		// loadonce: true,
		rowNum : 100,
		styleUI : 'Bootstrap',
		pager : "#deps_jqGridPager",
		emptyrecords : "Nothing to display"
	});

	$('#deps_jqGrid').navGrid('#deps_jqGridPager', {
		edit : false,
		add : false,
		del : false,
		search : true,
		refresh : true,
		view : false,
		position : "left",
		cloneToTop : false
	}).navButtonAdd('#deps_jqGridPager', {
		caption : "Add",
		buttonicon : "ui-icon-add",
		onClickButton : function() {
			prepareAddDepArea();
		}
	});
}

function loadAllDeparments(addPlaceID, selectVal) {

	var selectBoxId = "#" + addPlaceID;
	$(selectBoxId).html(" ");
	$.ajax({
		type : "GET",
		url : '/Ticket_System/rest/department/getAllDepartments',
		contentType : "application/json",
		mimeType : "application/json",
		success : function(departments) {
			$.each(departments, function(key, value) {
				// key == value.id db te id sutunu tutuyoruz...
				if (value.id == selectVal) {
					$(selectBoxId).append(
							$("<option></option>").attr("selected", true).attr(
									"value", value.id).text(value.name));
				} else {
					$(selectBoxId).append(
							$("<option></option>").attr("value", value.id)
									.text(value.name));
				}
			});
		},
		error : function() {
			alert("Deparment cannot added please try again. ");
		}
	});
}

var selectedDeparmentAreaID;

function prepareAddDepArea(depAddAreaID) {
	selectedDeparmentAreaID = depAddAreaID;
	$("#new_dep_name").val("");
	$("#add_dep_modal_msg").text("");
	$("#add_dep_modal").modal("show");
	$("#add_dep_modal_btn").prop("disabled", false);
}

function addDepartment() {
	$("#add_dep_modal_btn").prop("disabled", true);
	var dName = $("#new_dep_name").val();
	if (dName == "") {
		$("#add_dep_modal_msg").text("You have to fill required(*) places");
	} else {

		var company = {
			name : dName,
		};

		$.ajax({
			type : "POST",
			url : '/Ticket_System/rest/department/addDepartment',
			contentType : "application/json",
			mimeType : "application/json",
			data : JSON.stringify(company),
			success : function(result) {
				$("#add_dep_modal_msg").text(
						"Department added. Closing Window in 2sec..");
				$("#" + selectedDeparmentAreaID).append(
						$("<option></option>").attr("value", result.id).attr(
								"selected", true).text(result.name));
				$('#deps_jqGrid').trigger('reloadGrid');
				setTimeout(function() {
					$("#add_dep_modal_btn").prop("disabled", false);
					$('#add_dep_modal').modal('hide');
					$("#add_dep_modal_msg").text("");
				}, 2000);
			},
			error : function(jqXHR, textStatus, errorThrown) {
				if (jqXHR.status == 409) {
					$("#add_dep_modal_msg").text("Department exist!");	
					setTimeout(function() {
						$("#add_dep_modal_msg").text("");
						$('#add_dep_modal').modal('hide');
					}, 4000);
				} else {
					$("#add_dep_modal_msg").text(
							"Unresolved error! Send ticket!");
				}
			}
		});
	}
}

function addDepartmentLink(cellvalue, options, rowObject) {
	var departmentID = rowObject.id;
	var clickLink = "<button id='DepartmentRowButton'  onclick='prepareDeleteDepartmentArea("
			+ departmentID
			+ ")'"
			+ "class='btn btn-warning btn-xs'>Delete</button>"
	return clickLink;
}

function deleteDepartment(selectedDeparmentAreaID) {

	console.log("delete dep id: " + selectedDeparmentAreaID);
	$("#delete_dep_modal_btn").prop("disabled", true);

	
			$.ajax({
				type : "POST",
				url : '/Ticket_System/rest/department/deleteDepartment',
				contentType : "application/json",
				mimeType : "application/json",
				data : JSON.stringify(selectedDeparmentAreaID),
				success : function(department) {
					$("#delete_dep_modal_btn").prop("disabled", false);
					$('#deps_jqGrid').trigger('reloadGrid');
					$('#delete_department_modal_msg').text("Department deleted Closing Window in 2sec..")
					
					setTimeout(function() {
						$('#delete_department_modal').modal('hide');
						$("#delete_department_modal_msg").text("");
					}, 2000);
				},
				error : function(jqXHR, textStatus, errorThrown) {
					if (jqXHR.status == 409) {
						$("#delete_department_modal_msg").text("You can't delete this department, users or tickets using this !");
						$("#delete_dep_modal_btn").prop("disabled", true);
						setTimeout(function() {
							$('#delete_department_modal').modal('hide');
							$("#delete_department_modal_msg").text("");
							$("#delete_dep_modal_btn").prop("disabled", false);
						}, 4000);
					} else {
						$("#delete_department_modal_msg").text(
								"Unresolved error! Send ticket!");
					}
				}
			});

}

function prepareDeleteDepartmentArea(depDeleteAreaID) {
	selectedDeparmentAreaID = depDeleteAreaID;
	$("#delete_department_modal").modal('show');

}