<%@page import="comm.DownloadProgress"%>
<%@page import="comm.Transfer"%>
<%@page import="comm.SystemUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	request.setCharacterEncoding("UTF-8");
	response.setCharacterEncoding("UTF-8");
%>
<!DOCTYPE html>
<html lang="zh">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>文件中转站</title>
<link rel="stylesheet" href="css/global.css">
<style type="text/css">
div.footer {
	text-align: center;
	position: absolute;
	bottom: 0px;
	height: 62px;
	width: 99%;
}
</style>
</head>
<body>
	<h1>导入链接</h1>
	<form>
		<input id="urlBox" style="width: 800px;" name="url"><br> <br>
		<button>提交</button>
	</form>
	<%
		String url = SystemUtil.handleURL(request.getParameter("url"));
		if (url != null && !"".equals(url)) {
			boolean downloading = true;
			boolean failed = false;
			try {
				DownloadProgress.Statu statu = Transfer.getInst().doDownload(url);
				downloading = (statu == DownloadProgress.Statu.DOWNLOADING);
			} catch (Exception e) {
	%>
	<h2>
		下载失败!
		<%=e.getMessage()%></h2>
	<%
				failed = true;
			}
			if (!failed) {
				if (downloading) {
	%>
	<h2>下载中, <%= Transfer.getInst().queryProgress(url) %></h2>
	<button onclick="location = '';">刷新</button>
	<%
				} else { //下载完成
	%>
	<h5>
		Origin URL : <a href="<%= SystemUtil.handleURL(url) %>"><%= SystemUtil.handleURL(url) %></a>
	</h5>
	<h2>
		<a href="down.jsp?<%= SystemUtil.hash(SystemUtil.handleURL(url)) %>">点此下载/另存为文件</a>
	</h2>
	<%
				}
			}
		}
	%>
	<div class="footer">
	<a href="<%= SystemUtil.getConfigParam("transfer") + "/100MB.test"%>">测速文件</a><br>
	<a href="https://github.com/develon2015/LinkTransfer">https://github.com/develon2015/LinkTransfer</a>
	</div>
</body>
<script>
	var urlBox = document.getElementById("urlBox");
	urlBox.value = "<%= url == null ? "" : url %>";
</script>
</html>
<%@ page trimDirectiveWhitespaces="true"%>