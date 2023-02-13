<#include "parts/security.ftlh">
<#import "parts/common.ftlh" as c>

<@c.page>
    <form method="get" class="form-inline mb-3">
        <div class="form-row">
            <input type="text" name="departurePointSearch" class="form-control mr-2" value="${departurePointSearch!}"
                   placeholder="departure point">
            <input type="text" name="arrivalPointSearch" class="form-control mr-2" value="${arrivalPointSearch!}"
                   placeholder="arrival point">
            <input type="text" name="dateSearch" class="form-control mr-2" value="${dateSearch!}" placeholder="dateSearch">
            <button type="submit" class="btn btn-primary">Search</button>
        </div>
    </form>

    <#include "parts/trainEdit.ftlh" />

    <#include "parts/trainList.ftlh" />

</@c.page>