<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="u" uri="/WEB-INF/utils.tld" %>

<jsp:include page="include/header.jsp" />

<section id="main">
        <div class="container">
            <div class="row">
                <div class="col-xs-8 col-xs-offset-2 box">
                    <div class="label label-default pull-right">
                        id: ${ computer.id }
                    </div>
                    <h1>Edit Computer</h1>
                   
                   <c:if test="${ errors != null }">
                   	<c:choose>
                   		<c:when test="${ errors.size() > 0 }">
                   			<div id="msgErrors" class="alert alert-danger">
                   				<strong>Oh snap!</strong> It seems you left some mistakes on our sweet form.
                   				<c:forEach items="${ errors }" var="error">
                   					<p>${ error }</p>
                   				</c:forEach>
                   			</div>
                   		</c:when>
                   		<c:otherwise>
                   			<div id="msgComputerUpdated" class="alert alert-success">
                   				<strong>Success!</strong> Your modifications were saved successfully.
                   			</div>
                   		</c:otherwise>
                   	</c:choose>
                   </c:if>
                   
                   <c:if test="${ bAddSuccess != null && bAddSuccess == true }">
                   		<div id="msgComputerAdded" class="alert alert-success">
                   			<strong>Success!</strong> Computer was added successfully. Feel free to take a moment to check for any typo!
                   		</div>
                   </c:if>
                   
                    <form action="<c:url value="/computers/edit?id=${ computer.id }" />" method="POST">
                        <input type="hidden" value="0"/>
                        <fieldset>
                            <div class="form-group">
                                <label for="computerName">Computer name</label>
                                <input type="text" class="form-control" id="computerName" name="computerName" placeholder="Computer name" value="<c:choose><c:when test="${ computerNameValue != null }"><c:out value="${ computerNameValue }" /></c:when><c:otherwise><c:if test="${ computer != null }"><c:out value="${ computer.name }" /></c:if></c:otherwise></c:choose>">
                            </div>
                            <div class="form-group">
                                <label for="introduced">Introduced date</label>
                                <input type="text" class="form-control" id="introduced" name="introduced" placeholder="Introduced date" value="<c:choose><c:when test="${ dateIntroducedValue != null }"><c:out value="${ dateIntroducedValue }" /></c:when><c:otherwise><c:if test="${ computer != null }"><c:out value="${ computer.introduced }" /></c:if></c:otherwise></c:choose>">
                            </div>
                            <div class="form-group">
                                <label for="discontinued">Discontinued date</label>
                                <input type="text" class="form-control" id="discontinued" name="discontinued" placeholder="Discontinued date" value="<c:choose><c:when test="${ dateDiscontinuedValue != null }"><c:out value="${ dateDiscontinuedValue }" /></c:when><c:otherwise><c:if test="${ computer != null }"><c:out value="${ computer.discontinued }" /></c:if></c:otherwise></c:choose>">
                            </div>
                            <div class="form-group">
                                <label for="companyId">Company</label>
                                <select class="form-control" id="companyId" name="companyId">
                                    <option value="-1">--</option>
                                    <c:forEach items="${ companies }" var="company">
                                    	<option value="${ company.id }"
                                    		<c:choose>
                                    			<c:when test="${ companyIdValue != null }">
                                    				<c:if test="${ company.id == companyIdValue }">
                                    					selected="selected"
                                    				</c:if>
                                    			</c:when>
                                    			<c:otherwise>
                                    				<c:if test="${ computer.company != null && company.id == computer.company.id }">
                                    					selected="selected"
                                    				</c:if>
                                    			</c:otherwise>
                                    		</c:choose> > <c:out value="${ company.name }" /> </option>
                                    </c:forEach>
                                </select>
                            </div>            
                        </fieldset>
                        <div class="actions pull-right">
                        	<input type="hidden" name="computerId" id="computerId" value="${ computer.id }" />
                            <input type="submit" value="Edit" class="btn btn-primary">
                            or
                            <a href="<c:url value="/" />" class="btn btn-default">Cancel</a>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </section>

<jsp:include page="include/footer.jsp" />