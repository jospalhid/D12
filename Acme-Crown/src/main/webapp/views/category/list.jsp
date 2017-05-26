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

<display:table name="categories" id="category" requestURI="${requestURI}" pagesize="10" class="table table-hover">

	<display:column>
		<img src="${category.picture }" alt="${category.name }" width="40">
	</display:column>
	
	<thead> 
		<tr> 
			<th><spring:message code="category.name" var="nameHeader" /></th>
			<th><spring:message code="category.description" var="descriptionHeader" /></th>
		</tr>
	</thead>
	<tbody>
		<tr>
			<td><display:column property="name" title="${nameHeader}" sortable="true" /> </td>
			<td><display:column property="description" title="${descriptionHeader}" sortable="false" /> </td>
	</tr>
	
	<display:column>
		<a href="category/admin/edit.do?categoryId=${category.id }">
			<spring:message code="categor.edit" var="editHeader" />
			<jstl:out value="${editHeader}" />
		</a>
	</display:column>

</tbody>

</display:table>

</div>