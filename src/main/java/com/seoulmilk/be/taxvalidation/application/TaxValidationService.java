package com.seoulmilk.be.taxvalidation.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.seoulmilk.be.auth.application.AuthService;
import com.seoulmilk.be.tax.domain.NtsTax;
import com.seoulmilk.be.tax.exception.NtsTaxNotFoundException;
import com.seoulmilk.be.tax.persistence.NtsTaxRepository;
import com.seoulmilk.be.taxvalidation.dto.request.CodefRequest;
import com.seoulmilk.be.taxvalidation.dto.request.InvoiceValidationRequest;
import com.seoulmilk.be.taxvalidation.dto.response.InvoiceVerificationResponse;
import com.seoulmilk.be.taxvalidation.exception.TaxValidationException;
import com.seoulmilk.be.taxvalidation.infrastructure.codef.CodefCacheService;
import com.seoulmilk.be.taxvalidation.infrastructure.codef.EasyCodefProvider;
import com.seoulmilk.be.auth.domain.User;
import io.codef.api.EasyCodef;
import io.codef.api.EasyCodefServiceType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.seoulmilk.be.tax.exception.errorcode.NtsTaxErrorCode.NTS_TAX_NOT_FOUND;
import static com.seoulmilk.be.taxvalidation.exception.errorcode.TaxValidationErrorCode.CODEF_API_ERROR;
import static com.seoulmilk.be.taxvalidation.exception.errorcode.TaxValidationErrorCode.JSON_PROCESSING_ERROR;
import static com.seoulmilk.be.taxvalidation.infrastructure.constants.CodefParameter.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaxValidationService {
    private static final String IS_VALIDATED = "1";

    @Value("${api.codef.request-term}")
    private Long requestTerm;

    @Value("${api.codef.url}")
    private String productUrl;

    private final AuthService authService;
    private final NtsTaxRepository ntsTaxRepository;
    private final EasyCodefProvider easyCodefProvider;
    private final CodefCacheService codefCacheService;
    private final AsyncValidateService asyncValidateService;

    public void validateInvoicesPreVerified(List<InvoiceValidationRequest> request, String loginTypeLevel) {
        User user = authService.getLoginUser();
        List<NtsTax> ntsTaxes = getNtsTaxesById(request);
        EasyCodef easyCodef = easyCodefProvider.getEasyCodef();

        for (int i = 0; i < ntsTaxes.size(); i++) {
            NtsTax ntsTax = ntsTaxes.get(i);
            CodefRequest codefRequest = new CodefRequest(easyCodef, user, ntsTax, loginTypeLevel, false);
            asyncValidateService.validateInvoicesPreVerified(productUrl, i, codefRequest);
            sleepThread(requestTerm);
        }

    }

    private void sleepThread(Long requestTerm) {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        CompletableFuture<Void> future = new CompletableFuture<>();
        scheduler.schedule(() -> future.complete(null), requestTerm, TimeUnit.MILLISECONDS);
        future.join();
        scheduler.shutdown();
    }

    public List<InvoiceVerificationResponse> validateInvoicePostVerified(List<InvoiceValidationRequest> requests) {
        User user = authService.getLoginUser();

        String response = afterAuthenticatedRequest(user.getCodefId());
        analyzeResponse(response, requests.get(0).id());

        codefCacheService.removeTwoWayInfo(user.getCodefId());
        asyncValidateService.validateInvoicesPostVerified(user.getCodefId());

        return findTaxIsNormal(getTaxes(requests));
    }

    private List<NtsTax> getNtsTaxesById(List<InvoiceValidationRequest> taxIds) {
        return taxIds.stream()
                .map(tax -> ntsTaxRepository.findById(tax.id()).orElseThrow(() -> new NtsTaxNotFoundException(NTS_TAX_NOT_FOUND)))
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateIsValidated(List<InvoiceValidationRequest> requests) {
        List<NtsTax> ntsTaxes = getTaxes(requests);
        for (NtsTax ntsTax : ntsTaxes) {
            ntsTax.updateIsValidated(IS_VALIDATED);
        }
    }

    public List<InvoiceVerificationResponse> findTaxIsNormal(List<NtsTax> taxes) {
        List<InvoiceVerificationResponse> result = new ArrayList<>();
        for (NtsTax ntsTax : taxes) {
            result.add(new InvoiceVerificationResponse(ntsTax.getId(), ntsTax.getIsNormal()));
        }
        return result;
    }

    private List<NtsTax> getTaxes(List<InvoiceValidationRequest> requests) {
        List<Long> taxIds = requests.stream()
                .map(InvoiceValidationRequest::id)
                .toList();
        return ntsTaxRepository.findAllById(taxIds);
    }

    private String afterAuthenticatedRequest(String codefId) {
        EasyCodef easyCodef = easyCodefProvider.getEasyCodef();
        HashMap<String, Object> certificatedBody = new HashMap<>(codefCacheService.getFirstTaxBody(codefId));
        certificatedBody.putAll(Map.of(SIMPLE_AUTH.getParamName(), "1", IS_2_WAY.getParamName(), true));
        certificatedBody.put(TWO_WAY_INFO.getParamName(), codefCacheService.getTwoWayInfo(codefId));

        try {
            return easyCodef.requestCertification(productUrl, EasyCodefServiceType.DEMO, certificatedBody);
        } catch (UnsupportedEncodingException | InterruptedException | JsonProcessingException e) {
            throw new TaxValidationException(CODEF_API_ERROR);
        }
    }

    private void analyzeResponse(String response, Long taxId) {
        NtsTax ntsTax = ntsTaxRepository.findById(taxId).orElseThrow(() -> new NtsTaxNotFoundException(NTS_TAX_NOT_FOUND));
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

