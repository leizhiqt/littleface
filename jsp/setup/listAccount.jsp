<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="/jsp/incl/static.inc" %>

<html>
<head>
<title>Nick</title>
<link rel="stylesheet" type="text/css" href="jsp/styles/default.css" />
<script type="text/javascript" src="jsp/js/util.js"></script>
<%-- 
<script type="text/javascript"  language="javascript" charset="utf-8">

</script> 
<style type="text/css">

</style>
--%>
</head>

<body>
<form id="default" name="default" method="Post" action="<%=request.getContextPath()%>/Account.do" >
<%@ include file="/jsp/incl/adminHeader.jsp" %>
<!-- SearchTable -->
    <div class="SearchTable" >
      <ul>
     	<li class="Output"><html:message key="ID" /></li>
	<li class="Input"><html:input type="text" id="ID" name="ID" property="ID" size="15" maxlength="255"/></li>
     	<li class="Output" id="flag"><html:message key="Code" /> </li>
        <li class="Input"><html:input type="text" id="Code" name="Code" property="Code" size="15" maxlength="255"/></li>

     	<li class="Output" id="flag"><html:message key="Extension" /> </li>
        <li class="Input"><html:input type="text" id="Extension" name="Extension" property="Extension" size="15" maxlength="255"/></li>
     	<li class="Output" id="flag"><html:message key="Group" /> </li>
        <li class="Input"><html:input type="text" id="Group" name="Group" property="Group" size="15" maxlength="255"/></li>

     	<li class="Output" id="flag"><html:message key="Currency" /> </li>
        <li class="Input"><html:input type="text" id="Currency" name="Currency" property="Currency" size="15" maxlength="255"/></li>
     	<li class="Output"><html:message key="Description" /></li>
        <li class="Input"><html:input type="text" id="Description" name="Description" property="Description" size="15" maxlength="255"/></li>

     	<li class="Button"><input type="button" id="Find" name="Find" value="Find" onclick="Util.Submit('/Account.do?state=list');"/><input type="button" id="Add" name="Add" value="Add" onclick="Util.Submit('/Account.do?state=promptAdd');"/><input type="reset" id="reset" name="reset" value="reset"/></li>
      </ul>
    </div>

<!-- ListTable -->
    <div class="Table7" >
      <ul>
     	<li class="Title"><html:message key="ID" /></li>
     	<li class="Title"><html:message key="Code" /></li>
     	<li class="Title"><html:message key="Extension" /></li>
     	<li class="Title"><html:message key="Group" /></li>
     	<li class="Title"><html:message key="Currency" /></li>
     	<li class="Title"><html:message key="Description" /></li>
     	<li class="Title"><html:message key="Operation" /></li>

<%int i=0;%>
<logic:iterate property="List">
        <li><%=Input.getValue("ID"+i)%></li>
        <li><%=Input.getValue("Code"+i)%></li>
        <li><%=Input.getValue("Extension"+i)%></li>
        <li><%=Input.getValue("Group"+i)%></li>
        <li><%=Input.getValue("Currency"+i)%></li>
        <li><%=Input.getValue("Description"+i)%></li>

        <li><a href="javascript:Util.Submit('/Account.do?state=promptUpdate&Key=<%=Input.getValue("ID"+i)%>');"><html:message key="Update" /></a> <a href="javascript:Util.Submit('/Account.do?state=processDelete&Key=<%=Input.getValue("ID"+i)%>');"><html:message key="Delete" /></a></li>
<% i++; %>
</logic:iterate>

	<%-- <li><html:input type="checkbox" id="UserName" name="UserName" value="0"/></li> --%>
      </ul>
    </div>

<%@ include file="/jsp/incl/winFooter.jsp" %>
</form>
</body>
</html>
