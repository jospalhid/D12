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


<div>
	<h2><spring:message code="project.card"/></h2>
	<p><strong><jstl:out value="${card.holder}"/></strong></p>
	<strong><spring:message code="project.card.number"/>:</strong> **** **** **** <jstl:out value="${number}"/><br>
	<strong><spring:message code="project.card.expiration"/>:</strong> <jstl:out value="${card.expirationMonth }"/>/<jstl:out value="${card.expirationYear }"/>
	<br/><jstl:out value="${card.brand}"/>
</div>

<div style="border:solid 1px; width:180px; margin:5px; padding:10px">
	<jstl:out value="${reward.cost}"/>$
	<h3><jstl:out value="${reward.title}"/></h3>
	<p><jstl:out value="${reward.description}"/></p>
</div>

<form:form action="project/crown/reward.do" modelAttribute="reward">
	
	<form:hidden path="id" />
	
	<input type="submit" name="save" value="<spring:message code="project.join" />" onclick="return confirm('<spring:message code="event.confirm.join" />')" />&nbsp;
	
	<input type="button" name="cancel" value="<spring:message code="project.cancel" />" onclick="window.location='project/display.do?projectId=${reward.project.id}'" /> <br />

</form:form>