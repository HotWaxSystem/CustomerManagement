<table class="table table-bordered table-striped table-hover">
    <thead>
        <tr>
            <th>${uiLabelMap.partyId}</th>
            <th>${uiLabelMap.roleTypeId}</th>
            <th>${uiLabelMap.firstName}</th>
            <th>${uiLabelMap.lastName}</th>
            <th>${uiLabelMap.gender}</th>
            <th>${uiLabelMap.birthDate}</th>
            <th>${uiLabelMap.email}</th>
            <th>${uiLabelMap.address1}</th>
            <th>${uiLabelMap.address2}</th>
            <th>${uiLabelMap.city}</th>
            <th>${uiLabelMap.postalCode}</th>
            <th>${uiLabelMap.stateProvinceGeoId}</th>
            <th>${uiLabelMap.contactNumber}</th>
            <th>${uiLabelMap.cardNumber}</th>
        </tr>
    </thead>

    <#if customerList?has_content>
        <tbody>
            <#list customerList as result>
                <tr>
                    <td>${result.get("partyId")!""}</td>
                    <td>${result.get("role")!""}</td>
                    <td>${result.get("firstName")!""}</td>
                    <td>${result.get("lastName")!""}</td>
                    <td>${result.get("gender")!""}</td>
                    <td>${result.get("birthDate")!""}</td>
                    <td>${result.get("email")!""}</td>
                    <td>${result.get("address1")!""}</td>
                    <td>${result.get("address2")!""}</td>
                    <td>${result.get("city")!""}</td>
                    <td>${result.get("postalCode")!""}</td>
                    <td>${result.get("stateProvinceGeoId")!""}</td>
                    <td>${result.get("contactNumber")!""}</td>
                    <td>${result.get("cardNumber")!""}</td>
                </tr>
            </#list>
        </tbody>
    <#else>
        <tbody>
            <tr><td colspan="15">No Customers Found</td></tr>
        </tbody>
    </#if>
</table>
