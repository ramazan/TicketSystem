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
		    label: "Delete",
		    name: 'id',
		    width: 40,
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
}

/*
 * addPlaceID: departmanlarin ekleneceği select tag idsi selectVal: girilmesi
 * durumunda eşleşen value default seçilir
 */
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
				$("#add_dep_modal_btn").prop("disabled", true);
				$("#add_dep_modal_msg").text(
						"Department added. Closing Window in 2sec..");
				$("#" + selectedDeparmentAreaID).append(
						$("<option></option>").attr("value", result.id).attr(
								"selected", true).text(result.name));
				setTimeout(function() {
					$('#add_dep_modal').modal('hide');
				}, 2000);
			},
			error : function() {
				$("#add_dep_modal_msg").text(
						"addDepartment | An Error Occured!");
			}
		});
	}
}

function addDepartmentLink(cellvalue, options, rowObject) {
	var departmentID = rowObject.id;
	  var clickLink = "<button id='DepartmentRowButton'  onclick='deleteDepartment(" + departmentID + ")'" +
	    "class='btn btn-warning btn-xs'>Delete</button>"
	  return clickLink;
}

function deleteDepartment(departmentID) {

	$("#delete_department_modal").modal('show');
		
	$("#delete_user_modal_btn" ).click(function( event ) {
		
		alert("tıklandı." + departmentID);
//		  $.ajax({
//		    type: "POST",
//		    url: '/Ticket_System/rest/department/deleteDepartment',
//		    contentType: "application/json",
//		    mimeType: "application/json",
//		    data: JSON.stringify(departmentID),
//		    success: function(department) {	    
//		    	
//		        $('#deps_jqGrid').trigger('reloadGrid');
//
//		    },
//		    error: function() {
//		      alert("Department details cannot get please try again. departmentID:  " + departmentID);
//		    }
//		  });
		
	});
		
	console.log("bitti..");
	
}