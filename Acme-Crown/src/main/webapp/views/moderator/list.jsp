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
<display:table name="moderators" id="moderator" requestURI="${requestURI}" pagesize="10" class="table table-hover">
	
	<thead> 
		<tr> 
			<th><spring:message code="moderator.name" var="nameHeader" /></th>
			<th><spring:message code="moderator.surname" var="surnameHeader" /></th>
			<th><spring:message code="moderator.email" var="emailHeader" /></th>
			<th><spring:message code="moderator.phone" var="phoneHeader" /></th>
			<th><spring:message code="moderator.level" var="levelHeader" /></th>
		</tr>
	</thead>
	<tbody>
		<tr>
			<td><display:column property="name" title="${nameHeader}" sortable="true" /> </td>
			<td><display:column property="surname" title="${surnameHeader}" sortable="true" /> </td>
			<td><display:column property="email" title="${emailHeader}" sortable="true" /> </td>
			<td><display:column property="phone" title="${phoneHeader}" sortable="true" /> </td>
			<td>
				<display:column title="${levelHeader}" sortable="true">
					<jstl:choose>
						<jstl:when test="${moderator.level==1 }">
							<img src="./images/silver.png" alt="Silver" width="25">
						</jstl:when>
						<jstl:when test="${moderator.level==2 }">
							<img src="./images/gold.png" alt="Gold" width="25">
						</jstl:when>
					</jstl:choose>
				</display:column>
			</td>
		</tr>
		
	<display:column>
		<img src="${moderator.picture }" alt="${moderator.surname }" width="75">
	</display:column>
	
	<display:column>
		<jstl:choose>
		<jstl:when test="${moderator.banned==false}">
		<a href="moderator/admin/ban.do?moderatorId=${moderator.id}">
			<spring:message code="moderator.ban" var="banHeader" />
			<jstl:out value="${banHeader}" />
		</a>
		</jstl:when>
		<jstl:otherwise>
		<a href="moderator/admin/unban.do?moderatorId=${moderator.id}">
			<spring:message code="moderator.unban" var="unbanHeader" />
			<jstl:out value="${unbanHeader}" />
		</a>
		</jstl:otherwise>
		</jstl:choose>
	</display:column>
	
	<display:column>
		<jstl:choose>
		<jstl:when test="${moderator.level==1}">
		<a href="moderator/admin/level.do?moderatorId=${moderator.id}">
			<img src="./images/up.png" alt="Up" width="25">
		</a>
		</jstl:when>
		<jstl:otherwise>
		<a href="moderator/admin/level.do?moderatorId=${moderator.id}">
			<img src="./images/down.png" alt="Down" width="25">
		</a>
		</jstl:otherwise>
		</jstl:choose>
	</display:column>
	
	</tbody>
</display:table>
	
</div>