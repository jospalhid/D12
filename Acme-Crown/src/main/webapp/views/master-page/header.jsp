<%--
 * header.jsp
 *
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>

<div>
	<img src="images/logo.png" alt="Acme-Crown Co., Inc." />
</div>

<nav class="navbar navbar-inverse">
<div>

	<div class="navbar-header">
      <a class="navbar-brand" href="#">Acme-Crown</a>
    </div>

	<ul id="jMenu" class="nav navbar-nav">
		<!-- Do not forget the "fNiv" class for the first level links !! -->
		<security:authorize access="hasRole('ADMIN')">
			<li class="dropdown"><a class="fNiv" class="dropdown-toggle" data-toggle="dropdown"><spring:message	code="master.page.administrator" /> <span class="caret"></span></a>
				<ul class="dropdown-menu">
					<li></li>
				</ul>
			</li>
		</security:authorize>
		
		<security:authorize access="hasRole('CUSTOMER')">
			<li class="dropdown"><a class="fNiv" class="dropdown-toggle" data-toggle="dropdown"><spring:message	code="master.page.customer" /><span class="caret"></span></a>
				<ul class="dropdown-menu">
					<li></li>
				</ul>
			</li>
		</security:authorize>
		
		<security:authorize access="isAnonymous()">
			<li><a class="fNiv"  href="security/login.do"><spring:message code="master.page.login" /><span class="caret"></span></a></li>
		</security:authorize>
		
		<security:authorize access="isAuthenticated()">
			<li class="dropdown">
				<a class="fNiv" class="dropdown-toggle" data-toggle="dropdown"> 
					<spring:message code="master.page.profile" /> 
			        (<security:authentication property="principal.username" />)
				<span class="caret"></span></a>
				<ul class="dropdown-menu">
					<li></li>
					<li><a href="j_spring_security_logout"><spring:message code="master.page.logout" /> </a></li>
				</ul>
			</li>
		</security:authorize>
		
		<li class="dropdown"><a class="fNiv" class="dropdown-toggle" data-toggle="dropdown"><spring:message	code="master.page.project" /><span class="caret"></span></a>
			<ul class="dropdown-menu">
				<li></li>
				<li><a href="project/available.do"><spring:message code="master.page.project.available" /></a></li>
			</ul>
		</li>
	</ul>
</div>

</nav>
<div>
<kbd><a href="?language=en">en</a> | <a href="?language=es">es</a></kbd>
</div>

