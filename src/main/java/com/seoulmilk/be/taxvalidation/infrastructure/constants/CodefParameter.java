package com.seoulmilk.be.taxvalidation.infrastructure.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CodefParameter {
    ORGANIZATION("organization"),
    ID("id"),
    LOGIN_TYPE("loginType"),
    LOGIN_TYPE_LEVEL("loginTypeLevel"),
    USER_NAME("userName"),
    PHONE_NO("phoneNo"),
    IDENTITY("identity"),
    TELECOM("telecom"),
    SUPPLIER_REG_NUMBER("supplierRegNumber"),
    CONTRACTOR_REG_NUMBER("contractorRegNumber"),
    APPROVAL_NO("approvalNo"),
    REPORTING_DATE("reportingDate"),
    SUPPLY_VALUE("supplyValue"),
    TWO_WAY_INFO("twoWayInfo"),
    CODE("code"),
    JOB_INDEX("jobIndex"),
    THREAD_INDEX("threadIndex"),
    JTI("jti"),
    TWO_WAY_TIMESTAMP("twoWayTimestamp"),
    SIMPLE_AUTH("simpleAuth"),
    IS_2_WAY("is2Way"),
    ;

    private final String paramName;
}
