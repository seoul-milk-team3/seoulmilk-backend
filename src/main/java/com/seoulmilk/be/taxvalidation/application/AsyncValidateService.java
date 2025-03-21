package com.seoulmilk.be.taxvalidation.application;

import com.seoulmilk.be.auth.application.AuthService;
import com.seoulmilk.be.auth.domain.User;
import com.seoulmilk.be.tax.persistence.NtsTaxRepository;
import com.seoulmilk.be.taxvalidation.infrastructure.codef.CodefCacheService;
import com.seoulmilk.be.taxvalidation.infrastructure.codef.CodefRequestThread;
import com.seoulmilk.be.taxvalidation.dto.request.CodefRequest;
import com.seoulmilk.be.taxvalidation.infrastructure.codef.EasyCodefRequestFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class AsyncValidateService {
    private final AuthService authService;
    private final EasyCodefRequestFactory easyCodefRequestFactory;
    private final CodefCacheService codefCacheService;
    private final NtsTaxRepository ntsTaxRepository;
    private final Map<String, List<CompletableFuture<Void>>> validateTasks = new ConcurrentHashMap<>();

    @Async
    public CompletableFuture<Void> validateInvoicesPreVerified(String productUrl, int threadNo, CodefRequest codefRequest) {
        User user = authService.getLoginUser();
        CodefRequestThread thread = CodefRequestThread.builder()
                .codefId(user.getCodefId())
                .productUrl(productUrl)
                .threadNo(threadNo)
                .codefRequest(codefRequest)
                .easyCodefRequestFactory(easyCodefRequestFactory)
                .codefCacheService(codefCacheService)
                .ntsTaxRepository(ntsTaxRepository)
                .build();

        CompletableFuture<Void> future = CompletableFuture.runAsync(thread);

        validateTasks.computeIfAbsent(user.getCodefId(), t -> new ArrayList<>()).add(future);

        return future;
    }

    public void validateInvoicesPostVerified(String codefId) {
        List<CompletableFuture<Void>> futures = validateTasks.getOrDefault(codefId, new ArrayList<>());
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        validateTasks.remove(codefId);
    }
}
