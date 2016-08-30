function loadAllCompanies(selectID) {

	var compSelectID = "#" + selectID;

	$(compSelectID).html(" "); // kutucugu temizle

	$.ajax({
		type : "GET",
		url : '/Ticket_System/rest/company/getAllCompanies',
		contentType : "application/json",
		mimeType : "application/json",
		success : function(companies) {
			$.each(companies, function(key, value) {
				// key == value.id db te id sutunu tutuyoruz...
				$(compSelectID).append(
						$("<option></option>").attr("value", value.id).text(
								value.name));
			});
		},
		error : function() {
			console.log("getAllCompanies error");
		}
	});
}

var selectedCompanyAreaID;

function prepareAddCompanyArea(areaID) {
	selectedCompanyAreaID = areaID;
	$('#add_company_modal_msg').text('');
	$('#new_company_fax').val('');
	$('#new_company_phone').val('');
	$('#new_company_email').val('');
	$('#new_company_name').val('');
	$('#new_company_address').val('');
	$("#add_company_modal_btn").prop("disabled", false);
	$("#add_company_modal").modal("show");
}

function addCompany() {

	var cEmail = $("#new_company_email").val();
	var cName = $("#new_company_name").val();
	var cPhone = $("#new_company_phone").val();
	var cFax = $("#new_company_fax").val();
	var cAddress = $("#new_company_address").val();

	if (cEmail == "" || cName == "") {
		$("#add_company_modal_msg").text("You have to fill required(*) places");
	} else {

		var company = {
			email : cEmail,
			name : cName,
			phone : cPhone,
			fax : cFax,
			address : cAddress
		};

		$.ajax({
			method : "POST",
			url : '/Ticket_System/rest/company/addCompany',
			contentType : "application/json",
			mimeType : "application/json",
			data : JSON.stringify(company),
			success : function(result) {
				$("#add_company_modal_btn").prop("disabled", true);
				$("#add_company_modal_msg").text(
						"Company added. Closing Window in 2sec..");
				$('#' + selectedCompanyAreaID).append(
						$("<option></option>").attr("value", result.id).attr(
								"selected", true).text(result.name));
				$('#comp_jqGrid').trigger('reloadGrid');
				setTimeout(function() {
					$('#add_company_modal').modal('hide');
				}, 2000);
			},
			error : function(jqXHR, textStatus, errorThrown) {
				if (jqXHR.status = 409) {
					$("#add_company_modal_msg").text(
							"Similar company name exist in system!");
				} else {
					$("#add_company_modal_msg").text(
							"AddCompany Error Occured!");
				}
				console.log("test AddCompany");
				console.log(jqXHR);
				console.log("testend AddCompany");
			}
		});
	}
}

function loadCompaniesPage() {
	$("#comp_jqGrid").GridUnload();
	$("#comp_jqGrid").jqGrid({
		caption : "COMPANIES LIST",
		url : "/Ticket_System/rest/company/getAllCompanies",
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
			label : "Company Name",
			name : 'name',
			width : 85
		}, {
			label : "Address",
			name : 'address',
			width : 140
		}, {
			label : "E-Mail",
			name : 'email',
			width : 85
		}, {
			label : "Phone",
			name : 'phone',
			width : 85
		}, {
			label : "Fax",
			name : 'fax',
			width : 85
		}, {
			label : "Detail",
			name : 'id',
			width : 40,
			formatter : addCompanyLink
		} ],
		viewrecords : true,
		height : 400,
		width : 890,
		// loadonce: true,
		rowNum : 100,
		styleUI : 'Bootstrap',
		pager : "#comp_jqGridPager",
		emptyrecords : "Nothing to display"
	});

	$('#comp_jqGrid').navGrid('#comp_jqGridPager', {
		edit : false,
		add : false,
		del : false,
		search : true,
		refresh : true,
		view : false,
		position : "left",
		cloneToTop : false
	}).navButtonAdd('#comp_jqGridPager', {
		caption : "Add",
		buttonicon : "ui-icon-add",
		onClickButton : function() {
			$("#add_company_modal").modal("show");
		}
	});

}

function addCompanyLink(cellvalue, options, rowObject) {
	var CompanyID = rowObject.id;
	var clickLink = "<button onclick='getCompany(" + CompanyID + ")'"
			+ "class='btn btn-warning btn-xs'>Detail</button>"
	return clickLink;
}

var selectedCompanyID;
function getCompany(companyID) {

	console.log("company id : " + companyID);
	$.ajax({
		method : "POST",
		url : '/Ticket_System/rest/company/getCompany',
		contentType : "application/json",
		mimeType : "application/json",
		data : JSON.stringify(companyID),
		success : function(company) {
			selectedCompanyID = company.id;

			// console.log("id: "+ result.id + "result.name: " + result.name +"
			// result.email : " +result.email);
			$("#selected_company_name").val(company.name);
			$("#selected_company_email").val(company.email);
			$("#selected_company_phone").val(company.phone);
			$("#selected_company_address").val(company.address);
			$("#selected_company_fax").val(company.fax);
			//				
			$("#detail_company_modal").modal('show');
			// setTimeout(function() {
			// $('#add_company_modal').modal('hide');
			// }, 2000);
		},
		error : function(jqXHR, textStatus, errorThrown) {
			if (jqXHR.status = 409) {
				$("#add_company_modal_msg").text(
						"Similar company name exist in system!");
			} else {
				$("#add_company_modal_msg").text("AddCompany Error Occured!");
			}
			console.log("test getCompany");
			console.log(jqXHR);
			console.log("testend getCompany");
		}
	});

}

function prepareDeleteCompanyArea() {
	$("#delete_company_modal_msg").text("");
	$("#delete_company_modal_btn").prop("disabled", false);
	$("#delete_company_modal").modal("show");
}
function deleteCompanyData() {

	$("#delete_company_modal_btn").prop("disabled", true);

	console.log("deleteCompanyData selectedCompanyID : " + selectedCompanyID);

	$.ajax({
		url : "/Ticket_System/rest/company/deleteCompanyData/",
		type : "POST",
		mimeType : "application/json",
		contentType : "application/json",
		data : JSON.stringify(selectedCompanyID),
		success : function() {
			$("#delete_company_modal_btn").prop("disabled", false);
			$("#delete_company_modal_msg").text(
					"Company deleted. Closing Window in 2sec..");
			$('#comp_jqGrid').trigger('reloadGrid');
			setTimeout(function() {
				$('#detail_company_modal').modal('hide');
				$('#delete_company_modal').modal('hide');
			}, 2000);
		},
		error : function(jqXHR, textStatus, errorThrown) {
			console.log("error :" + errorThrown);
		}
	});

}