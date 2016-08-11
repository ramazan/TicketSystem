
function getAllTickets(){
  $("#jqGrid").jqGrid({
    caption: "Ticket List",
    url: "/Ticket_System/rest/ticket/getAllTickets",
    datatype: "json",
    mtype: 'GET',
    colModel:[
      {label:"ID", name:'id', width:25, formatter:'showlink',formatoptions:{baseLinkUrl:'###', idName:'ticketID'}},
      {label:"Date", name:'date', width:60, formatter:'date',formatoptions: {srcformat:'Y-m-d H:i:s', newformat:'M d g:i A'}},
      {label:"Title", name:'title', width:100},
      {label:"From", name:'from', width:100},
      {label:"Department", name:'department', width:80}
    ],
    width:780,
    styleUI: 'Bootstrap',
    responsive: true,
    viewrecords: true,
    multiselect: true,
    height: 150,
    rowNum: 10,
    pager: "#jqGridPager"
  });

  $('#jqGrid').navGrid('#jqGridPager',
   {
     edit: false,
     add: false,
     del: false,
     search: true,
     refresh: true,
     view: true,
     position: "left",
     cloneToTop: false
   });
}



function addTicket(){


  var newTitle = $("#ticket_subject").val();
  var newMessage = $("#ticket_message").val();
  if(newTitle=="" || newMessage==""){
    $("#error_message").text("Please fill all boxes");
  }else{
    var newDepartment = $("#ticket_deparment option:selected").text();
    var newPriority = $("#ticket_priority option:selected").text();

    var ticket={sender:"hmenn",title:newTitle,message:newMessage,department:newDepartment,priority:newPriority};
    $.ajax({
        type: "POST",
        url: '/Ticket_System/rest/ticket/addTicket',
        contentType : "application/json",
        mimeType: "application/json",
        data : JSON.stringify(ticket),
        success : function(){
          console.log("ticket add ajax called")
          }
      });


  }
}
