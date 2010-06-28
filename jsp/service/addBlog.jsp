<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="/jsp/incl/static.inc" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="generator" content="HTML Tidy, see www.w3.org" />
<meta name="revision" content="$Id: addBlog.jsp,v 1.1 2008/09/03 22:31:35 cvs Exp $" />
<title>W3C Technical Reports and Publications</title>
<link type="text/css" rel="stylesheet" href="jsp/styles/global.css" />
<link type="text/css" rel="stylesheet" href="jsp/styles/userMenu.css" />
<script type="text/javascript" src="jsp/js/util.js"></script>
	<script type="text/javascript">
	/************************************************************************************************************
	(C) www.dhtmlgoodies.com, October 2005
	
	This is a script from www.dhtmlgoodies.com. You will find this and a lot of other scripts at our website.	
	
	Terms of use:
	You are free to use this script as long as the copyright message is kept intact. However, you may not
	redistribute, sell or repost it without our permission.
	
	Updated:
	
	February, 22nd 2006 - Instead of skipping onclick events when slide is in progress, start sliding in the other direction if mouse is clicked the second time.
	April, 	23rd 2006 - Added support for having only one pane expanded simultaneously( xpPanel_onlyOneExpandedPane)
	Thank you!
	
	www.dhtmlgoodies.com
	Alf Magne Kalleland
	
	************************************************************************************************************/	

	/* Update LOG 
	
	January, 28th - Fixed problem when double clicking on a pane(i.e. expanding and collapsing).
	
	*/
	var xpPanel_slideActive = true;	// Slide down/up active?
	var xpPanel_slideSpeed = 20;	// Speed of slide
	var xpPanel_onlyOneExpandedPane = false;	// Only one pane expanded at a time ?
	
	var dhtmlgoodies_xpPane;
	var dhtmlgoodies_paneIndex;
	
	var savedActivePane = false;
	var savedActiveSub = false;

	var xpPanel_currentDirection = new Array();
	
	var cookieNames = new Array();
	
	
	var currentlyExpandedPane = false;
	
	/*
	These cookie functions are downloaded from 
	http://www.mach5.com/support/analyzer/manual/html/General/CookiesJavaScript.htm
	*/	
	function Get_Cookie(name) { 
	   var start = document.cookie.indexOf(name+"="); 
	   var len = start+name.length+1; 
	   if ((!start) && (name != document.cookie.substring(0,name.length))) return null; 
	   if (start == -1) return null; 
	   var end = document.cookie.indexOf(";",len); 
	   if (end == -1) end = document.cookie.length; 
	   return unescape(document.cookie.substring(len,end)); 
	} 
	// This function has been slightly modified
	function Set_Cookie(name,value,expires,path,domain,secure) { 
		expires = expires * 60*60*24*1000;
		var today = new Date();
		var expires_date = new Date( today.getTime() + (expires) );
	    var cookieString = name + "=" +escape(value) + 
	       ( (expires) ? ";expires=" + expires_date.toGMTString() : "") + 
	       ( (path) ? ";path=" + path : "") + 
	       ( (domain) ? ";domain=" + domain : "") + 
	       ( (secure) ? ";secure" : ""); 
	    document.cookie = cookieString; 
	}

	function cancelXpWidgetEvent()
	{
		return false;	
		
	}
	
	function showHidePaneContent(e,inputObj)
	{
		if(!inputObj)inputObj = this;
		
		var img = inputObj.getElementsByTagName('IMG')[0];
		var numericId = img.id.replace(/[^0-9]/g,'');
		var obj = document.getElementById('paneContent' + numericId);
		if(img.src.toLowerCase().indexOf('up')>=0){
			currentlyExpandedPane = false;
			img.src = img.src.replace('up','down');
			if(xpPanel_slideActive){
				obj.style.display='block';
				xpPanel_currentDirection[obj.id] = (xpPanel_slideSpeed*-1);
				slidePane((xpPanel_slideSpeed*-1), obj.id);
			}else{
				obj.style.display='none';
			}
			if(cookieNames[numericId])Set_Cookie(cookieNames[numericId],'0',100000);
		}else{
			if(this){
				if(currentlyExpandedPane && xpPanel_onlyOneExpandedPane)showHidePaneContent(false,currentlyExpandedPane);
				currentlyExpandedPane = this;	
			}else{
				currentlyExpandedPane = false;
			}
			img.src = img.src.replace('down','up');
			if(xpPanel_slideActive){
				if(document.all){
					obj.style.display='block';
					//obj.style.height = '1px';
				}
				xpPanel_currentDirection[obj.id] = xpPanel_slideSpeed;
				slidePane(xpPanel_slideSpeed,obj.id);
			}else{
				obj.style.display='block';
				subDiv = obj.getElementsByTagName('DIV')[0];
				obj.style.height = subDiv.offsetHeight + 'px';
			}
			if(cookieNames[numericId])Set_Cookie(cookieNames[numericId],'1',100000);
		}	
		return true;	
	}
	
	
	
	function slidePane(slideValue,id)
	{
		if(slideValue!=xpPanel_currentDirection[id]){
			return false;
		}
		var activePane = document.getElementById(id);
		if(activePane==savedActivePane){
			var subDiv = savedActiveSub;
		}else{
			var subDiv = activePane.getElementsByTagName('DIV')[0];
		}
		savedActivePane = activePane;
		savedActiveSub = subDiv;
		
		var height = activePane.offsetHeight;
		var innerHeight = subDiv.offsetHeight;
		height+=slideValue;
		if(height<0)height=0;
		if(height>innerHeight)height = innerHeight;
		
		if(document.all){
			activePane.style.filter = 'alpha(opacity=' + Math.round((height / subDiv.offsetHeight)*100) + ')';
		}else{
			var opacity = (height / subDiv.offsetHeight);
			if(opacity==0)opacity=0.01;
			if(opacity==1)opacity = 0.99;
			activePane.style.opacity = opacity;
		}			
		
					
		if(slideValue<0){			
			activePane.style.height = height + 'px';
			subDiv.style.top = height - subDiv.offsetHeight + 'px';
			if(height>0){
				setTimeout('slidePane(' + slideValue + ',"' + id + '")',10);
			}else{
				if(document.all)activePane.style.display='none';
			}
		}else{			
			subDiv.style.top = height - subDiv.offsetHeight + 'px';
			activePane.style.height = height + 'px';
			if(height<innerHeight){
				setTimeout('slidePane(' + slideValue + ',"' + id + '")',10);				
			}		
		}
		
		
		
		
	}
	
	function mouseoverTopbar()
	{
		var img = this.getElementsByTagName('IMG')[0];
		var src = img.src;
		img.src = img.src.replace('.gif','_over.gif');
		
		var span = this.getElementsByTagName('SPAN')[0];
		span.style.color='#428EFF';		
		
	}
	function mouseoutTopbar()
	{
		var img = this.getElementsByTagName('IMG')[0];
		var src = img.src;
		img.src = img.src.replace('_over.gif','.gif');		
		
		var span = this.getElementsByTagName('SPAN')[0];
		span.style.color='';
		
		
		
	}
	
	
	function initDhtmlgoodies_xpPane(panelTitles,panelDisplayed,cookieArray)
	{
		dhtmlgoodies_xpPane = document.getElementById('dhtmlgoodies_xpPane');
		var divs = dhtmlgoodies_xpPane.getElementsByTagName('DIV');
		dhtmlgoodies_paneIndex=0;
		cookieNames = cookieArray;
		for(var no=0;no<divs.length;no++){
			if(divs[no].className=='dhtmlgoodies_panel'){
				
				var outerContentDiv = document.createElement('DIV');	
				var contentDiv = divs[no].getElementsByTagName('DIV')[0];
				outerContentDiv.appendChild(contentDiv);	
			
				outerContentDiv.id = 'paneContent' + dhtmlgoodies_paneIndex;
				outerContentDiv.className = 'panelContent';
				var topBar = document.createElement('DIV');
				topBar.onselectstart = cancelXpWidgetEvent;
				var span = document.createElement('SPAN');				
				span.innerHTML = panelTitles[dhtmlgoodies_paneIndex];
				topBar.appendChild(span);
				topBar.onclick = showHidePaneContent;
				if(document.all)topBar.ondblclick = showHidePaneContent;
				topBar.onmouseover = mouseoverTopbar;
				topBar.onmouseout = mouseoutTopbar;
				topBar.style.position = 'relative';

				var img = document.createElement('IMG');
				img.id = 'showHideButton' + dhtmlgoodies_paneIndex;
				img.src = 'jsp/images/arrow_up.gif';				
				topBar.appendChild(img);
				
				if(cookieArray[dhtmlgoodies_paneIndex]){
					cookieValue = Get_Cookie(cookieArray[dhtmlgoodies_paneIndex]);
					if(cookieValue)panelDisplayed[dhtmlgoodies_paneIndex] = cookieValue==1?true:false;
					
				}
				
				if(!panelDisplayed[dhtmlgoodies_paneIndex]){
					outerContentDiv.style.height = '0px';
					contentDiv.style.top = 0 - contentDiv.offsetHeight + 'px';
					if(document.all)outerContentDiv.style.display='none';
					img.src = 'jsp/images/arrow_down.gif';
				}
								
				topBar.className='topBar';
				divs[no].appendChild(topBar);				
				divs[no].appendChild(outerContentDiv);	
				dhtmlgoodies_paneIndex++;			
			}			
		}
	}
	
	</script>


	<style type="text/css">
	/*
	body{
		font-size:0.8em;
		font-family: Trebuchet MS, Lucida Sans Unicode, Arial, sans-serif;
		margin:0px;
		padding:0px;
	} */
	img{
		border:0px;
	}
	thead td{
		font-weight:bold;
		color:#000;
		background-color:#E2EBED;
	}
	td{
		padding:2px;
	}
	table{
		border:1px solid #000;
		border-collapse: collapse;
	}
	h1{
		font-size:1.3em;
		margin-bottom:0px;
	}
	table,h1,p,#ads{
		margin-left:10px;
	}
	#ads{
		margin-top:20px;
	}
	
	/* These classes are used by the script as rollover effect for table 1 and 2 */
	
	.tableRollOverEffect1{
		background-color:#317082;
		color:#FFF;
	}
	.tableRowClickEffect1{
		background-color:blue;
		color:#FFF;
	}
	.command {
		height:20px;
		background-color:#317082;
		}
	.command ul {
		margin:0px;
		padding:0px;
		border:0px;
		list-style:none;
		text-align:left;
		}
	.command ul li { display:inline;}

	.submit,.button {
		color:#5eb2c1;
		height:20px;
	}
