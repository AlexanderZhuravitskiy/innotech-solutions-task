<#include "parts/security.ftlh">
<#import "parts/common.ftlh" as c>
<#import "parts/pager.ftlh" as p>

<@c.page>
    <div class="mt-3">
        <#list trains as train>
            <div class="card mb-2">
                <div class="card-body">
                    <div>
                        <i>${train.departurePoint} → ${train.arrivalPoint}</i>
                        <br>
                        <i>Отправление: ${train.departureTime} ${train.date}</i>
                        <br>
                        <i>Прибытие: ${train.arrivalTime} ${train.date}</i>
                        <br>
                        <form action="/users/tickets/delete" method="post">
                            <input type="hidden" value="${train.id}" name="trainId">
                            <input type="hidden" name="_csrf" value="${_csrf.token}"/>
                            <button class="btn btn-primary mt-1" type="submit">Return</button>
                        </form>
                    </div>
                </div>
            </div>
        <#else>
            No tickets
        </#list>
    </div>
</@c.page>