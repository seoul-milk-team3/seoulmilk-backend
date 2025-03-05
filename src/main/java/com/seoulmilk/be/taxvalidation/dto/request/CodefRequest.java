package com.seoulmilk.be.taxvalidation.dto.request;

import com.seoulmilk.be.tax.domain.NtsTax;
import com.seoulmilk.be.user.domain.User;

public record CodefRequest(
        User user,
        NtsTax ntsTax,
        String loginTypeLevel,
        boolean isTwoWay
) {
}
