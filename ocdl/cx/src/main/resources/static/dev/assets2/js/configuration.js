token = GetQueryString("token");
initConfigInfo();
initUserInfo();



function initConfigInfo() {
    $.ajax({
        url: enviorment.API.PROJECT,
        contentType: 'application/json',
        dataType: "json",
        type: "GET",
        beforeSend: function (xhr) {
            xhr.setRequestHeader("AUTH_TOKEN", token);
        },
        success: function(data) {
            ajaxMessageReader(data,function (data) {
                var projectName=$("#name");
                var projectName2=$("#projectName");
                // var k8Url= $("#k8Url");
                // var git = $("#git");
                var templatePath = $("#templateUrl");
                var suffix = $("#suffix");
                var algorithm = $("#algorithm");

                projectName.val(data['projectName']);
                // k8Url.val(data['k8Url']);
                // git.val(data['gitPath']);
                templatePath.val(data['templatePath']);
                projectName2.text("Project:"+data['projectName']);
                suffix.val(data['suffix']);
                algorithm.val(data['algorithm']);

            })
        },
        error: function (data) {
        }
    })
}

function updateConfiguration() {
    $.ajax({
        url: enviorment.API.PROJECT_UPDATE,
        contentType: 'application/json',
        dataType: "json",
        beforeSend: function (xhr) {
            xhr.setRequestHeader("AUTH_TOKEN", token);
        },
        data:
            JSON.stringify({
                projectName: $("#name").val(),
                // gitPath: $("#git").val(),
                // k8Url: $("#k8Url").val(),
                templatePath: $("#templateUrl").val(),
                suffix:$("#suffix").val(),
                algorithm:$("#algorithm").val()
            }),
        type: "PUT",
        timeout: 0,
        success: function(data){
            ajaxMessageReader(data,function (data) {
                $("#projectName").text($("#name").val());
                alert("Update Successful");
            })
        },
        error: function (data) {
        }
    })
}