.cbody {
		background-color:#317082;
margin:0px;
padding:0px;
text-align:left;
background-color: #85c6fd;
color:blue;
}

.text {
background-color:#ffffff;
border:0px;
color:blue;
margin:2px;
padding:0px;
/*height:90%;*/
/*width:80%;*/
font-size:14px;
text-align:left;
display:inline;
}
	</style>
	<script type="text/javascript">
	/************************************************************************************************************
	(C) www.dhtmlgoodies.com, November 2005
	
	This is a script from www.dhtmlgoodies.com. You will find this and a lot of other scripts at our website.	
	
	Terms of use:
	You are free to use this script as long as the copyright message is kept intact. However, you may not
	redistribute, sell or repost it without our permission.
	
	Thank you!
	
	www.dhtmlgoodies.com
	Alf Magne Kalleland
	
	************************************************************************************************************/	
	var arrayOfRolloverClasses = new Array();
	var arrayOfClickClasses = new Array();
	var activeRow = false;
	var activeRowClickArray = new Array();
	
	function highlightTableRow()
	{
		var tableObj = this.parentNode;
		if(tableObj.tagName!='TABLE')tableObj = tableObj.parentNode;

		if(this!=activeRow){
			this.setAttribute('origCl',this.className);
			this.origCl = this.className;
		}
		this.className = arrayOfRolloverClasses[tableObj.id];
		
		activeRow = this;
		
	}
	
	function clickOnTableRow()
	{
		var tableObj = this.parentNode;
		if(tableObj.tagName!='TABLE')tableObj = tableObj.parentNode;		
		
		if(activeRowClickArray[tableObj.id] && this!=activeRowClickArray[tableObj.id]){
			activeRowClickArray[tableObj.id].className='';
		}
		this.className = arrayOfClickClasses[tableObj.id];
		
		activeRowClickArray[tableObj.id] = this;
				
	}
	
	function resetRowStyle()
	{
		var tableObj = this.parentNode;
		if(tableObj.tagName!='TABLE')tableObj = tableObj.parentNode;

		if(activeRowClickArray[tableObj.id] && this==activeRowClickArray[tableObj.id]){
			this.className = arrayOfClickClasses[tableObj.id];
			return;	
		}
		
		var origCl = this.getAttribute('origCl');
		if(!origCl)origCl = this.origCl;
		this.className=origCl;
		
	}
		
	function addTableRolloverEffect(tableId,whichClass,whichClassOnClick)
	{
		arrayOfRolloverClasses[tableId] = whichClass;
		arrayOfClickClasses[tableId] = whichClassOnClick;
		
		var tableObj = document.getElementById(tableId);
		var tBody = tableObj.getElementsByTagName('TBODY');
		if(tBody){
			var rows = tBody[0].getElementsByTagName('TR');
		}else{
			var rows = tableObj.getElementsByTagName('TR');
		}
		for(var no=0;no<rows.length;no++){
			rows[no].onmouseover = highlightTableRow;
			rows[no].onmouseout = resetRowStyle;
			
			if(whichClassOnClick){
				rows[no].onclick = clickOnTableRow;	
			}
		}
		
	}
	</script>

