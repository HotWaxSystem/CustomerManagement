package com.ofbiz.services;

import java.util.Map;
import java.util.HashMap;
import java.util.Locale;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.*;

import org.apache.ofbiz.service.DispatchContext;
import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.service.LocalDispatcher;
import org.apache.ofbiz.base.util.UtilMisc;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.service.ServiceUtil;
import org.apache.ofbiz.base.util.UtilDateTime;
import org.apache.ofbiz.entity.condition.EntityCondition;
import org.apache.ofbiz.base.util.UtilValidate;
import org.apache.ofbiz.entity.condition.EntityOperator;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.util.EntityQuery;

public class CreateCustomer {
    private static final String MODULE = CreateCustomer.class.getName();

    public static Map<String, Object> createCustomer(DispatchContext dctx, Map<String, ? extends Object> context) {
        Map<String, Object> result = new HashMap<>();
        Delegator delegator = dctx.getDelegator();
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Locale locale = (Locale) context.get("locale");
    
        // Get input parameters
        String firstName = (String) context.get("firstName");
        String lastName = (String) context.get("lastName");
        String gender = (String) context.get("gender");
        String emailAddress = (String) context.get("emailAddress");
        String dob = (String) context.get("dob");
        String address1 = (String) context.get("address1");
        String address2 = (String) context.get("address2");
        String city = (String) context.get("city");
        String postalCode = (String) context.get("postalCode");
        String stateProvinceGeoId = (String) context.get("stateProvinceGeoId");
        String contactNumber = (String) context.get("contactNumber");
        String creditCardNumber = (String) context.get("creditCardNumber"); // Added Credit Card Input
    
        try {
            String partyId = delegator.getNextSeqId("Party");
            GenericValue party = delegator.makeValue("Party", UtilMisc.toMap("partyId", partyId, "partyTypeId", "PERSON", "statusId", "PARTY_ENABLED"));
            delegator.create(party);
    
            GenericValue partyRole = delegator.makeValue("PartyRole", UtilMisc.toMap("partyId", partyId, "roleTypeId", "CUSTOMER"));
            delegator.create(partyRole);
    
            GenericValue person = delegator.makeValue("Person", UtilMisc.toMap("partyId", partyId, "firstName", firstName, "lastName", lastName, "gender", gender));
            delegator.create(person);
    
            String contactMechId1 = delegator.getNextSeqId("ContactMech");
            GenericValue contactMech1 = delegator.makeValue("ContactMech", UtilMisc.toMap("contactMechId", contactMechId1, "contactMechTypeId", "EMAIL_ADDRESS", "infoString", emailAddress));
            delegator.create(contactMech1);
    
            GenericValue partyContactMech1 = delegator.makeValue("PartyContactMech", UtilMisc.toMap("partyId", partyId, "contactMechId", contactMechId1, "fromDate", UtilDateTime.nowTimestamp()));
            delegator.create(partyContactMech1);
    
            String contactMechId2 = delegator.getNextSeqId("ContactMech");
            GenericValue contactMech2 = delegator.makeValue("ContactMech", UtilMisc.toMap("contactMechId", contactMechId2, "contactMechTypeId", "POSTAL_ADDRESS"));
            delegator.create(contactMech2);
    
            GenericValue postalAddress = delegator.makeValue("PostalAddress", UtilMisc.toMap("contactMechId", contactMechId2, "address1", address1, "address2", address2, "city", city, "postalCode", postalCode, "stateProvinceGeoId", stateProvinceGeoId));
            delegator.create(postalAddress);
    
            GenericValue partyContactMech2 = delegator.makeValue("PartyContactMech", UtilMisc.toMap("partyId", partyId, "contactMechId", contactMechId2, "fromDate", UtilDateTime.nowTimestamp()));
            delegator.create(partyContactMech2);
    
            String contactMechId3 = delegator.getNextSeqId("ContactMech");
            GenericValue contactMech3 = delegator.makeValue("ContactMech", UtilMisc.toMap("contactMechId", contactMechId3, "contactMechTypeId", "TELECOM_NUMBER"));
            delegator.create(contactMech3);
    
            GenericValue telecomNumber = delegator.makeValue("TelecomNumber", UtilMisc.toMap("contactMechId", contactMechId3, "contactNumber", contactNumber));
            delegator.create(telecomNumber);
    
            GenericValue partyContactMech3 = delegator.makeValue("PartyContactMech", UtilMisc.toMap("partyId", partyId, "contactMechId", contactMechId3, "fromDate", UtilDateTime.nowTimestamp()));
            delegator.create(partyContactMech3);
    
            String paymentMethodId = delegator.getNextSeqId("PaymentMethod");
            GenericValue paymentMethod = delegator.makeValue("PaymentMethod", UtilMisc.toMap(
                    "paymentMethodId", paymentMethodId,
                    "partyId", partyId,
                    "paymentMethodTypeId", "CREDIT_CARD"
            ));
            delegator.create(paymentMethod);
    
            if (UtilValidate.isNotEmpty(creditCardNumber)) {
                GenericValue creditCard = delegator.makeValue("CreditCard", UtilMisc.toMap(
                        "paymentMethodId", paymentMethodId,
                        "cardNumber", creditCardNumber
                ));
                delegator.create(creditCard);
            }
    
            result.put("partyId", partyId);
            return result;
        } catch (GenericEntityException e) {
            return ServiceUtil.returnError("Error creating customer: " + e.getMessage());
        }
    }
    
}
