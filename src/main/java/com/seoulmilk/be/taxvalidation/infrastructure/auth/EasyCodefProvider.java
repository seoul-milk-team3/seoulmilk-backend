package com.seoulmilk.be.taxvalidation.infrastructure.auth;

import io.codef.api.EasyCodef;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "api.codef")
@RequiredArgsConstructor
public class EasyCodefProvider {
    private final String clientId;
    private final String clientSecret;
    private final String publicKey;

    public EasyCodef getEasyCodef() {
        EasyCodef easyCodef = new EasyCodef();
        easyCodef.setClientInfoForDemo(clientId, clientSecret);
        easyCodef.setPublicKey(publicKey);
        return easyCodef;
    }
}