<script type="text/javascript">

window.onload=window.onresize=function(){
if(document.getElementById("left").clientHeight<document.getElementById("content").clientHeight){
document.getElementById("left").style.height=document.getElementById("content").offsetHeight+"px";
}
else{
document.getElementById("content").style.height=document.getElementById("left").offsetHeight+"px";
}
}
</script>

	<style type="text/css">
	table,tr,td{
		vertical-align:top;
	}
	.textInput{
		width:300px;
	}
	html{
		margin:0px;
	}
	.formButton{
		width:75px;
	}
	textarea,input,select{
		font-family:Trebuchet MS;
	}
	i{
		font-size:0.9em;
	}
	#formResponse {
		text-align:left;

		}
	</style>

</head>

<body>
<%@ include file="/jsp/incl/loginHeader.jsp" %>
<div id="content">
<div class="title">Hello:everbody</div>
<div class="command">
<ul>
<li><html:input type="text" id="SearchValue" name="SearchValue" size="15" maxlength="255" styles="text"/><a href="">Search</a></li>

</ul>
</div>


<form id="default" name="default" Method="post" action="<%=request.getContextPath()%>/Blog.do?state=processAddBlog">
<script type="text/javascript" src="jsp/js/calendar.js"></script>

<div id="formResponse">
<html:input type="hidden" id="ID" name="ID" property="ID"/>

