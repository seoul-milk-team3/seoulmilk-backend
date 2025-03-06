package com.seoulmilk.be.tax.persistence;

import com.seoulmilk.be.tax.dto.response.OfficeTaxFilterResponse;
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
}
