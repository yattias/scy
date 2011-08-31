<%@ include file="common-taglibs.jsp" %>
<tiles:insertDefinition name="default-page">
    <tiles:putAttribute name="main">


        <table>
            <tr>
                <td width="20%">
                    <img src="${elo.thumbnail}"/>
                </td>
                <td>
                    <table>
                        <tr>
                            <td>
                                <strong>Name</strong>
                            </td>
                            <td>
                                ${elo.myname}
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <strong>Created by</strong>
                            </td>
                            <td>
                                ${createdBy}
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <strong>Date</strong>
                            </td>
                            <td>
                                ${lastModified}
                            </td>
                        </tr>
                    </table>

                </td>
            </tr>
        </table>

        <form action="/webapp/app/webeport/StoreEloReflections.html">
            <table>
                <tr>
                    <td>
                        <strong>Description</strong>
                    </td>
                    <td>
                        <textarea rows="3" cols="30" name="eloReflectionDescription"></textarea>
                    </td>
                </tr>
                <tr>
                    <td>
                        <strong>General learning goals</strong>
                    </td>
                    <td>
                        <div id="generalLearningGoals">
                            <table>
                                <c:choose>
                                    <c:when test="${fn:length(generalLearningGoals) > 0}">
                                        <c:forEach var="learningGoal" items="${generalLearningGoals}">
                                            <tr>
                                                <td width="5%">
                                                    <input type="checkbox" name="generalLearningGoals" value="${learningGoal.id}"/>
                                                </td>
                                                <td>
                                                    ${learningGoal.goal}
                                                </td>
                                            </tr>

                                        </c:forEach>
                                    </c:when>
                                </c:choose>

                            </table>
                        </div>

                    </td>
                </tr>
                <tr>
                    <td>
                        <strong>Specific learning gooals</strong>
                    </td>
                    <td>
                        <div id="generalLearningGoals">
                            <table>
                                <c:choose>
                                    <c:when test="${fn:length(specificLearningGoals) > 0}">
                                        <c:forEach var="learningGoal" items="${specificLearningGoals}">
                                            <tr>
                                                <td width="5%">
                                                    <input type="checkbox" name="specificLearningGoals" value="${learningGoal.id}"/>
                                                </td>
                                                <td>
                                                    ${learningGoal.goal}
                                                </td>
                                            </tr>

                                        </c:forEach>
                                    </c:when>
                                </c:choose>

                            </table>
                        </div>

                    </td>
                </tr>
                <tr>
                    <td>
                        <strong>Reflection on ELO</strong>
                    </td>
                    <td>
                        <textarea rows="3" cols="30" name="reflectionOnElo"></textarea>
                    </td>
                </tr>
                <tr>
                    <td>
                        <strong>Reflection on inquiry</strong>
                    </td>
                    <td>
                        Slider here
                    </td>
                </tr>
            </table>
            <input type="submit" value="save">
        </form>


    </tiles:putAttribute>
</tiles:insertDefinition>