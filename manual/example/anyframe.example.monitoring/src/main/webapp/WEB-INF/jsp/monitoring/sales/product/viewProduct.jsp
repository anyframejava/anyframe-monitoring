<%@ include file="/sample/common/taglibs.jsp"%>
<html>   
<head>
    <%@ include file="/sample/common/meta.jsp" %>
    <title><fmt:message key="productDetail.title"/></title>
    <meta name="heading" content="<fmt:message key='productDetail.heading'/>"/>   
	<link rel="stylesheet" href="<c:url value='/sample/css/admin.css'/>" type="text/css">        
    <script type="text/javascript" src="<c:url value='/sample/javascript/global.js'/>"></script>	
    <script type="text/javascript" src="<c:url value='/sample/javascript/prototype.js'/>"></script>    
	<script language="javascript" src="<c:url value='/sample/javascript/CommonScript.js'/>"></script>
	<script language="JavaScript">
	<!--
	function fncAddProduct() {
		// Form validation
		if(FormValidation(document.productForm) != false) {	
			if(document.productForm.asYn.checked)
				document.productForm._asYn.value="Y";
			else
				document.productForm._asYn.value="N";

		    document.productForm.action="<c:url value='/monitoringAddProduct.do'/>";
		    document.productForm.submit();
		} 
	}
	
	function fncUpdateProduct() {
		// Form validation
		if(FormValidation(document.productForm) != false) {   
		    document.productForm.action="<c:url value='/monitoringUpdateProduct.do'/>";
		    document.productForm.submit();
		} 
	}

	function fncDeleteProduct(){	
		if(confirmDelete('product')) {
		    document.productForm.action="<c:url value='/monitoringDeleteProduct.do'/>";
		    document.productForm.submit();
		}	    
	}
		
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

				 	<c:if test="${empty product.prodNo}">
				 	Add Product Information
				 	<c:set var="readonly" value="false"/>
					</c:if>
			
				    <c:if test="${not empty product.prodNo}">	
					Update Product Information
					<c:set var="readonly" value="true"/>				 
					</c:if>					 				 
				</td>
			</tr>
		</table>
		</td>
		<td width="12" height="37"><img	src="<c:url value='/sample/images/ct_ttl_img03.gif'/>" width="12" height="37"></td>
	</tr>
</table>

