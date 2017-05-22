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
		<h1><jstl:out value="${contest.title}"/></h1>
		<p><jstl:out value="${contest.description}"/></p>
		<strong><spring:message code="contest.topic"/>: </strong><jstl:out value="${contest.topic}"></jstl:out><br/>
		<strong><spring:message code="contest.moment"/>: </strong><jstl:out value="${contest.moment}"></jstl:out><br/>
		<strong><spring:message code="contest.award"/>: </strong><jstl:out value="${contest.award}"></jstl:out><br/>
	</div>
	
	<jstl:if test="${canJoin==true}">
	<security:authorize access="hasRole('CROWN')">
	<div>
		<a href="contest/crown/join.do?contestId=${contest.id}" class="btn btn-primary" style="margin:5px;">
	 		<spring:message code="contest.join" var="joinHeader" />
			<jstl:out value="${joinHeader}" />
		</a>
	</div>
	</security:authorize>
	</jstl:if>
	
	<br/>
	<display:table name="projects" id="project" requestURI="${requestURI}" pagesize="10" class="table table-hover">
	<display:caption><h2><spring:message code="contest.projects"/></h2></display:caption>
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
	
	</display:table>
</div>