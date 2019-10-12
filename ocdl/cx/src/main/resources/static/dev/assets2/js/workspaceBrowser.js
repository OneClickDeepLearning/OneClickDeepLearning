var files =
    [
        {
            "file_name": "resource", "file_type": "folder", "children": [
                {"file_name": "persistence.txt", "file_type": "file", "children": []},
                {"file_name": "static.txt", "file_type": "file", "children": []},
                {"file_name": "bootstrap.txt", "file_type": "file", "children": []}
            ]
        },
        {
            "file_name": "static", "file_type": "folder", "children": [
                {"file_name": "ocdlDesp.txt", "file_type": "file", "children": []}
            ]
        },
        {
            "file_name": "image", "file_type": "folder", "children": [
                {"file_name": "ocdlDesp.png", "file_type": "file", "children": []},
                {"file_name": "ocdlDesp2.png", "file_type": "file", "children": []}
            ]
        },
        {
            "file_name": "ReadMe.MD", "file_type": "file", "children": []
        }
    ];



// Highlight selected row
$("#hadoop_browser tbody").on("mousedown", "tr", function () {
    $(".selected").not(this).removeClass("selected");
    $(this).toggleClass("selected");
});

getFileListFromHDFS();


function getFileListFromHDFS(){
    $.ajax({
        url: enviorment.API.LIST_FILE,
        contentType: 'application/json',
        dataType: "json",
        type: "GET",
        success: function (data) {
            ajaxMessageReader(data, function (data) {
                console.log(data);
                generateFileTree(data);

            }, function (response) {
                $("#template_alert").show();
            })
        }
    })
}


function generateFileTree(files) {
    var fileListBody = $("#file_list");
    renderTree(files, null, fileListBody);
    $("#hadoop_browser").treetable({expandable: true});
}

function renderTree(child, parentId, dom) {
    var domHTML;
    for (var i = 0; i < child.length; i++) {
        var file_name = child[i]["file_name"];
        var file_type = child[i]["file_type"];
        var leafs = child[i]["children"];
        var id;
        if (parentId == null) {
            id = i;
            if (file_type == "folder") {
                domHTML = "<tr data-tt-id='" + i + "'><td><span class='" + file_type + "'>" + file_name + "</span></td><td>" + file_type.toUpperCase() + "</td><td>-- MB</td><td></td></tr>";
                dom.append(domHTML);
                if (leafs.length > 0) {
                    renderTree(leafs, id , dom);
                }
            } else {
                domHTML = "<tr data-tt-id='" + i + "'><td><span class='" + file_type + "'>" + file_name + "</span></td><td>" + file_type.toUpperCase() + "</td><td>-- MB</td><td><button>Download</button></td></tr>";
                dom.append(domHTML);
            }
        } else {
            id = parentId +"-" + i;
            if (file_type == "folder") {
                domHTML = "<tr data-tt-id='" + id + "' data-tt-parent-id='" + parentId + "'><td><span class='" + file_type + "'>" + file_name + "</span></td><td>" + file_type.toUpperCase() + "</td><td>-- MB</td><td></td></tr>";
                dom.append(domHTML);
                if (leafs.length > 0) {
                    renderTree(leafs, id , dom);
                }
            } else {
                domHTML = "<tr data-tt-id='" + id + "' data-tt-parent-id='" + parentId + "'><td><span class='" + file_type + "'>" + file_name + "</span></td><td>" + file_type.toUpperCase() + "</td><td>-- MB</td><td><button>Download</button></td></tr>";
                dom.append(domHTML);
            }
        }

    }

}