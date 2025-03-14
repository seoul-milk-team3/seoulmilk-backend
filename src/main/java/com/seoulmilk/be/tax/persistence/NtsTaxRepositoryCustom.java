package com.seoulmilk.be.tax.persistence;

import com.seoulmilk.be.tax.domain.type.PayStatus;
import com.seoulmilk.be.tax.domain.type.RegionType;
import com.seoulmilk.be.tax.domain.type.ResultType;
import com.seoulmilk.be.tax.dto.response.branch.BranchTaxFilterResponse;
import com.seoulmilk.be.tax.dto.response.office.OfficeTaxFilterResponse;
import com.seoulmilk.be.auth.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface NtsTaxRepositoryCustom {

    Page<OfficeTaxFilterResponse> findOfficeTaxByFilters(LocalDate startYearAndMonth,
                                                         LocalDate endYearAndMonth,
                                                         RegionType region,
                                                         String searchSupplierName,
                                                         ResultType resultType,
                                                         String isValidated,
                                                         Pageable pageable,
                                                         User user);

    List<BranchTaxFilterResponse> findBranchTaxByFiltersAndUser(LocalDate startDate,
                                                                LocalDate endDate,
                                                                ResultType resultType,
                                                                PayStatus payStatus,
                                                                User user,
                                                                Pageable pageable);
}