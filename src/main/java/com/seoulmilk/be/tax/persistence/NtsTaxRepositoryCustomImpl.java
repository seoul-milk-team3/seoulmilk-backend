package com.seoulmilk.be.tax.persistence;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.seoulmilk.be.tax.domain.type.PayStatus;
import com.seoulmilk.be.tax.domain.type.ResultType;
import com.seoulmilk.be.tax.dto.response.BranchTaxFilterResponse;
import com.seoulmilk.be.tax.dto.response.OfficeTaxFilterResponse;
import com.seoulmilk.be.tax.dto.request.BranchTaxFilterRequest;
import com.seoulmilk.be.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;

import java.time.LocalDate;
import java.util.List;

import static com.seoulmilk.be.tax.domain.QNtsTax.ntsTax;

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
                                                                String isValidated,
                                                                Pageable pageable) {

        return jpaQueryFactory
                .select(Projections.constructor(OfficeTaxFilterResponse.class,
                        ntsTax.id,
                        ntsTax.suId,
                        ntsTax.ipId,
                        ntsTax.transDate,
                        ntsTax.suName,
                        ntsTax.suAddr,
                        ntsTax.isNormal,
                        ntsTax.isValidated,
                        ntsTax.createdDateTime
                        ))
                .from(ntsTax)
                .orderBy(ntsTax.id.desc())
                .where(
                        filterByIsValidated(isValidated),
                        filterByRegion(region),
                        filterBySupplierName(searchSupplierName),
                        filterByResultType(resultType),
                        filterByYearAndMonth(startYearAndMonth, endYearAndMonth)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    @Override
    public List<BranchTaxFilterResponse> findBranchTaxByFiltersAndUser(BranchTaxFilterRequest filter, User user, Pageable pageable) {
        return jpaQueryFactory
                .select(Projections.constructor(BranchTaxFilterResponse.class,
                        ntsTax.id,
                        ntsTax.issueId,
                        ntsTax.isNormal,
                        ntsTax.payStatus,
                        ntsTax.createdDateTime
                ))
                .from(ntsTax)
                .orderBy(ntsTax.id.desc())
                .where(
                        filterByPayStatus(filter.getPayStatus()),
                        filterByResultType(filter.getResultType()),
                        filterByYearAndMonth(filter.getStartDate(), filter.getEndDate()),
                        Expressions.stringTemplate("REPLACE({0}, '-', '')", ntsTax.suId)
                                .eq(user.getBusinessId().replace("-", ""))
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
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

    private BooleanExpression filterByPayStatus(PayStatus payStatus) {
        if (payStatus == null) {
            return null;
        }
        return ntsTax.payStatus.eq(payStatus);
    }

    private BooleanExpression filterByYearAndMonth(LocalDate startYearAndMonth, LocalDate endYearAndMonth) {
        if (ObjectUtils.isEmpty(startYearAndMonth) || ObjectUtils.isEmpty(endYearAndMonth)) {
            return null;
        } else {

            return ntsTax.createdDateTime.after(startYearAndMonth.atStartOfDay())
                    .and(ntsTax.createdDateTime.before(endYearAndMonth.plusDays(1).atStartOfDay()));

            //TODO: 일자 기준 필터링의 필드가 transDate 인지 createdTime 인지 확인 하고 주석 삭제 예정
//            return ntsTax.transDate.between(
//                    startYearAndMonth.toString(),
//                    (endYearAndMonth.plusDays(1)).toString()
//            );
        }
    }

    private BooleanExpression filterByIsValidated(String isValidated) {
        if (ObjectUtils.isEmpty(isValidated)) {
            return ntsTax.isValidated.ne(isValidated);
        } else {
            return ntsTax.isValidated.eq(isValidated);
        }
    }

}
