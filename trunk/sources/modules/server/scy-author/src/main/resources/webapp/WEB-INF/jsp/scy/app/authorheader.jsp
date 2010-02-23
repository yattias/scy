<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "XHTML1-s.dtd" />
<html xml:lang="en" lang="en">
<head>

    <%@ include file="../authorinclude.jsp" %>

</head>
<body>

<div id="doc3" class="yui-t7" style="">
    <div style="height:1em"></div>
    <div id="hd" class="border top header">
        <div class="logo">
            <div class="title">
                SCY - Science Created by YOU
            </div>
        </div>
        <div class="menubar">
            <div class="topmenu" style="margin: 4px 1em 2px  1em;">
                <sec:authorize ifAllGranted="ROLE_USER">
                    <div id="usernameBannerHome"><sec:authentication property="principal.username"/></div>
                    <div id="signOutBannerHome"><a id="styleOverRideSafari1" href="<c:url value="/j_spring_security_logout"/>">
                        <spring:message code="log.out"/></a></div>
                    <sec:authorize ifAllGranted="ROLE_TEACHER">
                            <span id="signOutBannerHome">
                                <a href="../teacher/index.html"><spring:message code="header.teacher"/></a>
                            </span>
                    </sec:authorize>
                    <sec:authorize ifAllGranted="ROLE_TEACHER">
                            <span id="signOutBannerHome">
                                <a href="../teacher/studentList.html">Student list</a>
                            </span>
                    </sec:authorize>
                    <sec:authorize ifAllGranted="ROLE_ADMINISTRATOR">
                            <span id="signOutBannerHome">
                                <a href="/admin/index.html"><spring:message code="header.admin"/></a>
                            </span>
                    </sec:authorize>
                    <sec:authorize ifAllGranted="ROLE_ADMINISTRATOR">
                            <span id="signOutBannerHome">
                                <a href="scenarios.html">Scenarios</a>
                            </span>
                    </sec:authorize>
                    <sec:authorize ifAllGranted="ROLE_ADMINISTRATOR">
                            <span id="signOugBannerHome">
                                <a href="/webapp/app/useradmin/UserAdminIndex.html">Manage users </a>
                            </span>
                    </sec:authorize>

                    <sec:authorize ifAllGranted="ROLE_TEACHER">
                            <span id="signOugBannerHome">
                                <a href="/webapp/app/scyauthor/ScyAuthorIndex.html">SCYAuthor </a>
                            </span>
                    </sec:authorize>

                    <sec:authorize ifAllGranted="ROLE_USER">
                            <span id="signOugBannerHome">
                                <a href="/webapp/student/profile.html">Profile </a>
                            </span>
                    </sec:authorize>

                </sec:authorize>
            </div>
        </div>
    </div>
    <div id="bd" class="border bottom">
    </div>
</div>
<div id="centeredDiv">

