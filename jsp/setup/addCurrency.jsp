<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="/jsp/incl/static.inc" %>

<html>
<head>
<title>Nick</title>
<link rel="stylesheet" type="text/css" href="jsp/styles/default.css" />
<script type="text/javascript" src="jsp/js/util.js"></script>
<%-- 
<<script type="text/javascript"  language="javascript" charset="utf-8">

</script> 
<style type="text/css">

</style>
--%>
</head>

<body>
<form name="default" Method="Post" action="<%=request.getContextPath()%>/Currency.do?state=promptAdd">
<%@ include file="/jsp/incl/adminHeader.jsp" %>
<!-- AddTable -->
    <div class="SearchTable" >
      <ul>
     	<li class="AddOutput"><fmt:message key="ISOCode" /></li>
	<li class="AddInput"><input type="text" id="ISOCode" name="ISOCode" property="ISOCode" size="15" maxlength="255"/></li>

     	<li class="AddOutput"><fmt:message key="Name" /></li>
	<li class="AddInput"><input type="text" id="Name" name="Name" property="Name" size="15" maxlength="255"/></li>

     	<li class="AddOutput"><fmt:message key="Country" /></li>
        <li class="AddInput"><html:select id="Country" name="Country" property="Country" /></li>
 
     	<li class="AddOutput"><fmt:message key="Symbol" /></li>
	<li class="AddInput"><input type="text" id="Symbol" name="Symbol" property="Symbol" size="15" maxlength="255"/></li>

     	<li class="Button"><input type="button" id="Add" name="Add" value="Add" onclick="Util.Submit('/Currency.do?state=processAdd');"/><input type="reset" id="reset" name="reset" value="reset"/></li>
      </ul>
    </div>
<%@ include file="/jsp/incl/winFooter.jsp" %>
</form>
</body>
</html>
