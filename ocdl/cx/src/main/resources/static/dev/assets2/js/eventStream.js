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
                for (var i = 0; i < data["personal_event"].length; i++) {
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
                for (let e in data.personal_event) {
                    let newTagHtml = "<span class=\"badge badge-primary\">New</span>";
                    if (e.newFlag) {
                        $("#" + e.modelId + "_td").append(newTagHtml);
                    }
                }

                <!-- approval list -->
                for (var i = 0; i < data.globle_event.length; i++) {
                    var tr;

                    tr = "<tr class='data'><td>" + data.globle_event[i].modelName + "</td> " +
                        "<td>" + data.globle_event[i].ownerName + "</td> <td>" + data["approvalModels"][i].algorithm + "</td>  " +
                        "<td>" + data.globle_event[i].version + "</td> <td>" +
                        data.globle_event[i].status +
                        "</td>" +
                        "<td>" +
                        data.globle_event[i].operatorName +
                        "</td>" +
                        "<td>" + data.globle_evnet[i].timeStamp + "</td>" +
                        "</tr>";

                    $("#table-globle-event").append(tr);
                }

                for (let e in data.globle_event) {
                    let newTagHtml = "<span class=\"badge badge-primary\">New</span>";
                    if (e.newFlag) {
                        $("#" + e.modelId + "_td").append(newTagHtml);
                    }
                }

            }, function (response) {
                alert(response.message)
            })
        }
    })
}