<form:form commandName="product" method="post" id="productForm" name="productForm" enctype="multipart/form-data">
<form:errors path="*" cssClass="error" element="div"/>
    <table width="100%" border="0" cellspacing="0" cellpadding="0" style="margin-top: 13px;">
		<c:if test="${not empty product.prodNo}">	
			<tr><td height="1" colspan="3" bgcolor="D6D6D6"></td></tr>
			<tr>		
				<td width="150" class="ct_td"><anyframe:message code="product.prodNo"/>&nbsp;*</td><td bgcolor="D6D6D6" width="1"></td>
				<td class="ct_write01">
					<form:input path="prodNo" id="prodNo" cssClass="ct_input_g" cssErrorClass="text medium error" readonly="${readonly}"/>
				</td>			
			</tr>
			</c:if>
		<tr><td height="1" colspan="3" bgcolor="D6D6D6"></td></tr>
		<tr>
			<td width="150" class="ct_td">
			
			<anyframe:message code="product.prodName"/>&nbsp;*</td><td bgcolor="D6D6D6" width="1"></td>
			<td class="ct_write01">									        
	        <form:input path="prodName" id="prodName" cssClass="ct_input_g" cssErrorClass="text medium error" maxlength="100"/>
			</td>
		</tr>	
		<tr><td height="1" colspan="3" bgcolor="D6D6D6"></td></tr>
		<tr>
			<td width="150" class="ct_td">
			
			<anyframe:message code="product.categoryNo"/>&nbsp;*</td><td bgcolor="D6D6D6" width="1"></td>
			<td class="ct_write01">	
			<c:if test="${empty product.prodNo}">	
				<form:select path="category.categoryNo" id="category.categoryNo" cssClass="ct_input_g" cssStyle="width:100px;">
			        <c:forEach var="category" items="${categoryList}">   									        
		            	<form:option value="${category.categoryNo}">${category.categoryName}</form:option>       
		            </c:forEach> 			
	            </form:select>
            </c:if>
            <c:if test="${not empty product.prodNo}">	
            	<form:input path="category.categoryNo" id="categoryNo" cssClass="ct_input_g" cssErrorClass="text medium error" maxlength="100" readonly="true"/>
            </c:if>
			</td>
		</tr>	
		<tr><td height="1" colspan="3" bgcolor="D6D6D6"></td></tr>
		<tr>
			<td width="150" class="ct_td">
			
			<anyframe:message code="product.prodDetail"/></td><td bgcolor="D6D6D6" width="1"></td>
			<td class="ct_write01">									        
	        <form:textarea path="prodDetail" id="prodDetail" cols="50" rows="5" cssClass="ct_input_g" cssErrorClass="text medium error"/>
			</td>
		</tr>	
		<tr><td height="1" colspan="3" bgcolor="D6D6D6"></td></tr>
		<tr>
			<td width="150" class="ct_td">
			
			<anyframe:message code="product.manufactureDay"/></td><td bgcolor="D6D6D6" width="1"></td>
			<td class="ct_write01">									        
	        <form:input path="manufactureDay" id="manufactureDay" cssClass="ct_input_g" cssErrorClass="text medium error" maxlength="10"/>
	        <a href="javascript:show_calendar(document.productForm.rootPath.value,'document.productForm.manufactureDay', document.productForm.manufactureDay.value)" id="iconCalendarregDate"><img src="<c:url value='/sample/images/ct_icon_date.gif'/>" width="16" height="18" border="0" align="absmiddle"></a>
			</td>
		</tr>	
		<tr><td height="1" colspan="3" bgcolor="D6D6D6"></td></tr>
		<tr>
			<td width="150" class="ct_td">
			
			<anyframe:message code="product.sellQuantity"/></td><td bgcolor="D6D6D6" width="1"></td>
			<td class="ct_write01">									        
	        <form:input path="sellQuantity" id="sellQuantity" cssClass="ct_input_g" cssErrorClass="text medium error" maxlength="255"/>
			</td>
		</tr>	
		<tr><td height="1" colspan="3" bgcolor="D6D6D6"></td></tr>
		<tr>
			<td width="150" class="ct_td">
			
			<anyframe:message code="product.sellAmount"/></td><td bgcolor="D6D6D6" width="1"></td>
			<td class="ct_write01">									        
	        <form:input path="sellAmount" id="sellAmount" cssClass="ct_input_g" cssErrorClass="text medium error" maxlength="255"/>
			</td>
		</tr>								
		<tr><td height="1" colspan="3" bgcolor="D6D6D6"></td></tr>
		<tr>
			<td width="150" class="ct_td">
			
			<anyframe:message code="product.asYn"/></td><td bgcolor="D6D6D6" width="1"></td>
			<td class="ct_write01">
			<form:checkbox path="asYn" id="asYn" value="Y"/>&nbsp;&nbsp;<anyframe:message code="product.as.possible"/>
			</td>
		</tr>
		<tr><td height="1" colspan="3" bgcolor="D6D6D6"></td></tr>
		<tr>
			<td width="150" class="ct_td">
			<anyframe:message code="product.imageFile"/></td><td bgcolor="D6D6D6" width="1"></td>
			<td class="ct_write01">		
				<c:if test="${not empty product.imageFile}">	
					<img src="<c:url value='${product.imageFile}'/>"
						alt="<anyframe:message code='product.imageFile'/>" border="0" onError="this.width=0;"/>
				</c:if>
				<c:if test="${empty product.imageFile}">
					<input type="file" name="realImageFile" class="ct_input_g" style="width:309px; height:19px" fieldTitle="Product Image" maxLength="100" >
				</c:if>							        
			</td>
		</tr>
	<tr><td height="1" colspan="3" bgcolor="D6D6D6"></td></tr>
	<input type="hidden" name="rootPath" value="<c:url value='/'/>"/>		
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
					<c:if test="${empty product.prodNo}">	
						<td width="17" height="23"><img src="<c:url value='/sample/images/ct_btnbg01.gif'/>" width="17" height="23"></td>
						<td background="<c:url value='/sample/images/ct_btnbg02.gif'/>" class="ct_btn01" style="padding-top:3px;"><a href="javascript:fncAddProduct();"><fmt:message key="button.add"/></a></td>
						<td width="14" height="23"><img src="<c:url value='/sample/images/ct_btnbg03.gif'/>" width="14" height="23"></td>
						<td width="30"></td>					
        			</c:if>
        					
					<c:if test="${not empty product.prodNo}">
						<td width="17" height="23"><img src="<c:url value='/sample/images/ct_btnbg01.gif'/>" width="17" height="23"></td>
						<td background="<c:url value='/sample/images/ct_btnbg02.gif'/>" class="ct_btn01" style="padding-top:3px;"><a href="javascript:fncUpdateProduct();"><fmt:message key="button.update"/></a></td>
						<td width="14" height="23"><img src="<c:url value='/sample/images/ct_btnbg03.gif'/>" width="14" height="23"></td>					
						<td width="30"></td>
						
						<td width="17" height="23"><img src="<c:url value='/sample/images/ct_btnbg01.gif'/>" width="17" height="23"></td>
						<td background="<c:url value='/sample/images/ct_btnbg02.gif'/>" class="ct_btn01" style="padding-top:3px;"><a href="javascript:fncDeleteProduct();"><fmt:message key="button.delete"/></a></td>
						<td width="14" height="23"><img src="<c:url value='/sample/images/ct_btnbg03.gif'/>" width="14" height="23"></td>
						<td width="30"></td>
        			</c:if>
													
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