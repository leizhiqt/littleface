<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="/jsp/incl/static.inc"%>
<!DOCTYPE form PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title><fmt:message key="CodeType"/></title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<link type="text/css" rel="stylesheet" href="skin/office/default/layout.css" />
<link rel="stylesheet" type="text/css" href="skin/office/default/presentation.css" />
<script type="text/javascript" src="jsp/js/util.js"></script>
<script type="text/javascript">
function checkbox(){
	var selectedObj = document.all["id"];
	var count = 0;
	for(var i=0;i<selectedObj.length;i++){
 		if(selectedObj[i].checked){
  			count = count + 1;
 		}
	}
	return count;
	/*
	var edit = document.getElementById("Edit");
	var remove = document.getElementById("Delete");
	var codeManager= document.getElementById("CodeManager");
	if(count > 0){
		remove.style.display="inline";
	} else {
		remove.style.display="none";
	}
	
	if(count == 1){
		edit.style.display="inline";
		codeManager.style.display="inline";		
	} else {
		edit.style.display="none";
		codeManager.style.display="none";
	}
	alert("count="+count);
	*/
}


function remove(url){
	if(checkbox()<1){
		alert("请选择至少一条记录");
		return false;
	}
	
	return docommit(url);
}

function edit(url){
	
	if(checkbox()==1){
		return docommit(url);
	}
	alert("请选择一条记录");
	return false;
}
</script>
</head>

<body>
<fmt:bundle basename="MessageBundle">

<c:url value="/CodeType.do" var="list"/>

<c:url value="/CodeType.do" var="promptAdd">
	<c:param name="method">promptAdd</c:param>
</c:url>

<c:url value="/CodeType.do" var="processAdd">
	<c:param name="method">processAdd</c:param>
</c:url>

<c:url value="/CodeType.do" var="promptUpdate">
	<c:param name="method">promptUpdate</c:param>
</c:url>

<c:url value="/CodeType.do" var="processUpdate">
	<c:param name="method">processUpdate</c:param>
</c:url>
Edit
<c:url value="/CodeType.do" var="processDelete">
	<c:param name="method">processDelete</c:param>
</c:url>

<c:url value="/CodeType.do" var="listCode">
	<c:param name="method">listCode</c:param>
</c:url>

<form method="post" action="${list}">
<div class="command">
<input type="submit" id="List" value="<fmt:message key="List"/>">
<input type="submit" id="Add" value="<fmt:message key="Add"/>" onclick="return docommit('${promptAdd}')">
<input type="submit" id="Edit" value="<fmt:message key="Edit"/>" onclick="return edit('${promptUpdate}')">
<input type="submit" id="Delete" value="<fmt:message key="Delete"/>" onclick="return remove('${processDelete}')">
<input type="submit" id="CodeManager" value="<fmt:message key="CodeManager"/>" onclick="return edit('${listCode}');">
</div>

<div>
			<select name="codeCategory">
				<c:forEach var="items" items="${codeCategory}" varStatus="s">
					
					<option value="${items}"
					<c:if test="${items==param.codeCategory}">
						selected="selected"
					</c:if>
						>
					<fmt:message key="${items}"/>
					</option>
				</c:forEach>
			</select>
</div>
<div>
<fmt:message key="CodeType"/>
<table>
<tr>
<td><fmt:message key="ID"/></td>
<td><fmt:message key="Name"/></td>
<td><fmt:message key="Category"/></td>
</tr>

<c:forEach var="item" items="${linearTypes}" varStatus="status">
<tr>
<td><input type="checkbox" name="id" id="id" value="${item.id}"> </td>
<td><c:out value="${item.name}"></c:out></td>
<td><fmt:message key="${item.category}"/></td>
</tr>
</c:forEach>
</table>
</div>

</form>
</fmt:bundle>
</body>
</html>