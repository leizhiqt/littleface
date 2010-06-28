<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="/jsp/etc/static.inc" %>

<html>
<head>
<title>Nick</title>
<link rel="stylesheet" type="text/css" href="jsp/etc/styles/default.css" />
<script type="text/javascript" src="jsp/etc/js/util.js"></script>
<%-- 
<script type="text/javascript"  language="javascript" charset="utf-8">

</script> 
<style type="text/css">

</style>
--%>
</head>

<body>
<form id="default" name="default" method="Post" action="<%=request.getContextPath()%>/Currency.do" >
<%@ include file="/jsp/etc/adminHeader.jsp" %>
<!-- SearchTable -->
    <div class="SearchTable" >
      <ul>
     	<li class="Output"><html:message key="ID" /></li>
	<li class="Input"><html:input type="text" id="ID" name="ID" property="ID" size="15" maxlength="255"/></li>

     	<li class="Output" id="flag"><html:message key="ISOCode" /> </li>
        <li class="Input"><html:input type="text" id="ISOCode" name="ISOCode" property="ISOCode" size="15" maxlength="255"/></li>

     	<li class="Output"><html:message key="Name" /></li>
	<li class="Input"><html:input type="text" id="Name" name="Name" property="Name" size="15" maxlength="255"/></li>

     	<li class="Output"><html:message key="Country" /></li>
        <li class="Input"><html:input type="text" id="Country" name="Country" property="Country" size="15" maxlength="255"/></li>

 
     	<li class="Button"><input type="button" id="Find" name="Find" value="Find" onclick="Util.Submit('/Currency.do?state=list');"/><input type="button" id="Add" name="Add" value="Add" onclick="Util.Submit('/Currency.do?state=promptAdd');"/><input type="reset" id="reset" name="reset" value="reset"/></li>
      </ul>
    </div>

<!-- ListTable -->
    <div class="Table6" >
      <ul>
     	<li class="Title"><html:message key="ID" /></li>
     	<li class="Title"><html:message key="ISOCode" /></li>
     	<li class="Title"><html:message key="Name" /></li>
     	<li class="Title"><html:message key="Country" /></li>
     	<li class="Title"><html:message key="Symbol" /></li>
     	<li class="Title"><html:message key="Operation" /></li>
<%int i=0;%>
<logic:iterate property="List">
        <li><%=Input.getValue("ID"+i)%></li>
        <li><%=Input.getValue("ISOCode"+i)%></li>
	<li><%=Input.getValue("Name"+i)%></li>
        <li><%=Input.getValue("Country"+i)%></li>
        <li><%=Input.getValue("Symbol"+i)%></li>

        <li><a href="javascript:Util.Submit('/Currency.do?state=promptUpdate&Key=<%=Input.getValue("ID"+i)%>');"><html:message key="Update" /></a> <a href="javascript:Util.Submit('/Currency.do?state=processDelete&Key=<%=Input.getValue("ID"+i)%>');"><html:message key="Delete" /></a></li>
<% i++; %>
</logic:iterate>

	<%-- <li><html:input type="checkbox" id="UserName" name="UserName" value="0"/></li> --%>
      </ul>
    </div>

<%@ include file="/jsp/etc/winFooter.jsp" %>
</form>
</body>
</html>
