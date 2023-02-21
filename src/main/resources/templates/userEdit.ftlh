<#import "parts/common.ftlh" as c>

<@c.page>
    User editor

    <form action="/users/${user.id}/update" method="post">
        <input type="text" name="username" value="${user.username}">
        <div  class="form-check">
            <input class="form-check-input" type="checkbox" id="inlineCheckbox" name="checkbox">
            <label class="form-check-label" for="inlineCheckbox">Сделать администратором</label>
        </div>
        <input type="hidden" value="${_csrf.token}" name="_csrf">
        <button type="submit">Save</button>
    </form>
</@c.page>