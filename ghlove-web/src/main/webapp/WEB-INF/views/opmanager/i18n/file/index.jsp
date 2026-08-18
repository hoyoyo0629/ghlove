<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" 	uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" 	uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="op" 		uri="/WEB-INF/tlds/functions" %>

<%@ page import="java.nio.file.*" %>
<%@ page import="java.nio.file.Files" %>
<%@ page import="java.util.*" %>
<%@ page import="java.util.stream.*" %>
<%@ page import="java.io.*" %>

<%
    String rootFilePath = "";
    String os = System.getProperty("os.name").toLowerCase();
    if (os.contains("win")) {
    	rootFilePath = "c:/";
    } else if (os.contains("mac")) {
    	rootFilePath = "/";
    } else if (os.contains("nix") || os.contains("nux") || os.contains("aix")) {
    	rootFilePath = "/";
    } else if (os.contains("linux")) {
    	rootFilePath = "/";
    } else if (os.contains("sunos")) {
    	rootFilePath = "/";
    } else {
        rootFilePath = "/";
    }


    String location = request.getParameter("location");
    String filePath = location == null ? rootFilePath : location;
    Path dirPath = Paths.get(filePath);
    List<Path> pathList = null;

    try {
        Stream<Path> walk = Files.walk(dirPath, 1);
        pathList = walk.filter(p -> !p.equals(dirPath)).sorted(Comparator.comparing(Files::isDirectory)).collect(Collectors.toList());
        Collections.reverse(pathList);

        request.setAttribute("pathList", pathList);
    } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }

    request.setAttribute("filePath", filePath);

%>
<style>
    .file-container {
        width: 500px;
        margin-top: 30px;
    }
    .file-header, .file-tr {
        display: flex;
    }

    .file-header div:nth-child(1), .file-tr div:nth-child(1) {
        width: 70%;
        max-width: 350px;
    }
    .file-header div:nth-child(2), .file-tr div:nth-child(2) {
        width: 30%;
        max-width: 100px;
        text-align: right;
    }

    #progress {
        appearance: none;
    }
    #progress::-webkit-progress-bar {
        background:#f0f0f0;
        border-radius:10px;
        box-shadow: inset 3px 3px 10px #ccc;
    }
    #progress::-webkit-progress-value {
        border-radius:10px;
        background: #1D976C;
        background: -webkit-linear-gradient(to right, #93F9B9, #1D976C);
        background: linear-gradient(to right, #93F9B9, #1D976C);

    }

    #progressBg {
        width: 100%;
        height: 100%;
        position: absolute;
        x: 0px;
        y: 0px;
        background-color: black;
        opacity: 0.6;
        display: flex;
        justify-content: center;
        align-items: center;
    }

    #progressBg span {
        font-weight: bold;
        font-size: 15px;
        color: #fff;
    }

    .off {
        display: none !important;
    }


</style>
<div id="progressBg" class="off">
    <div>
        <progress id="progress" value="0" min="0" max="100" style="width: 300px"></progress>
        <span><span id="percent">100</span>%</span>
    </div>
</div>

<div>
    <h3>현재 위치 : ${fn:escapeXml(filePath)}</h3>
    <h4>서버 : ${fn:escapeXml(serverIp)}</h4>
    <div class="file-container" >
        <div class="file-header">
            <div>파일명</div>
            <div>크기</div>
        </div>
        <div class="file-body">
            <div class="file-tr">
                <div><a href="javascript:history.back();">...</a></div>
            </div>
            <c:forEach items="${pathList}" var="path">
        		<div class="file-tr">
            		<c:choose>
            			<c:when test="${Files.isDirectory(path)}">
	            			<div>
	            				<img src="/content/opmanager/images/category/icon_folder.png" />
	            				<a href='/opmanager/file/index?location=${fn:escapeXml(filePath)}/${fn:escapeXml(path.getFileName())}'>${fn:escapeXml(path.getFileName())}</a>
	            			</div>
	                        <div></div>
                        </c:when>
	            		<c:otherwise>
	            			<div>
	            				<a style="color: #000000;" href="javascript:fileDownload('${fn:escapeXml(path.getFileName())}');">${fn:escapeXml(path.getFileName())}</a>
	            			</div>
	                        <div>${op:getFileSize(path)}MB</div>
	            		</c:otherwise>
            		</c:choose>
           		</div>
            </c:forEach>
        </div>
    </div>
    <div>
        <div>경로 : <input type="text" id="moveDir" /> <button onclick="moveDir()">이동</button></div>
        <div style="margin-top : 20px">폴더명 : <input type="text" id="dirName" /> <button onclick="makeDir()">폴더 생성</button></div>
        <div style="margin-top : 20px">파일 : <input type="file" name="files" style="display: inline-block; margin: 0;" /> <button onclick="fileUpload()">파일 업로드</button></div>
        <div style="margin-top : 20px">주소 : <input type="text" id="email" style="display: inline-block; margin: 0;" /> <button onclick="sendMail()">메일발송 테스트</button></div>
        <input type="hidden" id="filePath" value='${fn:escapeXml(filePath)}' />
    </div>
