package com.seoulmilk.be.user.domain.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    ADMIN("ROLE_ADMIN"),
    OFFICE("ROLE_OFFICE"),
    BRANCH("ROLE_BRANCH"),
    ;

    private final String name;
}
