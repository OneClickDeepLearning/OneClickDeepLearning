var user_name='';
var project_name='';
var list;
var token='';
var id='';
var profileImage='';



window.onload = function () {
    /*				dp.SyntaxHighlighter.ClipboardSwf = 'assets/js/clipboard.swf';
                    dp.SyntaxHighlighter.HighlightAll('code');*/
    initTemplateList();
    initProjectName();

    function addSpan(li,text){
        var span_1=document.createElement("span");
        span_1.innerHTML=text;
        li.appendChild(span_1);
    }
    function addLi(content, parent){
        var li_1=document.createElement("li");
        li_1.setAttribute("class","d-secondNav s-secondNav");
        li_1.setAttribute("onclick","javascript:getCode('"+content+"','"+parent+"');");
        addSpan(li_1,content);
        document.getElementById(parent).appendChild(li_1);
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

    function initTemplateList(){
        $.ajax({
            url: enviorment.API.TEMPLATE_LIST,
            contentType: 'application/json',
            dataType: "json",
            type: "GET",
            success:function (data) {
                ajaxMessageReader(data,function (data) {
                    var layerList=data[0];
                    var blockList=data[1];
                    var networksList=data[2];
                    var frameworks = data[3];
                    for(var i=0; i<layerList.length;i++){
                        addLi(layerList[i],"Layers");
                    }
                    for(var i=0; i<blockList.length;i++){
                        addLi(blockList[i],"Blocks");
                    }
                    for(var i=0; i<networksList.length;i++){
                        addLi(networksList[i],"Networks");
                    }
                    for(var i=0; i<frameworks.length;i++){
                        addLi(frameworks[i],"Frameworks");
                    }
                })
            },

            error: function (data) {
            }
        })
    }
}

/*
	$('#serverCtl').on('click', function(e){
		if($("#serverCtl").hasClass('toggle--on')){
			selectJupyterServer(0); //Connect CPU Server
		}else {
			selectJupyterServer(1); //Connect GPU Server
		}

	});*/

function ShowApprovalPortal(content, parent) {
    var li_1=document.createElement("li");
    var a=document.createElement("a");
    a.setAttribute("onclick","forwardTo('views/approvalCenter.html?token="+token+"')");
    a.setAttribute("href","#");
    a.innerHTML=content;
    li_1.appendChild(a);
    document.getElementById(parent).appendChild(li_1);
}
function ShowConfigurationPortal(content, parent){
    var li_1=document.createElement("li");
    var a=document.createElement("a");
    a.setAttribute("onclick","forwardTo('views/configuration.html?token="+token+"')");
    a.setAttribute("href","#");
    a.innerHTML=content;
    li_1.appendChild(a);
    document.getElementById(parent).appendChild(li_1);
}

function ajaxMessageReader(response, func){
    if(response.code=="200"){
        func(response.data);
    }else{
        alert(response.get("message"));
    }

    /*		func(response);*/
}

function changeProjectName() {
    var name = $("#projectName").text().slice(9);

    $.ajax({
        url: enviorment.API.PROJECT_NAME,
        contentType: 'application/json',
        dataType: "json",
        data:
            JSON.stringify({
                name: name
            }),
        type: "PUT",
        timeout: 0,
        beforeSend: function (xhr) {
            xhr.setRequestHeader("AUTH_TOKEN", token);
        },
        success: function(data){
            ajaxMessageReader(data,function (data) {
            })
        },
        error: function (data) {
        }
    })

}
function checkLogin() {
    if($("#username").val()==''){
        alert("please input the username");
        return false;
    }else if($("password").val()==''){
        alert("please input the password");
        return false;
    }
}

function signIn() {
    console.log($("#data_username").val());
    console.log($("#data_pwd").val());

    $.ajax({
        url: enviorment.API.LOGIN_PWD,
        contentType: 'application/json',
        dataType: "json",
        type: "POST",
        data:
            JSON.stringify({
                account: $("#data_username").val(),
                password: $("#data_pwd").val()
            }),
        success:function (data) {
            ajaxMessageReader(data,function (data) {
                token=data['token'];
                user_name=data['userName'];

                if(data['role']=="MANAGER"){
                    ShowApprovalPortal("MODEL CENTER","nav-menu");
                }
                ShowConfigurationPortal("CONFIGURE","nav-menu");


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


                selectJupyterServer();
            })
        },

        error: function (data) {
        }
    })
}


function getCode(name,type) {
    $.ajax({
        url: enviorment.API.TEMPLATE_CODE+"?name="+name+"&type="+type,
        contentType: 'application/json',
        dataType: "json",
        type: "GET",
        success:function (data) {
            ajaxMessageReader(data,function (data) {
                var  code = $("#code");
                $(".dp-highlighter").remove();
                code.text(data[0]);
                dp.SyntaxHighlighter.ClipboardSwf = 'assets2/js/clipboard.swf';
                dp.SyntaxHighlighter.HighlightAll('code');
                jsAnimateMenu();
                if(document.getElementById("card").className.search("flip") === -1){
                    flipPanel();
                }
            })
        },

        error: function (data) {
        }
    })
}

function submitToGit(){
    $.ajax({
        url: enviorment.API.MODEL,
        contentType: 'application/json',
        dataType: "json",
        type: "POST",
        timeout: 0,
        beforeSend: function (xhr) {
            xhr.setRequestHeader("AUTH_TOKEN", token);
        },
        success: function(data){
            ajaxMessageReader(data,function (data) {
                alert("Push successful!");
            })
        },
        error: function () {
        }
    })
}

function selectJupyterServer(){
    var server = "cpu";
    if($("#serverCtl").hasClass('toggle--on')){
        server = "cpu";
    }else{
        server = "gpu";
    }
    $.ajax({
        url: enviorment.API.JUPYTER_SERVER+"/"+server,
        contentType: 'application/json',
        dataType: "json",
        type: "POST",
        beforeSend: function (xhr) {
            xhr.setRequestHeader("AUTH_TOKEN", token);
            $("#resourceLoading").show();
            $("#resourceLoadingBar").show();
            $("#Section1_label").text("Launching the resource, please wait..");
        },

        timeout: 0,

        success: function(data) {
            $("#resourceLoading").hide();
            $("#resourceLoadingBar").hide();
            $("#jupyterFrame").show();
            ajaxMessageReader(data,function (data) {
                /*				    if(data["url"].contains(".")){*/
                $('#jupyterFrame').attr('src', "http://"+data["url"]+"/notebooks/MySpace");
                /*                    }else{
                                        alert(data['url']);
                                    }*/
            })
        },
        error: function () {
            $("#resourceLoading").show();
            $("#resourceLoadingBar").hide();
            $("#jupyterFrame").hide();
            $("#Section1_label").text("Sorry, Fail to load resource!");
        },
        complete:function(){

        }
    })
}


function forwardTo(url){
    window.top.location.href=url;
}

$('#projectName').click(function(){
    var td=$(this); //为后面文本框变成文本铺垫
    var text=$(this).text();
    var input=$('<input type="text" class="edit" value="'+text.slice(9)+'">');
    $(this).html( input );

    $('input').click(function(){
        return false;
    }); //阻止表单默认点击行为

    $('input').select(); //点击自动全选中表单的内容

    $('input').blur(function(){
        var nextxt=$(this).val();
        td.html("Project: "+nextxt);
        changeProjectName();
    }); //表单失去焦点文本框变成文本

});

var animation=0;
function loadAnimationBlue() {
    if(animation==1){
        $("#card front").removeClass("green");
        $("#card front").addClass("blue");
        setTimeout(loadAnimationGreen(),1000);
    }
}
function loadAnimationGreen(){
    if(animation==1) {
        $("#card front").removeClass("blue");
        $("#card front").addClass("green");
        setTimeout(loadAnimationBlue(), 1000);
    }
}
function cancleAnimation() {
    animation=0;
    $("#card front").removeClass("blue");
    $("#card front").removeClass("green");
}

function onSignIn(googleUser) {
    // Useful data for your client-side scripts:
    var profile = googleUser.getBasicProfile();
    console.log("ID: " + profile.getId()); // Don't send this directly to your server!
    id = profile.getId();
    console.log('Full Name: ' + profile.getName());
    user_name = profile.getName();
    console.log('Given Name: ' + profile.getGivenName());
    console.log('Family Name: ' + profile.getFamilyName());
    console.log("Image URL: " + profile.getImageUrl());
    profileImage = profile.getImageUrl();
    console.log("Email: " + profile.getEmail());

    // The ID token you need to pass to your backend:
    var id_token = googleUser.getAuthResponse().id_token;
    console.log("ID Token: " + id_token);

   /* $("#userinfo").show(1000);*/
    $("#userinfo").slideDown();
    $.ajax({
        url: enviorment.API.LOGIN_OAUTH,
        contentType: 'application/json',
        dataType: "json",
        type: "POST",
        timeout: 0,
        data: JSON.stringify({
            id: id
        }),
        success: function(data){

            var username=$("#username");
            var status=$("#status");
            var rescource=$("#rescourse");
            var card=$("#card");

            username.text(user_name);
            if (profileImage!=null&&profileImage!=''){
                $("#profileImage").attr("src",profileImage);
                $("#profileImage").removeClass("hide");
            }

            status.removeClass('status_disconnected');
            status.addClass('status_connected');
            rescource.removeClass('status_NoneR');
            rescource.addClass('status_cpu');
            card.removeClass('unlog');

            $("#loginBtnGroup").slideUp();
    /*        $("#signInBtn").addClass("hide");
            $("#signUpBtn").addClass("hide");*/
            $("#closeLogin").click();
        },
        error: function () {
        }
    })

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
        url: enviorment.API.DELETE_SERVER,
        contentType: 'application/json',
        dataType: "json",
        type: "DELETE",
        timeout: 0,
        success: function(data){
        },
        error: function () {
            alert("Fail to release user Resources ");
        }
    })
}