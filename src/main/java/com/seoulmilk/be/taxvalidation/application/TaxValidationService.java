package com.seoulmilk.be.taxvalidation.application;

import com.seoulmilk.be.auth.service.AuthService;
import com.seoulmilk.be.tax.domain.NtsTax;
import com.seoulmilk.be.tax.exception.NtsTaxNotFoundException;
import com.seoulmilk.be.tax.persistence.NtsTaxRepository;
import com.seoulmilk.be.taxvalidation.application.thread.CodefRequestThread;
import com.seoulmilk.be.taxvalidation.application.thread.CodefRequestThreadManager;
import com.seoulmilk.be.taxvalidation.dto.request.CodefRequest;
import com.seoulmilk.be.taxvalidation.dto.request.InvoiceValidationRequest;
import com.seoulmilk.be.taxvalidation.dto.response.InvoiceVerificationResponse;
import com.seoulmilk.be.taxvalidation.infrastructure.codef.EasyCodefProvider;
import com.seoulmilk.be.taxvalidation.infrastructure.request.EasyCodefRequestFactory;
import com.seoulmilk.be.user.domain.User;
import io.codef.api.EasyCodef;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.seoulmilk.be.tax.exception.errorcode.NtsTaxErrorCode.NTS_TAX_NOT_FOUND;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaxValidationService {
    private static final String IS_VALIDATED = "1";

    @Value("${api.codef.request-term}")
    private Long requestTerm;

    @Value("${api.codef.validating-term}")
    private Long validatingTerm;

    @Value("${api.codef.url}")
    private String productUrl;

    private final AuthService authService;
    private final NtsTaxRepository ntsTaxRepository;
    private final EasyCodefProvider easyCodefProvider;
    private final EasyCodefRequestFactory easyCodefRequestFactory;
    private final CodefCacheService codefCacheService;

    public void validateInvoicesPreVerified(List<InvoiceValidationRequest> request, String loginTypeLevel) {
        User user = authService.getLoginUser();
        List<NtsTax> ntsTaxes = getNtsTaxesById(request);
        EasyCodef easyCodef = easyCodefProvider.getEasyCodef();

        for (int i = 0; i < ntsTaxes.size(); i++) {
            NtsTax ntsTax = ntsTaxes.get(i);
            CodefRequestThread thread = CodefRequestThread.builder()
                    .codefId(user.getCodefId())
                    .productUrl(productUrl)
                    .threadNo(i)
                    .codefRequest(new CodefRequest(easyCodef, user, ntsTax, loginTypeLevel, false))
                    .easyCodefRequestFactory(easyCodefRequestFactory)
                    .codefCacheService(codefCacheService)
                    .ntsTaxRepository(ntsTaxRepository)
                    .build();

            thread.start();
            sleepThread(1, requestTerm);
        }
    }

    public List<InvoiceVerificationResponse> validateInvoicePostVerified(List<InvoiceValidationRequest> requests) {
        User user = authService.getLoginUser();

        CodefRequestThreadManager.notifyUserThread(user.getCodefId());
        sleepThread(requests.size(), validatingTerm);
        codefCacheService.removeTwoWayInfo(user.getCodefId());

        return findTaxIsNormal(getTaxes(requests));
    }

    private List<NtsTax> getNtsTaxesById(List<InvoiceValidationRequest> taxIds) {
        return taxIds.stream()
                .map(tax -> ntsTaxRepository.findById(tax.id()).orElseThrow(() -> new NtsTaxNotFoundException(NTS_TAX_NOT_FOUND)))
                .collect(Collectors.toList());
    }

    private void sleepThread(int size, Long term) {
        try {
            Thread.sleep(term * size);
        } catch (InterruptedException e) {
            log.error("error occurred: {}", e.getMessage());
            throw new RuntimeException(e);
        }
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
            log.info("ntsTaxId: {}, isNormal: {}", ntsTax.getId(), ntsTax.getIsNormal());
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
}

