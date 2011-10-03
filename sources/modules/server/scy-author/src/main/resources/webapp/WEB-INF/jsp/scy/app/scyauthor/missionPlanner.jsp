<%@ include file="common-taglibs.jsp" %>
<div id="missionPlanner">

<p>
    <spring:message code="TEACHER_INTRO_TO_PEDAGOGICAL_PLAN_TAB"/>
</p>
<br/>
<p>
    <spring:message code="TEACHER_INTRO_TO_PEDAGOGICAL_PLAN_TAB_2"/>
</p>

<c:choose>
    <c:when test="${fn:length(anchorEloWrappers) > 0}">
        <table>
            <tr>
                <th>
                    <spring:message code="ANCHOR_ELO"/>
                </th>
                <th>
                    <spring:message code="LAS"/>
                </th>
                <th>
                    <spring:message code="OBLIGATORY_IN_PORTFOLIO"/>
                </th>
                <!--th>
                    <spring:message code="OWN_COMMENTS"/>
                </th-->
            </tr>

            <c:forEach var="anchorEloWrapper" items="${anchorEloWrappers}">
                <tr class="${oddEven.oddEven}">
                    <td>
                        ${anchorEloWrapper.name} 
                    </td>
                    <td>
                        ${anchorEloWrapper.lasName}
                    </td>
                    <td>
                        <center>
                            <a href="javascript:openPage(document.getElementById('missionPlanner').parentNode.id, 'missionPlanner.html?eloURI=' + encodeURIComponent('${eloURI}') + '&action=setAnchorEloObligatory&anchorEloUri=' + encodeURIComponent('${anchorEloWrapper.encodedUri}'));">
                                <c:if test="${anchorEloWrapper.obligatoryInPortfolio == true}">
                                    <img src="/webapp/themes/scy/default/images/checkbox_checked.png" alt=""  />
                                </c:if>
                                <c:if test="${anchorEloWrapper.obligatoryInPortfolio == false}">
                                    <img src="/webapp/themes/scy/default/images/checkbox_unchecked.png" alt=""  />
                                </c:if>
                            </a>
                        </center>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </c:when>
</c:choose>
</div>
<sec:authorize ifAllGranted="ROLE_AUTHOR">
    <a href="missionPlanner.html?eloURI=${eloURI}&action=reinitializePedagogicalPlan">Reinitialize plan</a>
</sec:authorize>



