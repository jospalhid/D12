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
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<div class="container">

<!-- He quitado class="displaytag" del display:table -->
<display:table name="concepts" id="concept" requestURI="${requestURI}" pagesize="10" class="table table-hover">

	<jsp:useBean id="currentDate" class="java.util.Date" />
	<fmt:formatDate value="${currentDate}" pattern="HH" var="currentHour" />
	<fmt:formatDate value="${currentDate}" pattern="mm" var="currentMin" />
	<fmt:formatDate value="${currentDate}" pattern="ss" var="currentSeg" />
	<jstl:set var="hours" value="${concept.ttl-currentHour}"/>
	<jstl:set var="mins" value="${60-currentMin}"/>
	<jstl:set var="segs" value="${60-currentSeg}"/>

	<security:authorize access="hasRole('CROWN')">
	<display:column>
		<jstl:choose>
		<jstl:when test="${concept.valid==1}">
			<img src="./images/valid.png" alt="Valid" width="16">
		</jstl:when>
		<jstl:when test="${concept.valid==2}">
			<img src="./images/invalid.png" alt="Invalid" width="16">
		</jstl:when>
		</jstl:choose>
	</display:column>
	</security:authorize>

	<thead> 
		<tr> 
			<th><spring:message code="concept.title" var="titleHeader" /></th>
			<security:authorize access="hasRole('CROWN') or hasRole('ADMIN')">
			<th><spring:message code="concept.dibs" var="dibsHeader" /></th>
			</security:authorize>
			<security:authorize access="hasRole('BIDDER') or hasRole('ADMIN')">
			<th><spring:message code="concept.descripcion" var="descripcionHeader" /></th>
			</security:authorize>
			<th><spring:message code="concept.ttl" var="ttlHeader" /></th>
		</tr>
	</thead>
	<tbody>
		<tr>
			<td><display:column property="title" title="${titleHeader}" sortable="true" /> </td>
			<security:authorize access="hasRole('CROWN') or hasRole('ADMIN')">
			<td><display:column property="dibs" title="${dibsHeader}" sortable="true" /> </td>
			<td><display:column property="ttl" title="${ttlHeader}" sortable="false" /> </td>
			</security:authorize>
			<security:authorize access="hasRole('BIDDER') or hasRole('ADMIN')">
			<td><display:column property="descripcion" title="${descripcionHeader}" sortable="false" /> </td>
			</security:authorize>
			<security:authorize access="hasRole('BIDDER')">
			<jstl:if test="${auction==true }">
			<td>
				<display:column title="${ttlHeader}" sortable="true">
					<jstl:out value="${hours}"/> <spring:message code="concept.hour" />, 
					<jstl:out value="${mins}"/> <spring:message	code="concept.minutes" /> <spring:message	code="concept.and" />  
					<jstl:out value="${segs}"/> <spring:message	code="concept.seconds" /> <spring:message code="concept.left" />
				</display:column>
			</td>
			</jstl:if>
			</security:authorize>
	</tr>
	
	<security:authorize access="hasRole('BIDDER')">
	<jstl:if test="${auction==true }">
	<display:column>
		<form:form action="concept/bidder/bid.do?conceptId=${concept.id }" modelAttribute="bid">
			
			<acme:textbox code="concept.bid.input" path="input"/>

			<input type="image" name="bid" src="./images/bid.png" alt="Bid" width="16">
		</form:form>
	</display:column>
	</jstl:if>
	</security:authorize>
	
	<security:authorize access="hasRole('ADMIN')">
		<display:column>
			<a href="concept/admin/valid.do?conceptId=${concept.id}"><img src="./images/valid.png" alt="Valid" width="16"></a>
			<a href="concept/admin/invalid.do?conceptId=${concept.id}"><img src="./images/invalid.png" alt="Invalid" width="16"></a>
		</display:column>
	</security:authorize>

</tbody>

</display:table>

</div>