</div>

<script type="text/javascript">
$(function() {

});

function moveDir() {
    location.href = "/opmanager/file/index?location=" + $("#moveDir").val()
}

function makeDir() {
    var data = {
        dirName : $("#dirName").val(),
        path : $("#filePath").val()
    };

    $.ajax({
        url : '/opmanager/file/makeDir',
        type : 'post',
        data : JSON.stringify(data),
        contentType:"application/json;charset=UTF-8",
        success : function (result) {
            alert(result.data);
            location.reload();
        }
    })
}

function fileUpload() {
    var formData = new FormData();
    var files = $("input[name=files]")[0].files;
    formData.append("path", $("#filePath").val());
    for(i=0; i<files.length; i++) {
      formData.append("files", files[i]);
    }

    $.ajax({
		url : '/opmanager/file/uploadFile',
		type : 'post',
		data: formData,
		enctype: 'multipart/form-data',
		processData: false,
		contentType: false,
		cache: false,
		async: true,
        beforeSend: function( xhr ) {
            $("#progressBg").removeClass("off");
        },
        xhr: function() {
            var xhr = $.ajaxSettings.xhr()

            xhr.upload.onprogress = function(e){
                var percent = Math.round(e.loaded / e.total * 100);
                $("#progress").val(percent);
                $("#percent").text(percent);
            }

            return xhr
        },
		success : function(response) {
            if (response.isSuccess) {
                alert(response.data);
            } else {
                alert(response.errorMessage);
            }

            location.reload();
		},
        error : function (request, status, error) {
            let errBln = true;
        },
        complete: function () {
            $("#progressBg").addClass("off");
	    }
	});
}

function fileDownload(fileName) {

    var formData = new FormData();
    formData.append("path", $("#filePath").val());
    formData.append("fileName", fileName);

    var xhr = new XMLHttpRequest();

    xhr.onreadystatechange = function(){
        if (this.readyState == 4) {
            if (this.status == 200) {
                var filename = "";
                var disposition = xhr.getResponseHeader('Content-Disposition');

                if (disposition && disposition.indexOf('attachment') !== -1) {
                	disposition = disposition.replaceAll("<","&lt;");
                    disposition = disposition.replaceAll(">","&gt;");

                    var filenameRegex = /filename[^;=\n]*=((['"]).*?\2|[^;\n]*)/;
                    var matches = filenameRegex.exec(disposition);
                    if (matches != null && matches[1]) filename = matches[1].replace(/['"]/g, '');
                }

                var blob = this.response;
                var contentTypeHeader = this.getResponseHeader("Content-Type");
                var a = document.createElement("a");
                var url = window.URL.createObjectURL(new Blob([blob], { type: "application/octetstream" }));
                a.href = url;
                a.download = decodeURIComponent(filename);
                document.body.appendChild(a);
                a.click();
                window.URL.revokeObjectURL(url);
            } else {
                alert("파일 없음");
                return false;
            }
        }
    }
    xhr.open('POST', '/opmanager/file/downloadFile');
    xhr.responseType = 'arraybuffer'; // !!필수!!
    xhr.send(formData);

    //var data = {
    //    path : $("#filePath").val(),
    //    fileName : fileName
    //};

    /*$.ajax({
        url : '/opmanager/file/downloadFile',
        method : 'post',
        data : JSON.stringify(data),
        contentType:"application/json;charset=UTF-8",
        xhrFields: {
            responseType: 'arraybuffer'
        },
        success : function (data, textStatus, jqXhr) {

            if (!data) {
                alert("파일 없음");
                return false;
            }
            console.log(jqXhr.getResponseHeader('content-type'));
            var blob = new Blob([data], { type: "application/octetstream" });
            var fileName = getFileName(jqXhr.getResponseHeader('content-disposition'));
            fileName = decodeURI(fileName);

            console.log(fileName);

            if (window.navigator.msSaveOrOpenBlob) { // IE 10+
                window.navigator.msSaveOrOpenBlob(blob, fileName);
            } else {
                var link = document.createElement('a');
                var url = window.URL.createObjectURL(blob);
                link.href = url;
                link.target = '_self';
                if (fileName) link.download = fileName;
                document.body.append(link);
                link.click();
                link.remove();
                window.URL.revokeObjectURL(url);
            }

        },
        error : function () {
            alert("다운로드 실패");
            location.reload();
        }
    });*/
}

function getFileName (contentDisposition) {
    var fileName = contentDisposition
        .split(';')
        .filter(function(ele) {
            return ele.indexOf('filename') > -1
        })
        .map(function(ele) {
            return ele
                .replace(/"/g, '')
                .split('=')[1]
        });
    return fileName[0] ? fileName[0] : null
}

function sendMail() {
    var data = {
        email : $("#email").val()
    }

    $.ajax({
        url : '/opmanager/file/sendMail',
        type : 'post',
        data : data,
        success : function (response) {
            alert('처리되었습니다.');
        },
        error : function () {
            let errBln = true;
        }
    })
}


</script>

