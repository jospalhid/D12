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


<form:form action="extrareward/crown/edit.do" modelAttribute="extraReward">
	
	<form:hidden path="id" />
	<form:hidden path="project" />
	
	<acme:textbox code="extrareward.title" path="title"/>
	<acme:textarea code="extrareward.description" path="description"/>
	<acme:textbox code="extrareward.goal" path="goal"/>

	<button type="submit" name="save" class="btn btn-success">
		<i class="glyphicon glyphicon-floppy-saved"></i> <spring:message code="project.save" />
	</button>
	
	<input type="button" name="cancel" class="btn btn-danger" value="<spring:message code="project.cancel" />" onclick="window.location='project/display.do?projectId=${extraReward.project.id}'" /> <br />

</form:form>
