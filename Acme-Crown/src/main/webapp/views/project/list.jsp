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


	<jsp:useBean id="date" class="java.util.Date" />
	<fmt:formatDate value="${date}" pattern="dd" var="currentDay" />
	<fmt:formatDate value="${project.moment}" pattern="dd" var="projectDay"/>

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
	<display:column>
		<a href="project/display.do?projectId=${project.id}">
			<spring:message code="project.display" var="displayHeader" />
			<jstl:out value="${displayHeader}" />
		</a>
	</display:column>

	<%-- <security:authorize access="hasRole('CHORBI')">
	<jstl:if test="${own == true }">
		<display:column>
	  		<a href="event/chorbi/unregister.do?eventId=${event.id}">
	 			<spring:message code="event.unregister" var="unregisterHeader" />
		  		<jstl:out value="${unregisterHeader}" />
			 </a>
		</display:column>
	</jstl:if>
	<jstl:if test="${all == true }">
		<display:column>
	  		<a href="event/chorbi/register.do?eventId=${event.id}">
	 			<spring:message code="event.register" var="registerHeader" />
		  		<jstl:out value="${registerHeader}" />
			 </a>
		</display:column>
	</jstl:if>
	</security:authorize>
	
	
	<security:authorize access="hasRole('MANAGER')">
	<jstl:if test="${edit == true }">
	<display:column>
	  	<a href="event/manager/edit.do?eventId=${event.id}">
	 			<spring:message code="event.edit" var="editHeader" />
		  		<jstl:out value="${editHeader}" />
		 </a>
	</display:column>
	</jstl:if>
	</security:authorize> --%>
</tbody>

</display:table>


</div>