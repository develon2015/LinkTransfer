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
	
	String md5 = request.getQueryString();
	System.out.println("md5 -> " + md5);
	
	response.setStatus(HttpServletResponse.SC_FOUND);
	response.setHeader("Location", Transfer.getInst().queryDownLink(md5));
	// Location 由 Nginx 反代
	// Content-Disposition: attachment; filename*="UTF-8''update.zip"
%>
<%@ page trimDirectiveWhitespaces="true"%>