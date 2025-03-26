package com.ofbiz.services;
import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.stream.Collectors;

import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.service.DispatchContext;
import org.apache.ofbiz.service.ServiceUtil;
import org.apache.ofbiz.entity.condition.EntityCondition;
import org.apache.ofbiz.entity.condition.EntityOperator;
import org.apache.ofbiz.service.ModelService;

public class FindCustomer {

    private static final String MODULE = FindCustomer.class.getName();

    public static Map<String, Object> findCustomer(DispatchContext dctx, Map<String, Object> context) {
        String emailAddress = (String) context.get("emailAddress");
        String firstName = (String) context.get("firstName");
        String lastName = (String) context.get("lastName");
        String city = (String) context.get("city");
    
        Debug.logInfo("Searching for customer with email: " + emailAddress + ", firstName: " + firstName + ", lastName: " + lastName, MODULE);
    
        Map<String, Object> result = new HashMap<>();
    
        try {
            Delegator delegator = dctx.getDelegator();
            List<EntityCondition> conditions = new ArrayList<>();
    
            if (emailAddress != null && !emailAddress.isEmpty()) {
                conditions.add(EntityCondition.makeCondition("emailAddress", EntityOperator.LIKE, "%" + emailAddress + "%"));
            }
            if (firstName != null && !firstName.isEmpty()) {
                conditions.add(EntityCondition.makeCondition("firstName", EntityOperator.LIKE, "%" + firstName + "%"));
            }
            if (lastName != null && !lastName.isEmpty()) {
                conditions.add(EntityCondition.makeCondition("lastName", EntityOperator.LIKE, "%" + lastName + "%"));
            }
            if (city != null && !city.isEmpty()) {
                conditions.add(EntityCondition.makeCondition("city", EntityOperator.LIKE, "%" + city + "%"));
            }
    
            EntityCondition combinedCondition = EntityCondition.makeCondition(conditions, EntityOperator.AND);
            List<GenericValue> customers = delegator.findList("FindCustomerView", combinedCondition, null, null, null, false);
    
            if (customers != null && !customers.isEmpty()) {
                // Remove duplicates based on partyId
                List<GenericValue> uniqueCustomers = customers.stream()
                        .collect(Collectors.toMap(c -> c.getString("partyId"), c -> c, (c1, c2) -> c1))
                        .values()
                        .stream()
                        .collect(Collectors.toList());
    
                Debug.logInfo("Filtered customer list size: " + uniqueCustomers.size(), MODULE);
                result.put("customerList", uniqueCustomers);
            } else {
                result.put("customerList", new ArrayList<>());
            }
    
        } catch (GenericEntityException e) {
            Debug.logError(e, "Error occurred while searching for customers", MODULE);
            result.put(ModelService.ERROR_MESSAGE, "Error occurred while searching for customers: " + e.getMessage());
            result.put("customerList", new ArrayList<>());
            return ServiceUtil.returnError(e.getMessage());
        }
    
        return result;
    }
}
