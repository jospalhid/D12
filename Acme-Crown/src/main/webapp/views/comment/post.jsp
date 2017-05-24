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

<form:form action="comment/crown/save.do" modelAttribute="comment">
	
	<form:hidden path="project"/>
	
  	<acme:textbox code="comment.title" path="title"/>
  	<acme:textarea code="comment.text" path="text"/>
  	<acme:textbox code="comment.stars" path="stars"/>
	
	<input type="submit" name="save" value="<spring:message code="comment.save" />" />
	<input type="button" name="cancel" value="<spring:message code="comment.cancel" />" onclick="window.location='project/display.do?projectId=${reward.project.id}'" /> <br />
</form:form>