<%@ include file="/sample/common/taglibs.jsp"%>
<html>   
<head>
    <%@ include file="/sample/common/meta.jsp" %>
    <title><fmt:message key="categoryDetail.title"/></title>
    <meta name="heading" content="<fmt:message key='categoryDetail.heading'/>"/>   
	<link rel="stylesheet" href="<c:url value='/sample/css/admin.css'/>" type="text/css">        
    <script type="text/javascript" src="<c:url value='/sample/javascript/global.js'/>"></script>	
    <script type="text/javascript" src="<c:url value='/sample/javascript/prototype.js'/>"></script>    
	<script language="javascript" src="<c:url value='/sample/javascript/CommonScript.js'/>"></script>
	<script language="JavaScript">
	<!--		
	function goHome() {
		document.location.href="<c:url value='/monitoringListProduct.do'/>";
	}
	-->
	</script>         
</head>

<body bgcolor="#ffffff" text="#000000">
<!--************************** begin of contents *****************************-->

<!-- begin of title -->
<table width="100%" height="37" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td width="15" height="37"><img src="<c:url value='/sample/images/ct_ttl_img01.gif'/>" width="15" height="37"></td>
		<td background="<c:url value='/sample/images/ct_ttl_img02.gif'/>" width="100%" style="padding-left: 10px;">
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td width="93%" class="ct_ttl01">		
					View Category Information
				</td>
			</tr>
		</table>
		</td>
		<td width="12" height="37"><img	src="<c:url value='/sample/images/ct_ttl_img03.gif'/>" width="12" height="37"></td>
	</tr>
</table>

<form:form commandName="category" method="post" action="monitoringGetCategory.do" id="categoryForm" name="categoryForm">
<form:errors path="*" cssClass="error" element="div"/>
    <table width="100%" border="0" cellspacing="0" cellpadding="0" style="margin-top: 13px;">

		<tr><td height="1" colspan="3" bgcolor="D6D6D6"></td></tr>
		<tr>		
			<td width="130" class="ct_td"><anyframe:message code="category.categoryNo"/>&nbsp;*</td><td bgcolor="D6D6D6" width="1"></td>
			<td class="ct_write01">
				${category.categoryNo}
			</td>			
		</tr>
		<tr><td height="1" colspan="3" bgcolor="D6D6D6"></td></tr>
		<tr>
			<td width="130" class="ct_td">
			
			<anyframe:message code="category.categoryName"/>&nbsp;*</td><td bgcolor="D6D6D6" width="1"></td>
			<td class="ct_write01">
				${category.categoryName}									        
			</td>
		</tr>		
		<tr><td height="1" colspan="3" bgcolor="D6D6D6"></td></tr>
		<tr>
			<td width="130" class="ct_td">
			
			<anyframe:message code="category.categoryDesc"/></td><td bgcolor="D6D6D6" width="1"></td>
			<td class="ct_write01">
				${category.categoryDesc}
			</td>
		</tr>
		<tr><td height="1" colspan="3" bgcolor="D6D6D6"></td></tr>
		<tr>
			<td width="130" class="ct_td">
			
			<anyframe:message code="category.useYn"/></td><td bgcolor="D6D6D6" width="1"></td>
			<td class="ct_write01">		
				${category.useYn}										        
			</td>
		</tr>
	<tr><td height="1" colspan="3" bgcolor="D6D6D6"></td></tr>		
</table>

<!-- begin of button -->
<table width="100%" border="0" cellspacing="0" cellpadding="0"
	style="margin-top:10px;">
	<tr>
		<td width="53%">
		
		</td>
		<td align="right">
			<table border="0" cellspacing="0" cellpadding="0">
				<tr>		
					<td width="17" height="23"><img src="<c:url value='/sample/images/ct_btnbg01.gif'/>" width="17" height="23"></td>
					<td background="<c:url value='/sample/images/ct_btnbg02.gif'/>" class="ct_btn01" style="padding-top:3px;"><a href="javascript:goHome();"><fmt:message key="button.home"/></a></td>
					<td width="14" height="23"><img src="<c:url value='/sample/images/ct_btnbg03.gif'/>" width="14" height="23"></td>
				</tr>
			</table>
		</td>
	</tr>
</table>

</form:form>

	<script language="javascript" src="<c:url value='/sample/javascript/calendar.js'/>"></script>	
</body>
</html>