var selectedUserID, selectedUserEmail;


//users tablosu için tüm kullanıcıları getirme işlemi
function loadAllUsers() {

  $("#users_jqGrid").GridUnload();
  $("#users_jqGrid").jqGrid({
    caption: "USER LIST",
    url: "/Ticket_System/rest/user/getAllUsers",
    mtype: "GET",
    datatype: "json",
    colModel: [{
        label: "ID",
        name: 'id',
        width: 35,
        sortable:true,
        sorttype:"int",
        search:true,
        align:"center"
      }, 
       {
      label: "Name",
      name: 'name',
      width: 85
    }, {
      label: "E-Mail",
      name: 'email',
      width: 140
    }, {
      label: "Company",
      name: 'company.name',
      width: 85
    }, {
      label: "Department",
      name: 'department.name',
      width: 95
    }, {
      label: "Roles",
      name: 'userRoles',
      width: 125
    }, {
      label: "Detail",
      name: 'id',
      width: 48,
      formatter: addUserLink
    }],
    viewrecords: true,
    height: 400,
    width: 890,
    rowNum: 10,
    styleUI: 'Bootstrap',
    pager: "#users_jqGridPager",
    emptyrecords: "Nothing to display"
  });

  $('#users_jqGrid').navGrid('#users_jqGridPager', {
    edit: false,
    add: false,
    del: false,
    search: true,
    refresh: true,
    view: false,
    position: "left",
    cloneToTop: false
  }).navButtonAdd('#users_jqGridPager', {
    caption: "Add",
    buttonicon: "ui-icon-add",
    onClickButton: function() {
      $('#add_user_modal').modal('show');
    },
    position: "last"
  });

}

//yeni kullanıcı ekleme işlemi
function addUser() {

	
  var newName = $("#new_user_name").val();
  var newEmail = $('#new_user_email').val();
  var newCompanyID = $('#new_user_company').val();
  var newPassword = $('#new_user_password').val();

  var newRoles = [];
  var userDepartmentID = 0;
  if ($('#adminRole').is(':checked')) {
    newRoles.push("admin");
  }
  if ($('#supporterRole').is(':checked')) {
    newRoles.push("supporter");
    userDepartmentID = $("#new_user_dep").val();
  }
  if ($('#clientRole').is(':checked')) {
    newRoles.push("client");
  }

  if (newRoles.length == 0 || newName == "" || newEmail == "" ||
    newCompanyID == "" || newPassword == "") {
    console.log("error: fill all boxes");
    $("#add_user_msg").html("Please fill all boxes");
  } else {

	  $("#addUserButton").prop("disabled", true);
	  
    var person = {
      name: newName,
      email: newEmail,
      password: newPassword,
      company: {
        id: newCompanyID
      },
      userRoles: newRoles,
      department: {
        id: userDepartmentID
      }
    };

    $.ajax({
      type: "POST",
      url: '/Ticket_System/rest/user/addUser',
      contentType: "application/json",
      mimeType: "application/json",
      data: JSON.stringify(person),
      success: function() {
        $("#add_user_msg").text("User added. Closing Window..");
        // reload jqgrid
        $('#users_jqGrid').trigger('reloadGrid');
        // clear boxes
        $('input:checkbox').removeAttr('checked');
        $('#new_user_name').val('');
        $('#new_user_email').val('');
        $('#new_user_password').val(''); // inputları temizle.
        setTimeout(function() {
          $('#add_user_modal').modal('hide');
          $("#add_user_msg").text("");
          $("#addUserButton").prop("disabled", falsel);

        }, 2000);
      },
      error: function() {
        alert("User cannot added please try again. ");
      }
    });
  }
}

//profile sayfasındaki bilgilerin set edilmesi
function loadProfileInf() {
  var user = authenticatedUser;
  $("#user_email").text(user.email);
  $("#user_name").text(user.name);
  $("#user_roles").text(user.userRoles);
  $("#user_company").text(user.company.name);
}

// Profile sayfasındaki şifre eşleşme kontrolü
function validate() {
  var password1 = $("#user_new_pass").val();
  var password2 = $("#user_new_pass_c").val();

  if (password1 == "" && password2 == "") {
    $("#profile_alert").hide();

  } else if (password1 == password2 && password1 != "" && password2 != "") {
    $("#profile_alert").show();
    $("#pass_validate").text("Şifreler eşleşti!");
    $("#user_update_btn").removeAttr('disabled');

  } else {
    $("#profile_alert").show();
    $("#pass_validate").text("Şifreler eşleşmiyor!");
    $("#user_update_btn").prop("disabled", true);
  }
}


//profile sayfasındaki kullanıcın şifre değiştirme işlemi
function updateProfile() {

  var password = $("#user_new_pass").val();

  $.ajax({
    type: "PUT",
    url: '/Ticket_System/rest/user/updateProfile',
    contentType: "application/json",
    mimeType: "application/json",
    data: password,
    success: function() {
      $("#pass_validate").text("Şifre başarıyla değişti!");
      $("#user_update_btn").prop("disabled", true);
    },
    error: function() {
      $("#pass_validate").text(
        "Şifre değiştirilemedi , tekrar deneyin.");
    }
  });

}


