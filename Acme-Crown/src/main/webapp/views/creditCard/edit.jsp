<%--
 * display.jsp
 *
 * Copyright (C) 2016 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>




<form:form action="creditCard/edit.do" modelAttribute="creditCard" style="width:300px; margin-left:550px; margin-top:20px">
	
	<form:hidden path="id" />
	
	<acme:textbox code="creditCard.holder" path="holder"/>
	<acme:textbox code="creditCard.brand" path="brand"/>
	<acme:textbox code="creditCard.number" path="number"/>
	<acme:textbox code="creditCard.expirationMonth" path="expirationMonth"/>
	<acme:textbox code="creditCard.expirationYear" path="expirationYear"/>
	<acme:textbox code="creditCard.cvv" path="cvv"/>
	
		
	<div class="row">
	 	<div class="col-md-6 col-sm-6 col-xs-6 pad-adjust">
	 		<button type="submit" name="save" class="btn btn-success">
	 			<i class="glyphicon glyphicon-floppy-saved"></i> <spring:message code="creditCard.save" />
	 		</button>
		</div>
		<div class="col-md-6 col-sm-6 col-xs-6 pad-adjust">
			<input type="button" name="cancel" value="<spring:message code="creditCard.cancel" />"  class="btn btn-danger" onclick="window.location='welcome/index.do'" /> <br />
		</div>
	</div>
</form:form>
