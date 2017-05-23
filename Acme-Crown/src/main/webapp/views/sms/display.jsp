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
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>


<div class="container">
	
	<div>
		<h1><jstl:out value="${sms.subject}"/></h1>
		<p><jstl:out value="${sms.moment}"/></p>
		<strong><spring:message code="sms.recipient"/>: </strong><jstl:out value="${sms.recipient.userAccount.username }"></jstl:out><br/>
		<strong><spring:message code="sms.sender"/>: </strong><jstl:out value="${sms.sender.userAccount.username }"></jstl:out><br/><br/>
		<jstl:out value="${sms.body }"></jstl:out>
	</div>
	
	<%-- <form:form action="sms/delete.do" modelAttribute="sms">
	<form:hidden path="id" />
	
	<input type="submit" name="delete"
			value="<spring:message code="sms.delete" />"
			onclick="return confirm('<spring:message code="sms.confirm.delete" />')" />&nbsp;
	
	</form:form>
	 --%>
</div>