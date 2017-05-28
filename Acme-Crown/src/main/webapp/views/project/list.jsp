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
<display:table name="projects" id="project" requestURI="${requestURI}" pagesize="10" class="table table-hover">

	<jsp:useBean id="currentDate" class="java.util.Date" />
	<fmt:formatDate value="${currentDate}" pattern="MM" var="currentMonth" />
	<fmt:formatDate value="${currentDate}" pattern="dd" var="currentDay" />
	<fmt:formatDate value="${project.ttl}" pattern="MM" var="projectMonth" />
	<fmt:formatDate value="${project.ttl}" pattern="dd" var="projectDay" />

	<display:column>
		<jstl:choose>
		<jstl:when test="${project.banned==true}">
			<img src="./images/ban.png" alt="Banned" width="16">
		</jstl:when>
		<jstl:when test="${currentMonth==projectMonth && currentDay>projectDay}">
			<img src="./images/lock.png" alt="Lock" width="16">
		</jstl:when>
		<jstl:when test="${project.promoted==true}">
			<img src="./images/crown.png" alt="Promoted" width="16">
		</jstl:when>
		</jstl:choose>
	</display:column>

	<thead> 
		<tr> 
			<th><spring:message code="project.title" var="titleHeader" /></th>
			<th><spring:message code="project.goal" var="goalHeader" /> </th>
			<th><spring:message code="project.ttl" var="ttlHeader" /> </th>
			<th><spring:message code="project.category" var="categoryHeader" /> </th>
		</tr>
	</thead>
	<tbody>
		<tr>
			<td><display:column property="title" title="${titleHeader}" sortable="true" /> </td>
	
	
			<td><display:column property="goal" title="${goalHeader}" sortable="true" /> </td>
	
	
			<td><display:column property="ttl" title="${ttlHeader}" sortable="true" /> </td>
	
	
			<td><display:column property="category.name" title="${categoryHeader}" sortable="true" /> </td>
	</tr>
	
	<security:authorize access="isAnonymous() or hasRole('CROWN') or hasRole('MODERATOR')">
	<display:column>
		<a href="project/display.do?projectId=${project.id}">
			<img src="./images/eye.png" alt="Display" width="25">
		</a>
	</display:column>
	</security:authorize>

</tbody>

</display:table>


</div>