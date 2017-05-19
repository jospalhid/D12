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
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>


<div class="container">
	<div class="error">
		<jstl:out value="${patron}"/>
	</div>

	<div>
		<h1><jstl:out value="${project.title}"/></h1>
		<p><jstl:out value="${project.description}"/></p>
		<strong><spring:message code="project.category"/>: </strong><jstl:out value="${project.category.name }"></jstl:out><br/>
		<strong><spring:message code="project.goal"/>: </strong> <jstl:out value="${project.goal}"/>$<br>
		<strong><spring:message code="project.currentGoal"/>: </strong> <jstl:out value="${currentGoal}"/>$<br>
		<strong><spring:message code="project.brackers"/>: </strong> <jstl:out value="${brackers}"/><br>
		<jstl:choose>
			<jstl:when test="${days >0 }">
				<strong><spring:message code="project.days"/>:</strong> <jstl:out value="${days}"/><br>
			</jstl:when>
			<jstl:otherwise>
				<spring:message code="project.finish"/> <jstl:out value="${project.ttl}"/>.
				<jstl:if test="${currentGoal>=project.goal}">
					<spring:message code="project.exito"/><img src="./images/smile.png" width="25px" alt="smail"/>
				</jstl:if>
			</jstl:otherwise>
		</jstl:choose>
	</div>
	
	<jstl:if test="${crown.id == project.crown.id}">
	<jstl:if test="${days>5}">
	 		<a href="project/crown/edit.do?projectId=${project.id}" class="btn btn-primary" style="margin:5px;">
	 		<spring:message code="project.edit" var="edtiProjectHeader" />
			<jstl:out value="${edtiProjectHeader}" />
		</a>
	</jstl:if>
	</jstl:if>
	
	<div id="myCarousel" class="carousel slide" data-ride="carousel" style="width:500px; heigth:300px">
		<ol class="carousel-indicators">
			<jstl:set var="index" value="0"/>
			<jstl:set var="active" value="true"/>
			<jstl:forEach var="row" items="${project.pictures}">
				<jstl:choose>
					<jstl:when test="${active==true}">
						<li data-target="project/display.do?projectId=${project.id}#myCarousel" data-slide-to="${index }" class="active"></li>
						<jstl:set var="active" value="false"/>
					</jstl:when>
					<jstl:otherwise>
						<li data-target="project/display.do?projectId=${project.id}#myCarousel" data-slide-to="${index }"></li>
					</jstl:otherwise>
				</jstl:choose>
				<jstl:set var="index" value="${index+1}"/>
			</jstl:forEach>
		</ol>
		
    	<jstl:set var="active" value="true"/>
  		<div class="carousel-inner" >
			<jstl:forEach var="row" items="${project.pictures}">
				<jstl:choose>
					<jstl:when test="${active==true}">
						<div class="item active">
							<img src="${row.url}" width="500px" alt="${row.alt}">
							<jstl:set var="active" value="false"/>
   						</div>
					</jstl:when>
					<jstl:otherwise>
						<div class="item">
							<img src="${row.url}" width="500px" alt="${row.alt}">
   						</div>
					</jstl:otherwise>
				</jstl:choose>
			</jstl:forEach>
			<a class="left carousel-control" href="project/display.do?projectId=${project.id}#myCarousel" data-slide="prev">
				<span class="sr-only">Previous</span>
			</a>
			<a class="right carousel-control" href="project/display.do?projectId=${project.id}#myCarousel" data-slide="next">
				<span class="sr-only">Next</span>
			</a>
		</div>
	</div>	
	<p><a href="project/crown/picture.do?projectId=${project.id }" class="btn btn-primary">
	 		<spring:message code="project.picture.add" var="addPictureHeader" />
			<jstl:out value="${addPictureHeader}" />
	</a></p>
		
	<div style="position: relative; left:500px; top:-600px; margin:30px; max-width:200px;">
		<h2><spring:message code="project.rewards"/></h2>
		
		<jstl:if test="${crown.id == project.crown.id}">
			<p><a href="reward/crown/create.do?projectId=${project.id }" class="btn btn-primary">
	 			<spring:message code="project.reward.add" var="addRewardHeader" />
				<jstl:out value="${addRewardHeader}" />
			</a></p>
		</jstl:if>
		
		<jstl:forEach var="row" items="${project.rewards}">
			<div class="recompensas">
				<jstl:out value="${row.cost}"/>$
				<h3><jstl:out value="${row.title}"/></h3>
				<p><jstl:out value="${row.description}"/></p>
				<jstl:if test="${days>0}">
					<p><a href="project/crown/reward.do?rewardId=${row.id }" <a href="#" class="btn btn-sm btn-success">Default text here</a>>
						<spring:message code="project.reward.select" var="selectHeader" />
						<jstl:out value="${selectHeader}" />
					</a></p>
					<jstl:if test="${crown.id == project.crown.id}">
						<jstl:set var="bb" value="${fn:length(row.crowns)}"/>
						<jstl:if test="${bb==0}">
						<p><a href="reward/crown/delete.do?rewardId=${row.id }" class="btn btn-sm btn-danger">
	 						<spring:message code="project.reward.delete" var="deleteRewardHeader" />
							<jstl:out value="${deleteRewardHeader}" />
						</a></p>
						</jstl:if>
					</jstl:if>
				</jstl:if>
			</div>
		</jstl:forEach>
	</div>
	
	<jstl:if test="${currentGoal>=project.goal}">
	<div>
		<h2><spring:message code="project.extraRewards"/></h2>
		
		<jstl:if test="${crown.id == project.crown.id}">
			<p><a href="extrareward/crown/create.do?projectId=${project.id }">
	 			<spring:message code="project.extrareward.add" var="addExtraRewardHeader" />
				<jstl:out value="${addExtraRewardHeader}" />
			</a></p>
		</jstl:if>
		
		<jstl:forEach var="row" items="${project.extraRewards}">
			<div style="border:solid 1px; width:180px; margin:5px; padding:10px">
				<jstl:out value="${row.goal}"/>$
				<h3><jstl:out value="${row.title}"/></h3>
				<p><jstl:out value="${row.description}"/></p>
			</div>
		</jstl:forEach>
	</div>
	</jstl:if>
</div>