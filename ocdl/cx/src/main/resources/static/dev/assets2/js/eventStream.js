var myevent;

function initEvent() {

    $.ajax({
        url: enviorment.API.EVENT,
        contentType: 'application/json',
        dataType: "json",
        beforeSend: function (xhr) {
            xhr.setRequestHeader("AUTH_TOKEN", token);
        },
        type: "GET",
        success: function (data) {
            ajaxMessageReader(data, function (data) {
                console.log(data);

                myevent = data;

                <!-- personal event list -->
                for (var i = 0; i < data.personal_event.length; i++) {
                    var tr = "<tr class='data'><td id='" + data.personal_event[i].modelId + "_td'>" + data.personal_event[i].modelName + "</td><td>" +
                        data.personal_event[i].algorithm +
                        "</td><td>" + data.personal_event[i].version +
                        "</td>" +
                        "<td>" + data.personal_event[i].status + "</td> " +
                        "<td>" + data.personal_event[i].lastOperatorName + "</td>" +
                        "<td>" + data.personal_event[i].timeStamp + "</td>" +
                        "</tr>";

                    $("#table-personal-event").append(tr);

                }
                let newPersonalTag = 0;
                let newGlobalTag = 0;
                for (let e in data.personal_event) {
                    let newTagHtml = "<span class=\"badge badge-primary\">New</span>";
                    if (e.newFlag) {
                        $("#" + e.modelId + "_td").append(newTagHtml);
                        newPersonalTag++;
                    }
                }

                <!-- approval list -->
                for (var i = 0; i < data.global_event.length; i++) {
                    var tr;

                    tr = "<tr class='data'><td>" + data.global_event[i].modelName + "</td> " +
                        "<td>" + data.global_event[i].ownerName + "</td> <td>" + data["approvalModels"][i].algorithm + "</td>  " +
                        "<td>" + data.global_event[i].version + "</td> <td>" +
                        data.global_event[i].status +
                        "</td>" +
                        "<td>" +
                        data.global_event[i].operatorName +
                        "</td>" +
                        "<td>" + data.global_event[i].timeStamp + "</td>" +
                        "</tr>";

                    $("#table-global-event").append(tr);
                }

                for (let e in data.global_event) {
                    let newTagHtml = "<span class=\"badge badge-primary\">New</span>";
                    if (e.newFlag) {
                        $("#" + e.modelId + "_td").append(newTagHtml);
                        newGlobalTag++;
                    }
                }



            }, function (response) {
                alert(response.message)
            })
        }
    })
}