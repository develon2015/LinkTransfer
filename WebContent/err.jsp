<%@page import="java.net.URLDecoder"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	//
	String errCause = request.getQueryString();
%>
<!DOCTYPE html>
<html lang="zh">
<head>
<meta charset="UTF-8">
<title>异常错误</title>
<link rel="stylesheet" href="css/global.css">
</head>
<body class="center">
	<h1>Sorry, 一个错误发生了.</h1>
	<font><%= URLDecoder.decode(errCause, "UTF-8") %></font>
</body>
</html>
<%@ page trimDirectiveWhitespaces="true"%>