package com.seoulmilk.be.user.domain.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Telecom {
    SKT(0),
    KT(1),
    LG(2),
    ;

    private final int num;
}
