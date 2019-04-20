function modelTest() {
    var formData = new FormData();
    formData.append("file", document.getElementById("fileInput").files[0]);
    $.ajax({
        url: "/rest/picture",
        data: formData,
        type: "POST",
        contentType: false,//这里
        processData: false,//这两个一定设置为false
        success: function (info) {
            alert(info);
            if (info == "success") {
                alert("成功上传");
            } else {
                alert(info);
            }
        }
    });
}
//点击本地上传文件
$('#btn').click( () => {
    $('#fileInput').click();
})
$('#fileInput').change( (event) => {
    var files = event.target.files;
    pics = files;
    appendFile(files, '.list-btn');
})


//拖拽上传文件 在页面进行预览 上传form用到ajax
const dragbox = document.querySelector('.dragFile');
dragbox.addEventListener('dragover', function (e) {
    e.preventDefault(); // 必须阻止默认事件
}, false);
dragbox.addEventListener('drop', function (e) {
    e.preventDefault(); // 阻止默认事件
    var files = e.dataTransfer.files; //获取文件
    appendFile(files, '.list-btn')
    // code
}, false);

function appendFile (files, listName) {
    for( file of files ) {
        let url = window.URL.createObjectURL(file);
        let liStr = `
            <li class="list-group-item">
              <div>
                <img src="${url}" alt="文件" />
              </div>
            </li>
          `;
        $(listName).append(liStr);
    }
}
