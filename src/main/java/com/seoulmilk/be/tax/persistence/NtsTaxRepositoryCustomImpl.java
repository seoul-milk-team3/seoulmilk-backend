package com.seoulmilk.be.tax.persistence;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.seoulmilk.be.tax.domain.type.ResultType;
import com.seoulmilk.be.tax.dto.response.OfficeTaxFilterResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;

import java.time.LocalDate;
import java.util.List;

import static com.seoulmilk.be.tax.domain.QNtsTax.ntsTax;
import static com.seoulmilk.be.user.domain.QUser.user;

@Slf4j
@Repository
@RequiredArgsConstructor
public class NtsTaxRepositoryCustomImpl implements NtsTaxRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<OfficeTaxFilterResponse> findOfficeTaxByFilters(LocalDate startYearAndMonth,
                                                                LocalDate endYearAndMonth,
                                                                String region,
                                                                String searchSupplierName,
                                                                String resultType,
                                                                Pageable pageable) {

        try {
            List<OfficeTaxFilterResponse> result = jpaQueryFactory
                    .select(Projections.constructor(OfficeTaxFilterResponse.class,
                            ntsTax.suId,
                            ntsTax.ipId,
                            ntsTax.transDate,
                            ntsTax.suName,
                            ntsTax.suAddr,
                            ntsTax.isNormal))
                    .from(ntsTax)
                    .orderBy(ntsTax.id.desc())
                    .where(
                            filterByRegion(region),
                            filterBySupplierName(searchSupplierName),
                            filterByResultType(resultType),
                            filterByYearAndMonth(startYearAndMonth, endYearAndMonth)
                    )
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetch();

            log.info("Query executed, result size: {}", result.size());
            return result;

        } catch (Exception e) {
            log.error("Error executing query", e);
        }

        return null;
    }

    private BooleanExpression filterByRegion(String region) {
        if (ObjectUtils.isEmpty(region)) {
            return null;
        } else {
            return ntsTax.suAddr.contains(region);
        }
    }

    private BooleanExpression filterBySupplierName(String searchSupplierName) {
        if (ObjectUtils.isEmpty(searchSupplierName)) {
            return null;
        } else {
            return ntsTax.suName.contains(searchSupplierName);
        }
    }

    private BooleanExpression filterByResultType(String resultType) {
        ResultType type = ResultType.fromString(resultType);

        if (ObjectUtils.isEmpty(resultType) || type == ResultType.ALL) {
            return null;
        } else {
            return ntsTax.isNormal.eq(type);
        }
    }

    private BooleanExpression filterByYearAndMonth(LocalDate startYearAndMonth, LocalDate endYearAndMonth) {
        if (ObjectUtils.isEmpty(startYearAndMonth) || ObjectUtils.isEmpty(endYearAndMonth)) {
            return null;
        } else {
            return ntsTax.transDate.between(
                    startYearAndMonth.toString(),
                    (endYearAndMonth.plusDays(1)).toString()
            );
        }
    }
}
