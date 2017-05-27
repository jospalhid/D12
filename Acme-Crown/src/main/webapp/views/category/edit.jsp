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


<form:form action="category/admin/edit.do" modelAttribute="category">
	
	<form:hidden path="id" />

	<acme:textbox code="category.name" path="name"/>
	<acme:textbox code="category.description" path="description"/>
	<acme:textbox code="category.picture" path="picture"/>
	  
     <button type="submit" name ="save" class="btn btn-success">
  		<i class="glyphicon glyphicon-floppy-saved"></i> <spring:message code="category.save" />
	</button>
     
	<jstl:if test="${category.id != 0}">
		<button type="submit" name ="delete" class="btn btn-danger" onclick="return confirm('<spring:message code="category.confirm.delete" />')">
  			<i class="glyphicon glyphicon-remove-sign"></i><spring:message code="category.delete" />
		</button>
	</jstl:if>
	
	<input type="button" name="cancel" class="btn btn-danger"  value="<spring:message code="category.cancel" />" onclick="window.location='category/admin/list.do'" /> <br />
	
            
  </form:form>
<br/>
