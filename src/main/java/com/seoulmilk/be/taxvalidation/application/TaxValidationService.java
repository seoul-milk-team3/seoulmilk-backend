package com.seoulmilk.be.taxvalidation.application;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.seoulmilk.be.auth.service.AuthService;
import com.seoulmilk.be.tax.domain.NtsTax;
import com.seoulmilk.be.tax.exception.NtsTaxNotFoundException;
import com.seoulmilk.be.tax.persistence.NtsTaxRepository;
import com.seoulmilk.be.taxvalidation.exception.TaxValidationException;
import com.seoulmilk.be.taxvalidation.infrastructure.auth.EasyCodefProvider;
import com.seoulmilk.be.taxvalidation.infrastructure.request.EasyCodefRequestFactory;
import com.seoulmilk.be.user.domain.User;
import io.codef.api.EasyCodef;
import io.codef.api.EasyCodefServiceType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import static com.seoulmilk.be.tax.exception.errorcode.NtsTaxErrorCode.NTS_TAX_NOT_FOUND;
import static com.seoulmilk.be.taxvalidation.exception.errorcode.TaxValidationErrorCode.*;
import static com.seoulmilk.be.taxvalidation.infrastructure.constants.CodefParameter.CODE;
import static com.seoulmilk.be.taxvalidation.infrastructure.constants.CodefResponseCode.SIMPLE_AUTHENTICATION_RESPONSE;

@Slf4j
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

    public void validateTaxInvoice(Long taxId) {
        User user = authService.getLoginUser();
        NtsTax ntsTax = getNtsTaxById(taxId);

        String response = requestCodefApi(user, ntsTax);
        log.info("response : {}", response);
        Map<String, Object> responseMap = parseResponse(response);

        validateResponse(responseMap);

        if (isSimpleAuthenticationResponse(responseMap)) {
            codefApiCacheService.saveCodefResponse(user.getCodefId(), (Map<String, Object>) responseMap.get("data"));
        }
    }

    private NtsTax getNtsTaxById(Long taxId) {
        return ntsTaxRepository.findById(taxId)
                .orElseThrow(() -> new NtsTaxNotFoundException(NTS_TAX_NOT_FOUND));
    }

    private String requestCodefApi(User user, NtsTax ntsTax) {
        EasyCodef codef = easyCodefProvider.getEasyCodef();

        try {
            log.info("request codef api");
            return codef.requestProduct(productUrl, EasyCodefServiceType.DEMO,
                    easyCodefRequestFactory.createValidationRequest(user, ntsTax));
        } catch (UnsupportedEncodingException e) {
            log.error("UnsupportedEncodingException");
            throw new TaxValidationException(UNSUPPORTED_ENCODING_ERROR);
        } catch (JsonProcessingException e) {
            log.info("JsonProcessingException");
            throw new TaxValidationException(JSON_PROCESSING_ERROR);
        } catch (InterruptedException e) {
            log.info("InterruptedException");
            throw new TaxValidationException(INTERRUPTED_ERROR);
        }
    }

    private Map<String, Object> parseResponse(String response) {
        try {
            return new ObjectMapper().readValue(response, HashMap.class);
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
}

