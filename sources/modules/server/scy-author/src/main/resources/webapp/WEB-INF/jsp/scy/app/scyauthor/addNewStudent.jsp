<%@ include file="common-taglibs.jsp" %>
<tiles:insertDefinition name="dialog-page">
    <tiles:putAttribute name="main">


        

        <form action="addNewStudent.html">

            <table width="100%">
                <tr>
                    <td>
                       <spring:message code="FIRST_NAME"/>
                    </td>
                    <td>
                        <input type="text" name="firstName"/>
                    </td>
                </tr>
                <tr>
                    <td>
                        <spring:message code="LAST_NAME"/>
                    </td>
                    <td>
                        <input type="text" name="lastName"/>
                    </td>
                </tr>
                <tr>
                    <td>
                        <spring:message code="USERNAME"/>
                    </td>
                    <td>
                        <input type="text" name="userName"/>
                    </td>
                </tr>
                <tr>
                    <td>
                        <spring:message code="PASSWORD"/>
                    </td>
                    <td>
                        <input type="password" name="password"/>
                    </td>
                </tr>
                <tr>
                    <td colspan="2" align="right" >
                        <input type="submit"/>
                    </td>
                </tr>
                <input type="hidden" value="addStudent" name="action"/>
                <input type="hidden" value="${eloURI}" name="eloURI"/>

            </table>

        </form>

    </tiles:putAttribute>
</tiles:insertDefinition>
