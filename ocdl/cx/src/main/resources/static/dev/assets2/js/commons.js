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
            },function (data) {
                alert(data.message)
            })
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

    initEvent();

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




function signOut() {
    var auth2 = gapi.auth2.getAuthInstance();
    auth2.signOut().then(function () {
        console.log('User signed out.');
    });
    $("#userinfo").slideUp();
    $("#loginBtnGroup").slideDown();


    releaseResource();
    HideConfigurationPortal();
    ShowInitMenu();

    token = "";
    JumpToWithToken("index.html");
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
            alert("User resource released")
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
            },function () {
            })
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

function ajaxMessageReader(response, func, errfunc){
    if(response.code=="200"){
        func(response.data);
    }else{
        errfunc(response);

    }

}

function changeAgentContent() {
    document.getElementById("fileAgent").value = document.getElementById("file").value;
}



//TODO replace the sinup function to this
function createAccount(username,password,role, func) {
    if(key == null ||key ==''){
        getPublicKey();
    }

    $.ajax({
        url:enviorment.API.REGISTER,
        type: "POST",
        contentType: 'application/json',
        data:
            JSON.stringify({
                username: username,
                password:password,
                role:role
            }),
        dataType: "json",
        error: function(request) {
            alert("Connection error");
        },
        success: function(data) {
            ajaxMessageReader(data,function (data) {
                func(data)
            },function (data) {
                alert(data.message)
            })
        }

    })
}

//TODO replace the updateConfiguration function to this
function modifyConfiguration(projectName,gitPath,k8Url,templatePath,suffix,algorithm,func) {
    $.ajax({
        url: enviorment.API.PROJECT_UPDATE,
        contentType: 'application/json',
        dataType: "json",
        beforeSend: function (xhr) {
            xhr.setRequestHeader("AUTH_TOKEN", token);
        },
        data:
            JSON.stringify({
                projectName: projectName,
                gitPath: gitPath,
                k8Url: k8Url,
                templatePath: templatePath,
                suffix:suffix,
                algorithm:algorithm
            }),
        type: "PUT",
        timeout: 0,
        success: function(data){
            ajaxMessageReader(data,function (data) {
                func(data);
            },function (data) {
                alert(data.message)
            })
        },
        error: function (data) {
        }
    })
}