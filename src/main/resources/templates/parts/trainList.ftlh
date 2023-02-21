<#include "security.ftlh">
<#import "pager.ftlh" as p>

<@p.pager url page />

<div class="mt-3">
    <#list page.content as train>
        <div class="card mb-2">
            <div class="card-header">
                <b>${train.price} BYN</b>
            </div>
            <div class="card-body">
                <div>
                    <i>${train.departurePoint} → ${train.arrivalPoint}</i>
                    <br>
                    <i>Отправление: ${train.departureTime} ${train.date}</i>
                    <br>
                    <i>Прибытие: ${train.arrivalTime} ${train.date}</i>
                    <br>
                    <#if train.freePlaces gt 0>
                        <i>Осталось мест: ${train.freePlaces}</i>
                        <br>
                        <#if user?? && !isAdmin>
                        <form action="/users/tickets/${train.id}/add" method="post">
                            <input type="hidden" name="_csrf" value="${_csrf.token}"/>
                            <button class="btn btn-primary mt-1" type="submit">Buy</button>
                        </form>
                        </#if>
                    <#else>
                        <i>Мест нет</i>
                    </#if>
                </div>
            </div>
        </div>
    <#else>
        No trains
    </#list>
</div>

<@p.pager url page />