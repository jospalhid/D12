
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<form:form action="security/signin.do" modelAttribute="actorForm">
	
	<acme:textbox code="security.username" path="username"/>
	<acme:password code="security.password" path="password1"/>
	<acme:password code="security.password2" path="password2"/>
	<br/>

	<acme:textbox code="security.name" path="name"/>
	<acme:textbox code="security.surname" path="surname"/>
	<acme:textbox code="security.email" path="email"/>
	<acme:textbox code="security.phone" path="phone"/>
	<acme:textbox code="security.picture" path="picture"/>
	
	<br/>
	<acme:checkbox code="security.condition" value="acepto" path="conditions"/>
	
	<br/><br/>
	<input type="submit" name="signin" value="<spring:message code="security.submit" />" />
	<input type="button" name="cancel" value="<spring:message code="security.cancel" />" onclick="window.location='welcome/index.do'" /> <br />
	
</form:form>