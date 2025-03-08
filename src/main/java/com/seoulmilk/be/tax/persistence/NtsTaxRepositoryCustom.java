package com.seoulmilk.be.tax.persistence;

import com.seoulmilk.be.tax.dto.response.BranchTaxFilterResponse;
import com.seoulmilk.be.tax.dto.response.OfficeTaxFilterResponse;
import com.seoulmilk.be.tax.dto.request.BranchTaxFilterRequest;
import com.seoulmilk.be.user.domain.User;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface NtsTaxRepositoryCustom {

    List<OfficeTaxFilterResponse> findOfficeTaxByFilters (LocalDate startYearAndMonth,
                                                          LocalDate endYearAndMonth,
                                                          String region,
                                                          String searchSupplierName,
                                                          String resultType,
                                                          String isValidated,
                                                          Pageable pageable);

    List<BranchTaxFilterResponse> findBranchTaxByFiltersAndUser(BranchTaxFilterRequest filter, User user, Pageable pageable);
}
