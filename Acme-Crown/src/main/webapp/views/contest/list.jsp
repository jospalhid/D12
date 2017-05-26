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

<div class="container">

<!-- He quitado class="displaytag" del display:table -->
<display:table name="contests" id="contest" requestURI="${requestURI}" pagesize="10" class="table table-hover">

	<jsp:useBean id="currentDate" class="java.util.Date" />
	<fmt:formatDate value="${currentDate}" pattern="MM" var="currentMonth" />
	<fmt:formatDate value="${currentDate}" pattern="dd" var="currentDay" />
	<fmt:formatDate value="${currentDate}" pattern="yy" var="currentYear" />
	<fmt:formatDate value="${contest.moment}" pattern="MM" var="projectMonth" />
	<fmt:formatDate value="${contest.moment}" pattern="dd" var="projectDay" />
	<fmt:formatDate value="${contest.moment}" pattern="yy" var="projectYear" />

	<display:column>
		<jstl:if test="${currentYear==projectYear && currentMonth==projectMonth && currentDay>projectDay}">
			<img src="./images/lock.png" alt="Lock" width="16">
		</jstl:if>
	</display:column>

	<thead> 
		<tr> 
			<th><spring:message code="contest.topic" var="topicHeader" /></th>
			<th><spring:message code="contest.moment" var="momentHeader" /></th>
			<th><spring:message code="contest.award" var="awardHeader" /></th>
		</tr>
	</thead>
	<tbody>
		<tr>
			<td><display:column property="topic" title="${topicHeader}" sortable="true" /> </td>
	
			<td><display:column property="moment" title="${momentHeader}" sortable="true" /> </td>
	
			<td><display:column property="award" title="${awardHeader}" sortable="true" /> </td>
	</tr>
	<display:column>
	<security:authorize access="hasRole('CROWN')">
		<a href="contest/display.do?contestId=${contest.id}">
			<img src="./images/eye.png" alt="Display" width="25">
		</a>
	</security:authorize>
	</display:column>
	
	<display:column>
	<security:authorize access="hasRole('ADMIN')">
	<jstl:if test="${currentYear<projectYear || (currentYear==projectYear && currentMonth<projectMonth) || (currentMonth==projectMonth && currentDay<projectDay)}">
		<a href="contest/admin/edit.do?contestId=${contest.id}">
			<spring:message code="contest.edit" var="editHeader" />
			<jstl:out value="${editHeader}" />
		</a>
	</jstl:if>
	</security:authorize>
	</display:column>
	
	<display:column>
		<security:authorize access="hasRole('ADMIN')">
		<jstl:if test="${up==true }">
		<a href="contest/admin/winner.do?contestId=${contest.id}">
			<img src="./images/winner.png" alt="Winner" width="25">
		</a>
		</jstl:if>
		</security:authorize>
	</display:column>

</tbody>

</display:table>


</div>