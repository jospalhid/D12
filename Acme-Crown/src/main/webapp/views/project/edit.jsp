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
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>


<form:form action="event/manager/edit.do" modelAttribute="event">
	
	<form:hidden path="id" />

	<acme:textbox code="event.title" path="title"/>
	<acme:textbox code="event.description" path="description"/>
	<acme:textbox code="event.picture" path="picture"/>
	<acme:textbox code="event.seatsOffered" path="seatsOffered"/>
	<br>
	<spring:message code="event.moment" var="momentHeader" />
	<jstl:out value="${momentHeader}" />
	<acme:textbox code="event.day" path="day"/>
	<acme:textbox code="event.month" path="month"/>
	<acme:textbox code="event.year" path="year"/>
	<acme:textbox code="event.hour" path="hour"/>
	<acme:textbox code="event.minutes" path="minutes"/>
	<br>
	
	<input type="submit" name="save" value="<spring:message code="event.save" />" />
	
	<jstl:if test="${event.id != 0}">
		<input type="submit" name="delete" value="<spring:message code="event.delete" />" onclick="return confirm('<spring:message code="event.confirm.delete" />')" />&nbsp;
	</jstl:if>
	
	<input type="button" name="cancel" value="<spring:message code="event.cancel" />" onclick="window.location='event/manager/list.do'" /> <br />
	
	<div>
		<jstl:out value="${errors}"/>
	</div>

</form:form>