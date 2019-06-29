


var salesData = [
    {value: 3},
    {value: 5},
    {value: 1}
];

var modelTypeList;


/*var svg = d3.select("#pie").append("svg").attr("width", 700).attr("height", 300);

svg.append("g").attr("id", "salesDonut");
Donut3D.draw("salesDonut", randomData(), 150, 150, 130, 100, 30, 0.4);

Donut3D.draw("salesDonut", salesData, 150, 150, 130, 100, 30, 0.4);*/

/*
token = GetQueryString("token");*/
/*

initProjectName();
initUserInfo();

initModelTypeList();
*/


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
/*                $("#todoNum").text(salesData[0].value);
                Donut3D.draw("salesDonut", salesData, 150, 150, 130, 100, 30, 0.4);*/


                google.charts.load("current", {packages: ["corechart"]});
                google.charts.setOnLoadCallback(function(){
                    drawChart(salesData);
                });

                function drawChart(salesData) {
                    var data = google.visualization.arrayToDataTable([
                        ['Type', 'Number'],
                        ['New', salesData[0].value],
                        ['Approved', salesData[1].value],
                        ['Rejected', salesData[2].value],
                    ]);

                    var options = {
                        title: 'Model Center',
                        is3D: true,
                        backgroundColor: 'transparent',
                        width: 400,
                        height: 300,
                        colors:['#716aca','#007bff','#f4516c']
                    };

                    var chart = new google.visualization.PieChart(document.getElementById('donutchart'));
                    chart.draw(data, options);
                }

                <!-- waiting list -->
                for (var i = 0; i < data["newModels"].length; i++) {
                    var tr = "<tr class='data'><td>" + data["newModels"][i].modelName + "</td><td>" +
                        "<select id=\"modelType" + data["newModels"][i].modelId+"\" >" +
                        "</select>" +
                        "</td> <td>" +
                        "<select id=\"version" + data["newModels"][i].modelId + "\" style=\"color: black;\">\n" +
                        "<option value=\"CACHED_VERSION\">\n" +
                        "CACHED VERSION\n" +
                        "</option>\n" +
                        "<option value=\"RELEASE_VERSION\">\n" +
                        "RELEASE VERSION\n" +
                        "</option>\n" +
                        "</select>" +
                        "</td> \n " +
                        "<td>"+ data["newModels"][i].timeStamp +"</td> <td>" +
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
                    var tr;
                    if(data["approvalModels"][i].status=="RELEASED"){
                        tr = "<tr class='data'><td>" + data["approvalModels"][i].modelName + "</td> <td>" + data["approvalModels"][i].algorithm + "</td> <td>" + data["approvalModels"][i].version + "</td>  " +
                            "<td>"+ data["approvalModels"][i].timeStamp +"</td> <td>" +
                            " <div class=\"btn-group\" role=\"group\" aria-label=\"Basic example\">" +
                            "<button type='button' class='btn btn-info' onclick='generateDownloadCode(\""+ data["approvalModels"][i].modelFileName +"\")'>Download Code</button>" +
                            "<button type=\"button\" class=\"btn btn-danger\" disabled onclick='void(0)'>Undo</button>" +
                            "<button type='button' class='btn btn-info' disabled onclick='void(0)'>Release</button>"+
                            "</div>" +
                            "</td></tr>";
                    }else{
                        tr = "<tr class='data'><td>" + data["approvalModels"][i].modelName + "</td> <td>" + data["approvalModels"][i].algorithm + "</td> <td>" + data["approvalModels"][i].version + "</td>  " +
                            "<td>"+ data["approvalModels"][i].timeStamp +"</td> <td>" +
                            " <div class=\"btn-group\" role=\"group\" aria-label=\"Basic example\">" +
                            "<button type='button' class='btn btn-info' onclick='generateDownloadCode(\""+ data["approvalModels"][i].modelFileName +"\")'>Download Code</button>" +
                            "<button type=\"button\" class=\"btn btn-danger\" onclick='UpdateDecision(\"" + data["approvalModels"][i].modelId + "\",-1,\"approved\")'>Undo</button>" +
                            "<button type='button' class='btn btn-info' onclick='releaseModel(\"" + data["approvalModels"][i].modelId+"\" )'>Release</button>"+
                            "</div>" +
                            "</td></tr>";
                    }

                    $("#tableApproval").append(tr);
                }

                <!-- reject list -->
                for (var i = 0; i < data["rejectedModels"].length; i++) {
                    var tr = "<tr class='data'><td>" + data["rejectedModels"][i].modelName + "</td><td>" + data["rejectedModels"][i].timeStamp + "</td>" +
                        "<td>\n" +
                        " <div class=\"btn-group\" role=\"group\" aria-label=\"Basic example\">" +
                        "<button type=\"button\" class=\"btn btn-danger\" onclick='UpdateDecision(\"" + data["rejectedModels"][i].modelId + "\",-1,\"rejected\")'>Undo</button>" +
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
    var bigVersion='NONE';
    if(status==0){
        decision="rejected";
    }else if(status==1){
        decision="approved";
        var indexModel=document.getElementById("modelType"+id).selectedIndex;
        modelType=document.getElementById("modelType"+id).options[indexModel].value;
        var indexVersion=document.getElementById("version"+id).selectedIndex;
        bigVersion = document.getElementById("version"+id).options[indexVersion].value;
    }else if(status==-1){
        decision="new";
    }

    $.ajax({
        url: enviorment.API.MODEL+"/"+id+"?fromStatus="+origin+"&toStatus="+decision+"&upgradeVersion="+bigVersion,
        contentType: 'application/json',
        dataType: "json",
        data:
            JSON.stringify({
                modelId: id,
                status: origin,
                algorithm: modelType
            }),
        type: "POST",
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

function releaseModel(id) {
    $.ajax({
        url: enviorment.API.MODEL+"/"+id,
        contentType: 'application/json',
        dataType: "json",
        type: "PATCH",
        timeout: 0,
        beforeSend: function (xhr) {
            xhr.setRequestHeader("AUTH_TOKEN", token);
        },
        success: function (data) {
            ajaxMessageReader(data, function (data) {
                initApproralCenterInfo();
                alert("The model has been released!")
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
