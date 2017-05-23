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
<display:table name="crowns" id="crown" requestURI="${requestURI}" pagesize="10" class="table table-hover">
	
	<thead> 
		<tr> 
			<th><spring:message code="crown.name" var="nameHeader" /></th>
			<th><spring:message code="crown.surname" var="surnameHeader" /></th>
			<th><spring:message code="crown.email" var="emailHeader" /></th>
			<th><spring:message code="crown.phone" var="phoneHeader" /></th>
		</tr>
	</thead>
	<tbody>
		<tr>
			<td><display:column property="name" title="${nameHeader}" sortable="true" /> </td>
			<td><display:column property="surname" title="${surnameHeader}" sortable="true" /> </td>
			<td><display:column property="email" title="${emailHeader}" sortable="true" /> </td>
			<td><display:column property="phone" title="${phoneHeader}" sortable="true" /> </td>
		</tr>
		
	<display:column>
		<img src="${crown.picture }" alt="${crown.surname }" width="50">
	</display:column>
	
	<display:column>
		<jstl:choose>
		<jstl:when test="${crown.banned==false}">
		<a href="crown/moderator/ban.do?crownId=${crown.id}">
			<spring:message code="crown.ban" var="banHeader" />
			<jstl:out value="${banHeader}" />
		</a>
		</jstl:when>
		<jstl:otherwise>
		<a href="crown/moderator/unban.do?crownId=${crown.id}">
			<spring:message code="crown.unban" var="unbanHeader" />
			<jstl:out value="${unbanHeader}" />
		</a>
		</jstl:otherwise>
		</jstl:choose>
	</display:column>
	
	
	</tbody>
</display:table>
	
</div>