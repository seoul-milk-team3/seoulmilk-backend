package com.seoulmilk.be.taxvalidation.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.seoulmilk.be.auth.service.AuthService;
import com.seoulmilk.be.tax.domain.NtsTax;
import com.seoulmilk.be.tax.exception.NtsTaxNotFoundException;
import com.seoulmilk.be.tax.persistence.NtsTaxRepository;
import com.seoulmilk.be.taxvalidation.dto.response.InvoiceVerificationResponse;
import com.seoulmilk.be.taxvalidation.exception.TaxValidationException;
import com.seoulmilk.be.taxvalidation.infrastructure.auth.EasyCodefProvider;
import com.seoulmilk.be.taxvalidation.infrastructure.request.EasyCodefRequestFactory;
import com.seoulmilk.be.user.domain.User;
import io.codef.api.EasyCodef;
import io.codef.api.EasyCodefServiceType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import static com.seoulmilk.be.tax.exception.errorcode.NtsTaxErrorCode.NTS_TAX_NOT_FOUND;
import static com.seoulmilk.be.taxvalidation.exception.errorcode.TaxValidationErrorCode.*;
import static com.seoulmilk.be.taxvalidation.infrastructure.constants.CodefParameter.*;
import static com.seoulmilk.be.taxvalidation.infrastructure.constants.CodefResponseCode.SIMPLE_AUTHENTICATION_RESPONSE;

@Service
@RequiredArgsConstructor
public class TaxValidationService {
    @Value("${api.codef.url}")
    private String productUrl;

    private final AuthService authService;
    private final NtsTaxRepository ntsTaxRepository;
    private final EasyCodefProvider easyCodefProvider;
    private final EasyCodefRequestFactory easyCodefRequestFactory;
    private final CodefApiCacheService codefApiCacheService;

    public void validateInvoicePreVerified(Long taxId) {
        User user = authService.getLoginUser();
        NtsTax ntsTax = getNtsTaxById(taxId);

        String response = requestCodefApi(user, ntsTax);
        Map<String, Object> responseMap = parseResponse(response);

        validateResponse(responseMap);

        if (isSimpleAuthenticationResponse(responseMap)) {
            codefApiCacheService.saveCodefResponse(user.getCodefId(), (Map<String, Object>) responseMap.get("data"));
        }
    }

    public InvoiceVerificationResponse validateInvoicePostVerified(Long taxId) {
        User user = authService.getLoginUser();
        NtsTax ntsTax = getNtsTaxById(taxId);

        String response = requestCodefApiWith2Way(user, ntsTax);

        Map<String, Object> responseMap = parseResponse(response);

        if (!(responseMap.get("data") instanceof Map)) {
            throw new TaxValidationException(INVALID_RESPONSE_FORMAT);
        }
        Map<String, Object> dataMap = (Map<String, Object>) responseMap.get("data");
        return new InvoiceVerificationResponse(dataMap.get("resAuthenticity").toString());
    }

    private NtsTax getNtsTaxById(Long taxId) {
        return ntsTaxRepository.findById(taxId)
                .orElseThrow(() -> new NtsTaxNotFoundException(NTS_TAX_NOT_FOUND));
    }

    private String requestCodefApi(User user, NtsTax ntsTax) {
        EasyCodef codef = easyCodefProvider.getEasyCodef();

        try {
            return codef.requestProduct(productUrl, EasyCodefServiceType.DEMO,
                    easyCodefRequestFactory.createValidationRequest(user, ntsTax));
        } catch (UnsupportedEncodingException e) {
            throw new TaxValidationException(UNSUPPORTED_ENCODING_ERROR);
        } catch (JsonProcessingException e) {
            throw new TaxValidationException(JSON_PROCESSING_ERROR);
        } catch (InterruptedException e) {
            throw new TaxValidationException(INTERRUPTED_ERROR);
        }
    }

    private String requestCodefApiWith2Way(User user, NtsTax ntsTax) {
        EasyCodef codef = easyCodefProvider.getEasyCodef();

        try {
            HashMap<String, Object> body = easyCodefRequestFactory.createValidationRequest(user, ntsTax);
            body.putAll(Map.of("simpleAuth", "1", IS_2_WAY.getParamName(), true));
            body.put(TWO_WAY_INFO.getParamName(), codefApiCacheService.getTwoWayInfo(user.getCodefId()));
            codefApiCacheService.removeTwoWayInfo(user.getCodefId());
            return codef.requestCertification(productUrl, EasyCodefServiceType.DEMO, body);
        } catch (UnsupportedEncodingException e) {
            throw new TaxValidationException(UNSUPPORTED_ENCODING_ERROR);
        } catch (JsonProcessingException e) {
            throw new TaxValidationException(JSON_PROCESSING_ERROR);
        } catch (InterruptedException e) {
            throw new TaxValidationException(INTERRUPTED_ERROR);
        }
    }

    private Map<String, Object> parseResponse(String response) {
        try {
            return new ObjectMapper().readValue(response, Map.class);
        } catch (JsonProcessingException e) {
            throw new TaxValidationException(JSON_PROCESSING_ERROR);
        }
    }

    private void validateResponse(Map<String, Object> responseMap) {
        if (!(responseMap.get("result") instanceof Map) || !(responseMap.get("data") instanceof Map)) {
            throw new TaxValidationException(INVALID_RESPONSE_FORMAT);
        }
        Map<String, Object> dataMap = (Map<String, Object>) responseMap.get("data");
        if (!dataMap.containsKey("continue2Way")) {
            throw new TaxValidationException(CODEF_API_ERROR);
        }
    }

    private boolean isSimpleAuthenticationResponse(Map<String, Object> responseMap) {
        Map<String, Object> resultMap = (Map<String, Object>) responseMap.get("result");
        return SIMPLE_AUTHENTICATION_RESPONSE.isEqual(resultMap.get(CODE.getParamName()).toString());
    }


    @Transactional
    public void updateNtsTaxIsNormal(Long taxId, String isNormal) {
        NtsTax ntsTax = getNtsTaxById(taxId);
        ntsTax.updateIsNormal(isNormal);
    }
}

