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

<%--
<div>
	<img src="images/logoLetra.png"  style="height: 150px;" class="img-responsive"  alt="Acme-Crown Co., Inc." />
</div>
 --%>
<nav class="navbar navbar-inverse">
<div>

	<div class="navbar-header">
      <a class="navbar-brand" href="#"><spring:message code="master.page.home" /> </a>
    </div>

	<ul id="jMenu" class="nav navbar-nav">
		<!-- Do not forget the "fNiv" class for the first level links !! -->
		
		<security:authorize access="isAnonymous()">
			<li><a class="fNiv"  href="security/login.do"><spring:message code="master.page.login" /><span class="caret"></span></a></li>
			<li class="dropdown"><a class="fNiv" class="dropdown-toggle" data-toggle="dropdown"><spring:message	code="master.page.signin" /><span class="caret"></span></a>
				<ul class="dropdown-menu">
					<li></li>
					<li><a href="security/signin/crown.do"><spring:message code="master.page.signin.crown" /></a></li>
					<li><a href="security/signin/bidder.do"><spring:message code="master.page.signin.bidder" /></a></li>
				</ul>
			</li>
		</security:authorize>
		
		<security:authorize access="isAuthenticated()">
			<li class="dropdown">
				<a class="fNiv" class="dropdown-toggle" data-toggle="dropdown"> 
					<spring:message code="master.page.profile" /> 
			        (<security:authentication property="principal.username" />)
				<span class="caret"></span></a>
				<ul class="dropdown-menu">
					<li></li>
					<security:authorize access="hasRole('CROWN')">
						<li><a href="creditCard/edit.do"><spring:message code="master.page.creditCard" /></a></li>
					</security:authorize>
					<security:authorize access="hasRole('MODERATOR')">
						<li><a href="crown/moderator/list.do"><spring:message code="master.page.ban" /></a></li>
						<li><a href="project/moderator/crown.do"><spring:message code="master.page.moderator.projects" /></a></li>
					</security:authorize>
					<security:authorize access="hasRole('ADMIN')">
						<li><a href="moderator/admin/list.do"><spring:message code="master.page.moderators" /></a></li>
					</security:authorize>
					<security:authorize access="hasRole('CROWN') or hasRole('BIDDER')">
						<li><a href="actor/display.do"><spring:message code="master.page.display" /></a></li>
					</security:authorize>
					<li><a href="j_spring_security_logout"><spring:message code="master.page.logout" /> </a></li>
				</ul>
			</li>
		</security:authorize>
		
		<security:authorize access="hasRole('ADMIN') or hasRole('CROWN') or hasRole('BIDDER') or hasRole('MODERATOR')">
		<li class="dropdown"><a class="fNiv" class="dropdown-toggle" data-toggle="dropdown"><spring:message	code="master.page.sms" /><span class="caret"></span></a>
			<ul class="dropdown-menu">
				<li></li>
				<li><a href="sms/received.do"><spring:message code="master.page.sms.list.received" /></a></li>
				<li><a href="sms/send.do"><spring:message code="master.page.sms.list.send" /></a></li>
				<li><a href="sms/create.do"><spring:message code="master.page.sms.new" /></a></li>
			</ul>
		</li>
		</security:authorize>
		
		<security:authorize access="hasRole('ADMIN')">
		<li class="dropdown"><a class="fNiv" class="dropdown-toggle" data-toggle="dropdown"><spring:message	code="master.page.category" /><span class="caret"></span></a>
			<ul class="dropdown-menu">
				<li></li>
				<li><a href="category/admin/list.do"><spring:message code="master.page.category.list" /></a></li>
				<li><a href="category/admin/create.do"><spring:message code="master.page.category.new" /></a></li>
			</ul>
		</li>
		</security:authorize>
		
		<security:authorize access="hasRole('ADMIN') or hasRole('CROWN') or hasRole('MODERATOR') or isAnonymous()">
		<li class="dropdown"><a class="fNiv" class="dropdown-toggle" data-toggle="dropdown"><spring:message	code="master.page.project" /><span class="caret"></span></a>
			<ul class="dropdown-menu">
				<li></li>
				<li><a href="project/available.do"><spring:message code="master.page.project.available" /></a></li>
				<security:authorize access="hasRole('CROWN')">
					<li><a href="project/crown/create.do"><spring:message code="master.page.project.create" /></a></li>
					<li><a href="project/crown/list.do"><spring:message code="master.page.project.list" /></a></li>
					<li><a href="project/crown/favs.do"><spring:message code="master.page.project.favs" /></a></li>
					<li><a href="project/crown/contributions.do"><spring:message code="master.page.project.contributions" /></a></li>
				</security:authorize>
				<security:authorize access="hasRole('MODERATOR')">
					<li><a href="project/moderator/list.do"><spring:message code="master.page.project.mod" /></a></li>
				</security:authorize>
			</ul>
		</li>
		</security:authorize>
		
		<security:authorize access="hasRole('ADMIN') or hasRole('CROWN') or hasRole('MODERATOR') or hasRole('BIDDER') or isAnonymous()">
		<li class="dropdown"><a class="fNiv" class="dropdown-toggle" data-toggle="dropdown"><spring:message	code="master.page.contest" /><span class="caret"></span></a>
			<ul class="dropdown-menu">
				<li></li>
				<li><a href="contest/available.do"><spring:message code="master.page.contest.available" /></a></li>
				<security:authorize access="hasRole('ADMIN')">
				<li><a href="contest/admin/create.do"><spring:message code="master.page.contest.create" /></a></li>
				</security:authorize>
			</ul>
		</li>
		</security:authorize>
		
		<security:authorize access="hasRole('BIDDER') or hasRole('CROWN') or hasRole('ADMIN')">
		<li class="dropdown"><a class="fNiv" class="dropdown-toggle" data-toggle="dropdown"><spring:message	code="master.page.concepts" /><span class="caret"></span></a>
			<ul class="dropdown-menu">
				<li></li>
				<security:authorize access="hasRole('CROWN')">
				<li><a href="concept/crown/list.do"><spring:message code="master.page.concept.list" /></a></li>
				<li><a href="concept/crown/create.do"><spring:message code="master.page.concept.create" /></a></li>
				</security:authorize>
				<security:authorize access="hasRole('BIDDER')">
				<li><a href="concept/bidder/list.do"><spring:message code="master.page.concept.auction" /></a></li>
				<li><a href="concept/bidder/win.do"><spring:message code="master.page.concept.win" /></a></li>
				</security:authorize>
				<security:authorize access="hasRole('ADMIN')">
				<li><a href="concept/admin/list.do"><spring:message code="master.page.concept.all" /></a></li>
				</security:authorize>
			</ul>
		</li>
		</security:authorize>
	</ul>
</div>

</nav>
<div>
<kbd><a href="?language=en">en</a> | <a href="?language=es">es</a></kbd>
</div>

