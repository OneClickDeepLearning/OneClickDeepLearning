
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

                <!-- personal event list -->
                for (var i = 0; i < data["personal_event"].length; i++) {
                    var tr = "<tr class='data'><td>" + data["personal_event"][i].modelName + "</td><td>" +
                        data["personal_event"][i].algorithm +
                        "</td><td>" + data["personal_event"][i].version +
                        "</td>" +
                        "<td>" + data["personal_event"][i].status + "</td> " +
                        "<td>" + data["personal_event"][i].lastOperation + "</td>" +
                        "<td>" + data["personal_event"][i].updateTime + "</td>" +
                        "</tr>";

                    $("#table-personal-event").append(tr);


                }

                <!-- approval list -->
                for (var i = 0; i < data["globle_event"].length; i++) {
                    var tr;

                    tr = "<tr class='data'><td>" + data["globle_event"][i].modelName + "</td> " +
                        "<td>" + data["globle_event"][i].eventOwner + "</td> <td>" + data["approvalModels"][i].algorithm + "</td>  " +
                        "<td>" + data["globle_event"][i].version + "</td> <td>" +
                        data["globle_event"][i].status +
                        "</td>" +
                        "<td>" +
                        data["globle_event"][i].operatorName +
                        "</td>" +
                        "<td>" + data["globle_evnet"][i].updateTime + "</td>" +
                        "</tr>";

                    $("#table-globle-event").append(tr);
                }

            }, function (response) {
                alert(response.message)
            })
        }
    })
}