package com.seoulmilk.be.global.presentation;

import com.seoulmilk.be.global.presentation.api.globalApi;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class globalController implements globalApi {

    @Value("${server.env}")
    private String env;

    @Override
    public ResponseEntity<String> healthcheck() {
        return ResponseEntity.ok(env);
    }
}
