<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.LinkedHashMap "%>

<%
	Map<String, String> gbar = new LinkedHashMap<String, String>();
	gbar.put("Activity","Activity.do");

	request.setAttribute("gbar", gbar);
%>