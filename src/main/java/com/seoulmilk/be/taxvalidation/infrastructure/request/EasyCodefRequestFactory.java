package com.seoulmilk.be.taxvalidation.infrastructure.request;

import com.seoulmilk.be.tax.domain.NtsTax;
import com.seoulmilk.be.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static com.seoulmilk.be.taxvalidation.infrastructure.constants.CodefParameter.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class EasyCodefRequestFactory {
    private static final String ORGANIZATION_CODE = "0004";
    private static final String LOGIN_TYPE_CODE = "5";
    private static final String BAR = "-";


    public HashMap<String, Object> createValidationRequest(User user, NtsTax ntsTax, String loginTypeLevel) {
        HashMap<String, Object> request = new HashMap<>(createDefaultRequest(loginTypeLevel));
        request.putAll(createUserRequest(user));
        request.putAll(createNtsTaxRequest(ntsTax));
        return request;
    }

    private HashMap<String, Object> createDefaultRequest(String loginTypeLevel) {
        HashMap<String, Object> request = new HashMap<>();
        request.put(ORGANIZATION.getParamName(), ORGANIZATION_CODE);
        request.put(LOGIN_TYPE.getParamName(), LOGIN_TYPE_CODE);
        request.put(LOGIN_TYPE_LEVEL.getParamName(), loginTypeLevel);
        return request;
    }

    private HashMap<String, Object> createUserRequest(User user) {
        HashMap<String, Object> request = new HashMap<>();
        request.put(ID.getParamName(), user.getCodefId());
        request.put(USER_NAME.getParamName(), user.getName());
        request.put(PHONE_NO.getParamName(), user.getPhoneNo());
        request.put(IDENTITY.getParamName(), user.getBirthday());
        request.put(TELECOM.getParamName(), user.getTelecom().getNum());
        return request;
    }

    private HashMap<String, Object> createNtsTaxRequest(NtsTax ntsTax) {
        HashMap<String, Object> request = new HashMap<>();
        request.put(SUPPLIER_REG_NUMBER.getParamName(), getRemovedBarStr(ntsTax.getSuId()));
        request.put(CONTRACTOR_REG_NUMBER.getParamName(), getRemovedBarStr(ntsTax.getIpId()));
        request.put(APPROVAL_NO.getParamName(), getRemovedBarStr(ntsTax.getIssueId()));
        request.put(REPORTING_DATE.getParamName(), ntsTax.getIssueDate());
        request.put(SUPPLY_VALUE.getParamName(), ntsTax.getChargeTotal().toString());
        return request;
    }

    private String getRemovedBarStr(String barStr) {
        return barStr.replace(BAR, "");
    }
}
