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
<display:table name="sms" id="sms" requestURI="${requestURI}" pagesize="10" class="table table-hover">

	<thead> 
		<tr> 
			<th><spring:message code="sms.subject" var="titleHeader" /></th>
			<th><spring:message code="sms.moment" var="momentHeader" /></th>
			<th><spring:message code="sms.body" var="bodyHeader" /></th>
		</tr>
	</thead>
	<tbody>
		<tr>
			<td><display:column property="subject" title="${subjectHeader}" sortable="true" /> </td>
			<td><display:column property="moment" title="${momentHeader}" sortable="true" /> </td>
			<td><display:column property="body" title="${bodyHeader}" sortable="false" /> </td>
	</tr>
	
	<%-- <display:column>
	  	<div>
	  	<a href="sms/delete.do?smsId=${sms.id}">
	  		<spring:message code="sms.delete" var="smsHeader" />
	  		<jstl:out value="${smsHeader}" />
	  	</a>
	  	</div>
  	</display:column> --%>

</tbody>

</display:table>

</div>


