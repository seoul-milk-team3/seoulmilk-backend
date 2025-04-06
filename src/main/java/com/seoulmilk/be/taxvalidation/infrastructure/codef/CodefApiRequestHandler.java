package com.seoulmilk.be.taxvalidation.infrastructure.codef;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.seoulmilk.be.tax.domain.NtsTax;
import com.seoulmilk.be.tax.persistence.NtsTaxRepository;
import com.seoulmilk.be.taxvalidation.dto.request.CodefRequest;
import com.seoulmilk.be.taxvalidation.exception.TaxValidationException;
import io.codef.api.EasyCodefServiceType;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import static com.seoulmilk.be.taxvalidation.exception.errorcode.TaxValidationErrorCode.*;
import static com.seoulmilk.be.taxvalidation.infrastructure.constants.CodefParameter.*;

@Slf4j
public class CodefApiRequestHandler implements Runnable {
    private static final String ENTER_AUTHENTICATION_CODE = "CF-03002";

    private final EasyCodefRequestFactory easyCodefRequestFactory;
    private final CodefRequest codefRequest;
    private final int handlerId;
    private final String codefId;
    private final String productUrl;
    private final CodefCacheService codefCacheService;
    private final NtsTaxRepository ntsTaxRepository;

    @Builder
    public CodefApiRequestHandler(String productUrl, EasyCodefRequestFactory easyCodefRequestFactory, CodefRequest codefRequest, int handlerId, String codefId, CodefCacheService codefCacheService, NtsTaxRepository ntsTaxRepository) {
        this.codefId = codefId;
        this.productUrl = productUrl;
        this.handlerId = handlerId;
        this.easyCodefRequestFactory = easyCodefRequestFactory;
        this.codefRequest = codefRequest;
        this.codefCacheService = codefCacheService;
        this.ntsTaxRepository = ntsTaxRepository;
    }

    @Override
    public void run() {
        String response;
        HashMap<String, Object> responseMap;

        HashMap<String, Object> body = easyCodefRequestFactory.createValidationRequest(
                codefRequest.user(), codefRequest.ntsTax(), codefRequest.loginTypeLevel());
        try {
            response = codefRequest.easyCodef().requestProduct(productUrl, EasyCodefServiceType.DEMO, body);
        } catch (UnsupportedEncodingException | InterruptedException | JsonProcessingException e) {
            throw new TaxValidationException(CODEF_API_ERROR);
        }

        try {
            responseMap = new ObjectMapper().readValue(response, HashMap.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        HashMap<String, Object> resultMap = (HashMap<String, Object>) responseMap.get(RESULT.getParamName());
        String code = (String)resultMap.get(CODE.getParamName());
        HashMap<String, Object> dataMap = (HashMap<String, Object>)responseMap.get(DATA.getParamName());

        boolean isContinue2Way = false;
        if (dataMap.containsKey(CONTINUE_TWO_WAY.getParamName())) {
            isContinue2Way = (boolean) dataMap.get(CONTINUE_TWO_WAY.getParamName());
        }

        if (ENTER_AUTHENTICATION_CODE.equals(code) && isContinue2Way) {
            codefCacheService.saveCodefResponse(codefId, (Map<String, Object>) responseMap.get(DATA.getParamName()));
            codefCacheService.saveFirstTaxBody(codefId, createFirstRequest());
        }
        log.info("taxId: {}, response: {}", codefRequest.ntsTax().getId(), response);

        if (handlerId > 0) {
            analyzeResponse(response);
        }
    }

    private HashMap<String, Object> createFirstRequest() {
        return easyCodefRequestFactory.createValidationRequest(
                codefRequest.user(), codefRequest.ntsTax(), codefRequest.loginTypeLevel());
    }

    private void analyzeResponse(String response) {
        NtsTax ntsTax = codefRequest.ntsTax();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode;
        try {
            rootNode = objectMapper.readTree(response);
            log.info(rootNode.asText());
        } catch (JsonProcessingException e) {
            throw new TaxValidationException(JSON_PROCESSING_ERROR);
        }
        String resAuthenticity = rootNode.path(DATA.getParamName()).path(RES_AUTHENTICITY.getParamName()).asText();
        ntsTax.updateIsNormal(resAuthenticity);
        ntsTaxRepository.save(ntsTax);
    }
}
