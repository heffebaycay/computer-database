<%@ tag language="java" pageEncoding="UTF-8" description="Pagination template" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="u" uri="/WEB-INF/utils.tld" %>
<%@ attribute name="currentPage" required="true" type="java.lang.Integer" %>
<%@ attribute name="totalPage" required="true"  type="java.lang.Integer" %>
<%@ attribute name="delta" required="true" type="java.lang.Integer"  %>
<%@ attribute name="urlPattern" required="true" %>

<ul class="pagination">

	<c:if test="${ currentPage gt 1 }">
		<li>
			<a href="<c:url value="${ u:formatString(urlPattern, currentPage - 1) }" />" aria-label="Previous">
            	<span aria-hidden="true">&laquo;</span>
           	</a>
		</li>
	</c:if>
	
	<c:set var="begin" value="${ currentPage - delta }" />
	<c:if test="${ begin < 1 }">
		<c:set var="begin" value="${ 1 }" />
	</c:if>
	
	<c:forEach var="i" begin="${ begin }" end="${ currentPage - 1 }" >
		<li>
			<a href="<c:url value="${ u:formatString(urlPattern, i) }" />">${ i }</a>
		</li>
	</c:forEach>
	
	<li class="active">
		<a href="">${ currentPage }</a>
	</li>
	
	<c:set var="end" value="${ currentPage + delta }" />
	<c:if test="${ end > totalPage }">
		<c:set var="end" value="${ totalPage }" />
	</c:if>            	
	
	<c:forEach var="i" begin="${ currentPage + 1 }" end="${ end }">
		<li>
			<a href="<c:url value="${ u:formatString(urlPattern, i) }" />">${ i }</a>
		</li>
	</c:forEach>
	
	<c:if test="${ currentPage lt totalPage }">
		<li>
			<a href="<c:url value="${ u:formatString(urlPattern, currentPage + 1) }" />" aria-label="Next">
				<span aria-hidden="true">&raquo;</span>
			</a>
		</li>
	</c:if>
	
</ul>
