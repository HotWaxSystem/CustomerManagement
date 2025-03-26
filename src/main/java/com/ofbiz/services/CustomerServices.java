package com.ofbiz.services;

import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.service.DispatchContext;
import org.apache.ofbiz.service.ServiceUtil;

import java.util.List;
import java.util.Map;

public class CustomerServices {
    private static final String MODULE = CustomerServices.class.getName();

    public static Map<String, Object> findAllCustomers(DispatchContext dctx, Map<String, ? extends Object> context) {
        Delegator delegator = dctx.getDelegator();
        
        try {
            // Query all customer records from the view entity
            List<GenericValue> customers = delegator.findList("FindCustomerView", null, null, null, null, false);

            // Return the result
            Map<String, Object> result = ServiceUtil.returnSuccess();
            result.put("customers", customers);
            return result;
        } catch (GenericEntityException e) {
            Debug.logError(e, "Error retrieving customer records from FindCustomerView", MODULE);
            return ServiceUtil.returnError("Error retrieving customer records.");
        }
    }
}
