var user_name='';
var project_name='';
var list;
var token;
var id='';
var profileImage='';


function tradeToken4UsrInfo() {
    $.ajax({
        url: enviorment.API.USER_INFO_BY_TOKEN+'?token='+token,
        contentType: 'application/json',
        dataType: "json",
        beforeSend: function (xhr) {
            xhr.setRequestHeader("AUTH_TOKEN", token);
        },
        type: "GET",
        success: function(data) {
            ajaxMessageReader(data,function (data) {
                afterSignIn(data);
            })
        },
        error: function (data) {
        }
    })
}


function afterSignIn(data) {
    token = data['token'];
    user_name=data['username'];

    if(data['role']=="MANAGER"){
        ShowManagerMenu();
        initModelTypeList();
    }else{
        ShowDeveloperMenu();
    }

    var status=$("#status");
    var rescource=$("#rescourse");
    var username=$("#username");

    username.text(user_name);
    status.removeClass('status_disconnected');
    status.addClass('status_connected');
    rescource.removeClass('status_NoneR');
    rescource.addClass('status_cpu');

    $("#loginBtnGroup").slideUp();
    $("#userinfo").slideDown();
    $("#closeLogin").click();
    $("#closeSignup").click();

    selectJupyterServer();
}


function afterSignIn_ConfigurationPage(data) {
    token= data['token'];
    user_name=data['username'];

    var status=$("#status");
    var rescource=$("#rescourse");
    var username=$("#username");

    username.text(user_name);
    status.removeClass('status_disconnected');
    status.addClass('status_connected');
    rescource.removeClass('status_NoneR');
    rescource.addClass('status_cpu');

    $("#loginBtnGroup").slideUp();
    $("#userinfo").slideDown();

}


function signOut() {
    var auth2 = gapi.auth2.getAuthInstance();
    auth2.signOut().then(function () {
        console.log('User signed out.');
    });
    $("#userinfo").slideUp();
    $("#loginBtnGroup").slideDown();

    releaseResource();
}

function releaseResource(){
    $.ajax({
        url: enviorment.API.LOGOUT,
        contentType: 'application/json',
        dataType: "json",
        type: "POST",
        beforeSend: function (xhr) {
            xhr.setRequestHeader("AUTH_TOKEN", token);
        },
        timeout: 0,
        success: function(data){
        },
        error: function () {
            alert("Fail to release user Resources ");
        }
    })
}


function initProjectName() {
    $.ajax({
        url: enviorment.API.PROJECT,
        contentType: 'application/json',
        dataType: "json",
        type: "GET",
        success: function(data) {
            ajaxMessageReader(data,function (data) {
                var projectName=$("#projectName");
                projectName.text("Project: "+data.projectName);
            })
        },
        error: function (data) {
        }
    })
}

function JumpToWithToken(path){
    window.location.href = path+"?token="+token;
}
function initUserInfo() {
    if(token!=''){
        tradeToken4UsrInfo();
    }
}

function GetQueryString(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if (r != null) return unescape(r[2]);
    return '';
}

function ajaxMessageReader(response, func){
    if(response.code=="200"){
        func(response.data);
    }else{
        alert(response.message);
    }

}

function changeAgentContent() {
    document.getElementById("fileAgent").value = document.getElementById("file").value;
}