//users tablosundaki detail butonlarının hazırlanması
function addUserLink(cellvalue, options, rowObject) {
  var userID = rowObject.id;
  var clickLink = "<a style='width:50px;' type='button' title='User Details'";
  clickLink += " onclick=\"getUser(" + userID + ")\" ><button class=\"btn btn-info btn-detail \">Detail</button></a>"
  return clickLink;
}

//detail buutonuna tıklandıgında ilgili kullanıcının bilgilerinin getirilmesi
function getUser(userID) {

  loadAllDeparments("selectedPersonDepartment");
  loadAllCompanies("selectedPersonCompany");

  selectedUserID = userID;
  $('#user-detail-modal').modal('show');

  $.ajax({
    type: "GET",
    url: '/Ticket_System/rest/user/getUserInfo/' + userID,
    contentType: "application/json",
    mimeType: "application/json",
    success: function(user) {
      selectedUserEmail = user.email;
      $("#selectedPersonName").val(user.name);
      $("#selectedPersonEmail").val(user.email);
      $("#selectedPersonPassword").val(user.password);
      $("#selectedPersonCompany").val(user.company.id);


      $("#selectedPersonRoleAdmin").prop("checked", false);
      $("#selectedPersonRoleSup").prop("checked", false);
      $("#selectedPersonRoleClient").prop("checked", false);
      $("#departmentInput").hide();

      $.each(user.userRoles, function(key, value) {
        if (value == "admin") {
          $("#selectedPersonRoleAdmin").prop("checked", true);
        } else if (value == "supporter") {
          $("#selectedPersonRoleSup").prop("checked", true);
          $("#departmentInput").show();
          $("#selectedPersonDepartment").val(user.department.id);
        } else if (value == "client") {
          $("#selectedPersonRoleClient").prop("checked", true);
        }

      });

    },
    error: function() {
      alert("user details cannot get please try again. userID:  " +
        userID);
    }
  });
}

//user detail modaldeki seçilen kullanıcının silinmesi
function deleteUserData() {

  console.log("started to delete userID:" + selectedUserID +
    " selected user mail : " + selectedUserEmail);

  $.ajax({
    url: "/Ticket_System/rest/user/deleteUser/" + selectedUserEmail,
    type: "POST",
    mimeType: "application/json",
    contentType: "application/json",
    data: JSON.stringify(selectedUserID),
    success: function() {
      console.log("user deleted!");
      $('#users_jqGrid').trigger('reloadGrid');
      user_detail_msg
      $("#delete_user_status").text("User deleted. Closing Window..");
      setTimeout(function() {
        $('#user-detail-modal').modal('hide');
        $('#delete_user_modal').modal('hide');
        $("#delete_user_status").text("");
      }, 2000);

    },
    error: function(jqXHR, textStatus, errorThrown) {
      console.log("error :" + errorThrown);
    }
  });

}

//user detail update kısmı
function updateUserData() {


  var newName = $("#selectedPersonName").val();
  var newEmail = $('#selectedPersonEmail').val();
  var newCompanyID = $('#selectedPersonCompany').val();
  var newPassword = $('#selectedPersonPassword').val();

  var newRoles = [];
  var userDepartmentID = 0;
  if ($('#selectedPersonRoleAdmin').is(':checked')) {
    newRoles.push("admin");
  }
  if ($('#selectedPersonRoleSup').is(':checked')) {
    newRoles.push("supporter");
    userDepartmentID = $("#selectedPersonDepartment").val();
  }
  if ($('#selectedPersonRoleClient').is(':checked')) {
    newRoles.push("client");
  }

  if (newRoles.length == 0 || newName == "" || newEmail == "" ||
    newCompanyID == "" || newPassword == "") {
    console.log("error: fill all boxes");
    $("#update_user_label").html("Please fill all boxes");
    setTimeout(function() {
      $("#update_user_label").text("");
    }, 2000);
  } else {
	  
	  $("#updateUserDataButton").prop("disabled", true);
    
	  var person = {
      id: selectedUserID,
      name: newName,
      email: newEmail,
      password: newPassword,
      company: {
        id: newCompanyID
      },
      userRoles: newRoles,
      department: {
        id: userDepartmentID
      }
    };

    $.ajax({
      type: "POST",
      url: '/Ticket_System/rest/user/updateUserData/' + selectedUserEmail,
      contentType: "application/json",
      mimeType: "application/json",
      data: JSON.stringify(person),
      success: function() {
        $("#update_user_label").text("User updated. Closing Window..");
        // reload jqgrid
        $('#users_jqGrid').trigger('reloadGrid');
        setTimeout(function() {
          $('#user-detail-modal').modal('hide');
          $('#update_user_modal').modal('hide');
          $("#user_detail_msg").text("");
          $("#update_user_label").text("");
          $("#updateUserDataButton").prop("disabled", false);

        }, 2000);
      },
      error: function() {
        alert("User cannot updated please try again. ");
      }
    });
  }
}


//user detail modaldeki password show/hide işlemi
document.getElementById("showHideButton").addEventListener("click", function(e) {
  var pwd = document.getElementById("selectedPersonPassword");
  if (pwd.getAttribute("type") == "password") {
    pwd.setAttribute("type", "text");
  } else {
    pwd.setAttribute("type", "password");

  }
});
