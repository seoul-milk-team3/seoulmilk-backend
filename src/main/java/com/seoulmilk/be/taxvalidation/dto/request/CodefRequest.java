package com.seoulmilk.be.taxvalidation.dto.request;

import com.seoulmilk.be.tax.domain.NtsTax;
import com.seoulmilk.be.user.domain.User;
import io.codef.api.EasyCodef;

public record CodefRequest(
        EasyCodef easyCodef,
        User user,
        NtsTax ntsTax,
        String loginTypeLevel,
        boolean isTwoWay
) {
}
