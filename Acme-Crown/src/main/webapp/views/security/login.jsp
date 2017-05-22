 <%--
 * login.jsp
 *
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<!-- <img src="images/munecote.png" class="img-rounded" alt="Cinque Terre"  style="position:relative; width:25%; margin-top:-1em;"> -->

<div class="container">


	<div id="loginbox" class="mainbox col-md-6 col-md-offset-3 col-sm-6 col-sm-offset-3"> 

    <div class="panel panel-default">
            <div class="panel-heading" >
                <div class="panel-title text-center">Login</div>
            </div> 
            
      <div class="panel-body">
<form:form action="j_spring_security_check" modelAttribute="credentials" class="form-horizontal">

<div class="input-group">
	<form:label path="username" >
		<spring:message code="security.username" />
	</form:label>
	<form:input path="username" class="form-control" name="user" style="width:295px;" />	
	<form:errors class="error" path="username" />
	<br />
</div>

<div class="input-group">

	<form:label path="password">
		<spring:message code="security.password" />
	</form:label>
	 <input id="password" type="password" class="form-control" name="password" placeholder="Password" style="width:295px;">
	<%--<form:password path="password" class="form-control" name="password" style="width=230px;"/> --%>	
	<form:errors class="error" path="password" />
	<br />
	</div>
	
	
	<jstl:if test="${showError == true}">
		<div class="error">
			<spring:message code="security.login.failed" />
		</div>
	</jstl:if>
	
	<div class="form-group" style="margin-top:15px;">
	 	<div class="col-sm-12 controls">
	 	 	<input  type="submit" value="<spring:message code="security.login"  />" class="btn btn-primary pull-right" style="margin-top:-10px"/>
     	</div>
	</div>
	
	
</form:form>
</div>
</div>
</div>

</div>


