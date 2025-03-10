package com.seoulmilk.be.taxvalidation.application.thread;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.seoulmilk.be.tax.application.NtsTaxService;
import com.seoulmilk.be.tax.domain.NtsTax;
import com.seoulmilk.be.taxvalidation.application.CodefCacheService;
import com.seoulmilk.be.taxvalidation.dto.request.CodefRequest;
import com.seoulmilk.be.taxvalidation.exception.TaxValidationException;
import com.seoulmilk.be.taxvalidation.infrastructure.request.EasyCodefRequestFactory;
import io.codef.api.EasyCodefServiceType;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import static com.seoulmilk.be.taxvalidation.exception.errorcode.TaxValidationErrorCode.CODEF_API_ERROR;
import static com.seoulmilk.be.taxvalidation.exception.errorcode.TaxValidationErrorCode.JSON_PROCESSING_ERROR;
import static com.seoulmilk.be.taxvalidation.infrastructure.constants.CodefParameter.*;

@Slf4j
public class CodefRequestThread extends Thread {

    private static final String RESULT = "result";
    private static final String DATA = "data";
    private static final String CONTINUE_TWO_WAY = "continue2Way";
    private static final String ENTER_AUTHENTICATION_CODE = "CF-03002";

    private final Object monitor = new Object();

    private final EasyCodefRequestFactory easyCodefRequestFactory;
    private final CodefRequest codefRequest;
    private final int threadNo;
    private final String codefId;
    private final String productUrl;
    private final CodefCacheService codefCacheService;
    private final NtsTaxService ntsTaxService;

    @Builder
    public CodefRequestThread(String productUrl, EasyCodefRequestFactory easyCodefRequestFactory, CodefRequest codefRequest, int threadNo, String codefId, CodefCacheService codefCacheService, NtsTaxService ntsTaxService) {
        this.codefId = codefId;
        this.productUrl = productUrl;
        this.threadNo = threadNo;
        this.easyCodefRequestFactory = easyCodefRequestFactory;
        this.codefRequest = codefRequest;
        this.codefCacheService = codefCacheService;
        this.ntsTaxService = ntsTaxService;
    }

    public Object getMonitor() {
        return monitor;
    }

    @Override
    public void run() {
        log.info("Thread is running!");
        CodefRequestThreadManager.addThread(codefId, this);
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

        HashMap<String, Object> resultMap = (HashMap<String, Object>) responseMap.get(RESULT);
        String code = (String)resultMap.get(CODE.getParamName());
        HashMap<String, Object> dataMap = (HashMap<String, Object>)responseMap.get(DATA);

        boolean isContinue2Way = false;
        if (dataMap.containsKey(CONTINUE_TWO_WAY)) {
            isContinue2Way = (boolean) dataMap.get(CONTINUE_TWO_WAY);
        }

        if (ENTER_AUTHENTICATION_CODE.equals(code) && isContinue2Way) {
            codefCacheService.saveCodefResponse(codefId, (Map<String, Object>) responseMap.get(DATA));
        }
        log.info("taxId: {}, response: {}", codefRequest.ntsTax().getId(), response);

        if (threadNo > 0) {
            analyzeResponse(response);
        }

        synchronized (monitor) {
            try {
                monitor.wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        if (threadNo < 1) {
            response = afterAuthenticatedRequest();
            log.info("after authenticated taxId: {}, response: {}", codefRequest.ntsTax().getId(), response);
            analyzeResponse(response);
        }

    }

    private String afterAuthenticatedRequest() {
        HashMap<String, Object> certificatedBody = easyCodefRequestFactory.createValidationRequest(
                codefRequest.user(), codefRequest.ntsTax(), codefRequest.loginTypeLevel());

//        certificatedBody.putAll(Map.of(SIMPLE_AUTH.getParamName(), NORMAL.getValue(), IS_2_WAY.getParamName(), true));
        certificatedBody.put(TWO_WAY_INFO.getParamName(), codefCacheService.getTwoWayInfo(codefId));


        HashMap<String, Object> requestBody = new HashMap<>(certificatedBody);
        requestBody.put(TWO_WAY_INFO.getParamName(), codefCacheService.getTwoWayInfo(codefId));  // 이걸 request map 에 넣기
//        requestBody.putAll(Map.of(SIMPLE_AUTH.getParamName(), NORMAL.getValue(), IS_2_WAY.getParamName(), true));

        try {
            return codefRequest.easyCodef().requestCertification(productUrl, EasyCodefServiceType.DEMO, requestBody);
        } catch (UnsupportedEncodingException | InterruptedException | JsonProcessingException e) {
            throw new TaxValidationException(CODEF_API_ERROR);
        }
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
        String resAuthenticity = rootNode.path(DATA).path(RES_AUTHENTICITY.getParamName()).asText();
        ntsTax.updateIsNormal(resAuthenticity);
        ntsTaxService.saveNtsTax(ntsTax);
    }
}
