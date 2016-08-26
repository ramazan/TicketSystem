var authenticatedUser;
var isAdmin = false;
var isSupporter = false;
var isClient = false;

// login to system
$.get("/Ticket_System/rest/session/login", function() {

  $(document).ready(function() {
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
          console.log(value);
          if (value == "admin")
            isAdmin = true;
          else if (value == "supporter")
            isSupporter = true;
          else
            isClient = true;
        });

        if (isAdmin == false) {
          console.log("nav-user is hided");
          $("#nav_users").hide();
        }
      }
    });

    loadAllTickets(1);
    loadAllDeparments("new_ticket_dep");

    loadAllDeparments("new_user_dep");
    loadAllCompanies("new_user_company");

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

  });
});


function logout() {
  $.get("/Ticket_System/rest/session/logout");
  window.location = "/Ticket_System";
}

function showProfile() {

  loadProfileInf();
  $("#user_new_pass").keyup(validate);
  $("#user_new_pass_c").keyup(validate);


  $('#nav_users').removeClass("active");
  $("#nav_tickets").removeClass("active");

  $('#users_page').hide();
  $('#tickets_page').hide();
  $('#ticket_details_page').hide();
  $('#profile_page').show();
}

function showTicketDetails() {

  $("#nav_users").removeClass("active");
  $('#nav_tickets').removeClass("active");
  $("#nav_profile").addClass("active");

  $('#ticket_details_page').show();

}

function showTickets() {

  loadAllTickets(1);

  $("#nav_profile").removeClass("active");
  $('#nav_users').removeClass("active");
  $("#nav_tickets").addClass("active");
  $('#users_page').hide();
  $('#ticket_details_page').hide();
  $('#profile_page').hide();
  $('#tickets_page').show();
}

function showUsers() {
  loadAllUsers();

  $("#nav_profile").removeClass("active");
  $('#nav_tickets').removeClass("active");
  $("#nav_users").addClass("active");
  $('#users_page').show();
  $('#ticket_details_page').hide();
  $('#profile_page').hide();
  $('#tickets_page').hide();
}

$('.dropdown').hover(function() {
    $(this).find('.dropdown-menu').first().stop(true, true).slideDown(350);
    }, function() {
    $(this).find('.dropdown-menu').first().stop(true, true).slideUp(100)
    });