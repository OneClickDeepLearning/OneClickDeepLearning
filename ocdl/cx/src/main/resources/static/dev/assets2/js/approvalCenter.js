
var token = GetQueryString("token");

var salesData = [
    {label: "Waiting", value: 3, color: "#3366CC"},
    {label: "Reject", value: 5, color: "#DC3912"},
    {label: "Approval", value: 1, color: "#109618"}
];

var modelTypeList;


var svg = d3.select("#pie").append("svg").attr("width", 700).attr("height", 300);

svg.append("g").attr("id", "salesDonut");
Donut3D.draw("salesDonut", randomData(), 150, 150, 130, 100, 30, 0.4);

Donut3D.draw("salesDonut", salesData, 150, 150, 130, 100, 30, 0.4);
initModelTypeList();





function GetQueryString(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if (r != null) return unescape(r[2]);
    return null;
}

function initApproralCenterInfo() {
    $("#tableNew .data").remove();
    $("#tableApproval .data").remove();
    $("#tableRejected .data").remove();


    $.ajax({
        url: enviorment.API.MODEL,
        contentType: 'application/json',
        dataType: "json",
        beforeSend: function (xhr) {
            xhr.setRequestHeader("AUTH_TOKEN", token);
        },
        type: "GET",
        success: function (data) {
            ajaxMessageReader(data, function (data) {
                salesData[0].value = data['newModels'].length;
                salesData[1].value = data['approvalModels'].length;
                salesData[2].value = data['rejectedModels'].length;
                $("#newModelNum").text(salesData[0].value);
                $("#approvalModelNum").text(salesData[1].value);
                $("#rejectedModelNum").text(salesData[2].value);
                $("#todoNum").text(salesData[0].value);
                Donut3D.draw("salesDonut", salesData, 150, 150, 130, 100, 30, 0.4);

                <!-- waiting list -->
                for (var i = 0; i < data["newModels"].length; i++) {
                    var tr = "<tr class='data'><td>" + data["newModels"][i].modelName + "</td><td>" + data["newModels"][i].project + "</td><td>" +
                        "<select id=\"modelType" + data["newModels"][i].modelId+"\" >" +
                        "</select>" +
                        "</td> <td>" +
                        "<select id=\"version" + data["newModels"][i].modelId + "\" style=\"color: black;\">\n" +
                        "<option value=\"2\">\n" +
                        "Small Update\n" +
                        "</option>\n" +
                        "<option value=\"1\">\n" +
                        "Large Update\n" +
                        "</option>\n" +
                        "</select>" +
                        "</td>  " +
                        "<td>\n" +
                        " <div class=\"btn-group\" role=\"group\" aria-label=\"Basic example\">" +
                        "<button type=\"button\" class=\"btn btn-success\" onclick='UpdateDecision(\"" + data["newModels"][i].modelId + "\",1,\"new\")'>Approve</button>" +
                        "<button type=\"button\" class=\"btn btn-danger\" onclick='UpdateDecision(\"" + data["newModels"][i].modelId+"\",0,\"new\")'>Reject</button>" +
                        "</div>" +
                        "</td></tr>";

                    $("#tableNew").append(tr);

                    for (var j = 0; j < modelTypeList.length; j++) {
                        var option = document.createElement("OPTION");
                        option.text = modelTypeList[j];
                        option.value = modelTypeList[j];
                        document.getElementById("modelType" + data["newModels"][i].modelId).options.add(option);
                    }

                }

                <!-- approval list -->
                for (var i = 0; i < data["approvalModels"].length; i++) {
                    var tr = "<tr class='data'><td>" + data["approvalModels"][i].modelName + "</td><td>" + data["approvalModels"][i].project + "</td><td>" + data["approvalModels"][i].modelType + "</td> <td>" + data["approvalModels"][i].version + "</td>  " +
                        "<td>\n" +
                        " <div class=\"btn-group\" role=\"group\" aria-label=\"Basic example\">" +
                        "<button type=\"button\" class=\"btn btn-danger\" onclick='UpdateDecision(\"" + data["approvalModels"][i].modelId + "\",-1,\"approval\")'>Undo</button>" +
                        "</div>" +
                        "</td></tr>";
                    $("#tableApproval").append(tr);
                }

                <!-- reject list -->
                for (var i = 0; i < data["rejectedModels"].length; i++) {
                    var tr = "<tr class='data'><td>" + data["rejectedModels"][i].modelName + "</td><td>" + data["rejectedModels"][i].project + "</td>" +
                        "<td>\n" +
                        " <div class=\"btn-group\" role=\"group\" aria-label=\"Basic example\">" +
                        "<button type=\"button\" class=\"btn btn-danger\" onclick='UpdateDecision(\"" + data["rejectedModels"][i].modelId + "\",-1,\"reject\")'>Undo</button>" +
                        "</div>" +
                        "</td></tr>";
                    $("#tableRejected").append(tr);
                }


            })
        },
        error: function (data) {
        }
    })
}

function initModelTypeList() {
    $.ajax({
        url: enviorment.API.MODEL_TYPE,
        contentType: 'application/json',
        dataType: "json",
        type: "GET",
        beforeSend: function (xhr) {
            xhr.setRequestHeader("AUTH_TOKEN", token);
        },
        success: function (data) {
            ajaxMessageReader(data, function (data) {
                modelTypeList = data;
                initApproralCenterInfo();
            })
        },
        error: function (data) {
        }
    })
}


function UpdateDecision(id,status,origin) {
    var decision='';
    var modelType=-1;
    var bigVersion=-1;
    if(status==0){
        decision="Reject";
    }else if(status==1){
        decision="Approval";
        var indexModel=document.getElementById("modelType"+id).selectedIndex;
        modelType=document.getElementById("modelType"+id).options[indexModel].value;
        var indexVersion=document.getElementById("version"+id).selectedIndex;
        bigVersion = parseInt(document.getElementById("version"+id).options[indexVersion].value);
    }else if(status==-1){
        decision="Undo";
    }

    $.ajax({
        url: enviorment.API.MODEL+id+"",
        contentType: 'application/json',
        dataType: "json",
        data:
            JSON.stringify({
                modelId: id,
                modelType: modelType,
                destStatus:decision,
                preStatus:origin,
                bigVersion:bigVersion
            }),
        type: "PUT",
        timeout: 0,
        beforeSend: function (xhr) {
            xhr.setRequestHeader("AUTH_TOKEN", token);
        },
        success: function (data) {
            ajaxMessageReader(data, function (data) {
                initApproralCenterInfo();
            })
        },
        error: function (data) {
        }
    })
}

function randomData() {
    return salesData.map(function (d) {
        return {label: d.label, value: 1000 * Math.random(), color: d.color};
    });
}

function ajaxMessageReader(response, func) {
    if (response.code == "400") {
    } else if (response.code == "200") {
        func(response.data);
    }

    /* func(response);*/
}