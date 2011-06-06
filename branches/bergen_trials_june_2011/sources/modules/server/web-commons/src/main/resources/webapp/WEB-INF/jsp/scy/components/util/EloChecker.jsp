<%@ include file="common-taglibs.jsp" %>
<tiles:insertDefinition name="default-page">
    <tiles:putAttribute name="main">


        <div id="regErrorMessages">
            <spring:bind path="eloUriForm.*">
                <c:forEach var="error" items="${status.errorMessages}">
                    <br/><c:out value="${error}"/>
                </c:forEach>
            </spring:bind>
        </div>

        <form:form action="EloChecker.html" commandName="eloUriForm">
            <form:input path="eloUri"/>
            <input type="submit"/>
        </form:form>
    </tiles:putAttribute>
</tiles:insertDefinition>