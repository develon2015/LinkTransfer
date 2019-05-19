<%@page import="comm.DownloadProgress"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.HashMap"%>
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
<title>文件中转站 MGR</title>
<link rel="stylesheet" href="css/global.css">
</head>
<body>
	<h1>列表</h1>
	<%
		HashMap<String, DownloadProgress> map = Transfer.getInst().getList();
		Set<String> keys = map.keySet();
		for (Object key : keys) {
	%>
	<h1><%=key.toString()%></h1>
	<%
		}
	%>
</body>
</html>
<%@ page trimDirectiveWhitespaces="true"%>