<table align="left" style="font-size:0.9em;">

<tr>
<td><html:message key="Title" /></td>
<td><html:input type="text" id="Title" name="Title" property="Title" size="15" maxlength="255"/></td>
</tr>

<tr>
<td><html:message key="Type" /></td>
<td><html:select id="TypeID" name="TypeID" property="TypeID" /></td>
</tr>

<tr>
<td><html:message key="Owner" /></td>
<td><html:output id="Owner" name="Owner" property="Owner"/> </td>
</tr>

<tr>
<td><html:message key="Date" /></td>
<td><html:input type="text" id="Date" name="Date" property="Date" size="15" maxlength="255"/> <a href="javascript:window.open('<%=request.getContextPath()%>/jsp/incl/calendar.jsp','Remote', 'width=220,height=252,scrollbars');setParams('Date');"><img src="jsp/images/miniDate.gif" border=0 alt="<html:message key="ChooseDate"/>"></a></td>
</tr>

<tr>
<td><html:message key="Description" /></td>
<td>
<textarea cols="50" rows="10" id="Description" name="Description"></textarea>
</td>
</tr>

<tr>
<td>&nbsp</td>
<td>
<input type="submit" id="Add" name="Add" value="Add"/>
<input type="button" id="Close" name="Close" value="Close" onclick="javascript:window.history.back(1);"/>
</td>
</tr>

</table>


</div>

</form>
</div>
</div>
<%@ include file="/jsp/incl/loginFooter.jsp" %>
</body>
