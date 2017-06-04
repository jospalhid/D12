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
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<div class="container">
	<div>
		<security:authorize access="hasRole('CROWN')">
			<spring:message code="actor.crown" var="crowHeader" />
			<h1><jstl:out value="${crowHeader}" /></h1>
			
		</security:authorize>
		<security:authorize access="hasRole('BIDDER')">
			<spring:message code="actor.bidder" var="bidderHeader" />
			<h1><jstl:out value="${bidderHeader}" /></h1>
		</security:authorize>
	</div>
	<div>
		<img src="${actor.picture }" alt="${actor.name }" width="150"><br>
		<strong><spring:message code="actor.name"/>: </strong><jstl:out value="${actor.name}"/><br>
		<strong><spring:message code="actor.surname"/>:</strong> <jstl:out value="${actor.surname}" /><br>
		<strong><spring:message code="actor.email"/>:</strong> <jstl:out value="${actor.email}" /><br>
		<strong><spring:message code="actor.phone"/>:</strong><jstl:out value="${actor.phone}" /><br>
		
	</div>
	<br>
	<security:authorize access="hasRole('CROWN')">
			<jstl:if test="${actor.amount > 0.0 }">
			<strong><spring:message code="actor.amount"/>: </strong><jstl:out value="${actor.amount}" /><br>
				<a href="crown/pay.do">
	 				<img src="./images/credit_card.png" alt="PayAmount" width="70">
				</a>
			</jstl:if>
	</security:authorize>
</div>