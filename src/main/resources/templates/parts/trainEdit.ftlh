<#if isAdmin>
    <a class="btn btn-primary" data-toggle="collapse" href="#collapseExample" role="button" aria-expanded="false"
       aria-controls="collapseExample">
        Add new Train
    </a>
</#if>

<div class="collapse <#if train??>show</#if>" id="collapseExample">
    <div class="form-group mt-3">
        <form method="post">
            <div class="form-group">
                <input type="text" name="departurePoint" class="form-control ${(departurePointError??)?string('is-invalid', '')}"
                       value="<#if train??>${train.departurePoint}</#if>" placeholder="departure point"/>
                <#if departurePointError??>
                    <div class="invalid-feedback">
                        ${departurePointError}
                    </div>
                </#if>
            </div>
            <div class="form-group">
                <input type="text" name="arrivalPoint" class="form-control ${(arrivalPointError??)?string('is-invalid', '')}"
                       value="<#if train??>${train.arrivalPoint}</#if>" placeholder="arrival point"/>
                <#if arrivalPointError??>
                    <div class="invalid-feedback">
                        ${arrivalPointError}
                    </div>
                </#if>
            </div>
            <div class="form-group">
                <input type="text" name="departureTime" class="form-control ${(departureTimeError??)?string('is-invalid', '')}"
                       value="<#if train??>${train.departureTime}</#if>" placeholder="departure time"/>
                <#if departureTimeError??>
                    <div class="invalid-feedback">
                        ${departureTimeError}
                    </div>
                </#if>
            </div>
            <div class="form-group">
                <input type="text" name="arrivalTime" class="form-control ${(arrivalTimeError??)?string('is-invalid', '')}"
                       value="<#if train??>${train.arrivalTime}</#if>" placeholder="arrival time"/>
                <#if arrivalTimeError??>
                    <div class="invalid-feedback">
                        ${arrivalTimeError}
                    </div>
                </#if>
            </div>
            <div class="form-group">
                <input type="text" name="date" class="form-control ${(dateError??)?string('is-invalid', '')}"
                       value="<#if train??>${train.date}</#if>" placeholder="date"/>
                <#if dateError??>
                    <div class="invalid-feedback">
                        ${dateError}
                    </div>
                </#if>
            </div>
            <div class="form-group">
                <input type="number" name="price" class="form-control ${(priceError??)?string('is-invalid', '')}"
                       placeholder="price of ticket"/>
                <#if priceError??>
                    <div class="invalid-feedback">
                        ${priceError}
                    </div>
                </#if>
            </div>
            <div class="form-group">
                <input type="number" name="freePlaces" class="form-control ${(freePlacesError??)?string('is-invalid', '')}"
                       placeholder="number of places"/>
                <#if freePlacesError??>
                    <div class="invalid-feedback">
                        ${freePlacesError}
                    </div>
                </#if>
            </div>
            <input type="hidden" name="_csrf" value="${_csrf.token}"/>
            <div class="form-group">
                <button type="submit" class="btn btn-primary">Add</button>
            </div>
        </form>
    </div>
</div>