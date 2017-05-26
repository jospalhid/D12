<%--
 * index.jsp
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

<jstl:if test="${unread>0 }">
<div style="position:absolute">
	<img src="./images/unread.png" alt="Unread" width="50"> <strong><jstl:out value="${unread }"></jstl:out></strong>
</div>
</jstl:if>

<div id="myCarousel" class="carousel slide" data-ride="carousel" style="margin-top: -1.5%;">

  <!-- Indicators -->
  <ol class="carousel-indicators">
    <li data-target="#myCarousel" data-slide-to="0" class="active"></li>
    <li data-target="#myCarousel" data-slide-to="1"></li>
    <li data-target="#myCarousel" data-slide-to="2"></li>
  </ol>
  
 <!-- Wrapper for slides -->
   <div class="carousel-inner">
   

    <div class="item active">
       
    	<spring:message code="index.image.crowdfounding" var="crowdfounding"/>
		<img src="${crowdfounding }">

	</div>
	
    <div class="item">

		<spring:message code="index.image.bocadillo" var="bocadillo"/>
		<img src="${bocadillo}">
    </div>
   

    <div class="item">

		<spring:message code="index.image.tutorial" var="tutorial_pagar"/>
		<img src="${tutorial_pagar}">
	
    </div>
  </div>


<!-- Left and right controls -->
  <a class="left carousel-control" href="#myCarousel" data-slide="prev">
    <span class="sr-only">Previous</span>
  </a>
  <a class="right carousel-control" href="#myCarousel" data-slide="next">
    <span class="sr-only">Next</span>
  </a>

</div>



