
function getAllDepartments(){
  $.ajax({
        type : "POST",
        url : '/Ticket_System/rest/department/getAllDepartments',
        contentType : "application/json",
        mimeType : "application/json",
        success : function(departments) {
          $.each(departments, function(key, value) {
            // key == value.id db te id sutunu tutuyoruz...
            $('#userDepartment').append(
                $("<option></option>").attr("value", value.id).text(value.name));
          });
        },

        error : function() {
          alert("User cannot added please try again. ");
        }
      });

}
