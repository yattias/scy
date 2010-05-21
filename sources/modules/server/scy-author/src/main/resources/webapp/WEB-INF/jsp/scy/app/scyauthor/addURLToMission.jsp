
<h1>${model.name}</h1>
<form action="addURLToMission.html" method="post">
    <table>
        <tr>
            <td>URL</td>
            <td><input type="text" id="url" name="url"></td>
        </tr>
        <input type="hidden" name="model" value="${model.id}"/>
        <input type="hidden" name="action" value="addNewUrl"/> 
    </table>
</form>