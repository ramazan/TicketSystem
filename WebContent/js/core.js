var authenticatedUser;
var isAdmin = false;
var isSupporter = false;
var isClient = false;

$("#navLinks > li").hide();
$("#nav_tickets").show();

$(document).ready(function() {
  showTickets();
  authenticateUser();
});

$('#supporterRole').change(function() {
  if (this.checked)
    $('#departmentFade').fadeIn();
  else
    $('#departmentFade').fadeOut();
});

$('#selectedPersonRoleSup').change(function() {
  if (this.checked)
    $('#departmentInput').fadeIn();
  else
    $('#departmentInput').fadeOut();
});

function authenticateUser() {
  // login to system
  $.get("/Ticket_System/rest/session/login", function() {
    $.ajax({
      type: "POST",
      url: '/Ticket_System/rest/session/getAuthenticatedUser',
      contentType: "application/json",
      mimeType: "application/json",
      success: function(data) {
        authenticatedUser = data;
        $("#nav_nickname").text(authenticatedUser.email);
        // sayfayi izinlere gore hazirla
        $.each(authenticatedUser.userRoles, function(key, value) {
          if (value == "admin")
            isAdmin = true;
          else if (value == "supporter")
            isSupporter = true;
          else
            isClient = true;
        });
        if (isAdmin == true) {
          $("#nav_users").show();
          $("#nav_companies").show();
          $("#nav_deps").show();
        }
      }
    });
  });
}


function logout() {
  $.get("/Ticket_System/rest/session/logout");
  window.location = "/Ticket_System";
}

function showProfile() {

  loadProfileInf();
  $("#user_new_pass").keyup(validate).val("");
  $("#user_new_pass_c").keyup(validate).val("");
  $("#profile_update_alert").hide();

  $("#navs li").removeClass("active");
  $("#nav_profile").addClass("active");

  $("#pages > div").hide();
  $('#profile_page').show();
}

function showTicketDetails(selectedTicket) {
  $('#ticket_details_page').show();

  // ticket benim degilse kapatamam
  if (selectedTicket.sender.id != authenticatedUser.id) {
    $("#ticket_detail_close_btn").hide();
  }

  // admin degilsem editleyemem
  if (isAdmin == false) {
    $("#ticket_detail_delete_btn").hide();
    $("#ticket_detail_edit_btn").hide();
  }

  // kapaliysa kapalidir!
  if (selectedTicket.status == false) {
    $("#ticket_detail_close_btn").hide();
  }
}

function showTickets() {

  loadAllTickets(1);

  $("#navs li").removeClass("active");
  $("#nav_tickets").addClass("active");

  $("#pages > div").hide();
  $('#tickets_page').show();
}

function showCompanies() {
  loadCompaniesPage();
  $("#navs li").removeClass("active");
  $("#nav_companies").addClass("active");

  $("#pages > div").hide();
  $('#companies_page').show();
}

function showUsers() {
  loadAllUsers();

  $("#navs li").removeClass("active");
  $("#nav_users").addClass("active");

  $("#pages > div").hide(); // atasi pages olan divleri gizle
  $('#users_page').show();
}

function showDepartments() {
  loadDepartmentsPage();

  $("#navs li").removeClass("active");
  $("#nav_deps").addClass("active");

  $("#pages > div").hide();
  $('#departments_page').show();
}

function showCompanies() {
  loadCompaniesPage();

  $("#navs li").removeClass("active");
  $("#nav_companies").addClass("active");

  $("#pages > div").hide();
  $("#companies_page").show();
}

$('.dropdown').hover(function() {
  $(this).find('.dropdown-menu').first().stop(true, true).slideDown(350);
}, function() {
  $(this).find('.dropdown-menu').first().stop(true, true).slideUp(100)
});
