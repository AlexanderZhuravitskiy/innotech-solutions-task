<#import "parts/common.ftlh" as c>

<@c.page>
    <p class="fs-3">Your balance: ${user.balance} BYN</p>
    <form action="/users/balance/add" method="post">
        <div class="form-group row mb-3">
            <label class="col-sm-2 col-form-label">Replenishment amount :</label>
            <div class="col-sm-6">
                <input type="number" name="replenishmentAmount" class="form-control" placeholder="Enter replenishment amount" />
            </div>
        </div>
        <input type="hidden" name="_csrf" value="${_csrf.token}" />
        <button class="btn btn-primary" type="submit">Top up</button>
    </form>
</@c.page>