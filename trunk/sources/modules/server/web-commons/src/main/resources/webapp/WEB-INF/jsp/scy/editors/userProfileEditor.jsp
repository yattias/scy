<%@ include file="common-taglibs.jsp" %>
<tiles:insertDefinition name="default-page">
    <tiles:putAttribute name="main">


        <h1>My Profile</h1>

        <div>
            <table>
                <tr  class="${oddEven.oddEven}">
                    <td width="20%">
                        First name
                    </td>
                    <td>
                        <s:ajaxTextFieldForUsers model="${userDetails}" property="firstName"/>
                    </td>
                </tr>
                <tr  class="${oddEven.oddEven}">
                    <td width="20%">
                        Last name
                    </td>
                    <td>
                        <s:ajaxTextFieldForUsers model="${userDetails}" property="lastName"/>
                    </td>
                </tr>
                <tr  class="${oddEven.oddEven}">
                    <td width="20%">
                        Language
                    </td>
                    <td>
                        <s:ajaxTextFieldForUsers model="${userDetails}" property="locale"/>
                    </td>
                </tr>
                <tr  class="${oddEven.oddEven}">
                    <td>
                        Profile picture
                    </td>
                    <td>
                        <img src="/webapp/common/filestreamer.html?username=${userDetails.username}&showIcon"/>
                        
                        <form method="post" action="/webapp/components/profilePictureUploadController.html" enctype="multipart/form-data">
                            <input type="file" name="file"/>
                            <input type="submit"/>
                        </form>
                    </td>
                </tr>
            </table>







    </tiles:putAttribute>
</tiles:insertDefinition>