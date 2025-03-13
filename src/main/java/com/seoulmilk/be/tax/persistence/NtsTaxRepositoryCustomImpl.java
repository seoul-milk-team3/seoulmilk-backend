package com.seoulmilk.be.tax.persistence;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.seoulmilk.be.tax.domain.type.PayStatus;
import com.seoulmilk.be.tax.domain.type.RegionType;
import com.seoulmilk.be.tax.domain.type.ResultType;
import com.seoulmilk.be.tax.dto.response.BranchTaxFilterResponse;
import com.seoulmilk.be.tax.dto.response.OfficeTaxFilterResponse;
import com.seoulmilk.be.user.domain.User;
import com.seoulmilk.be.user.persistence.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.seoulmilk.be.tax.domain.QNtsTax.ntsTax;


@Slf4j
@Repository
@RequiredArgsConstructor
public class NtsTaxRepositoryCustomImpl implements NtsTaxRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;
    private final UserRepository userRepository;

    @Override
    public List<OfficeTaxFilterResponse> findOfficeTaxByFilters(LocalDate startYearAndMonth,
                                                                LocalDate endYearAndMonth,
                                                                RegionType region,
                                                                String searchSupplierName,
                                                                ResultType resultType,
                                                                String isValidated,
                                                                Pageable pageable,
                                                                User userInfo) {

        Optional<User> users = userRepository.findByBusinessId(userInfo.getBusinessId());

        List<String> employeeIds = users.stream()
                .map(User::getEmployeeId)
                .toList();

        return jpaQueryFactory
                .select(Projections.constructor(OfficeTaxFilterResponse.class,
                        ntsTax.id,
                        ntsTax.issueId,
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
                        ntsTax.user.employeeId.in(employeeIds).or(Expressions.stringTemplate("REPLACE({0}, '-', '')", ntsTax.suId)
                                .eq(userInfo.getBusinessId().replace("-", ""))),
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
    public List<BranchTaxFilterResponse> findBranchTaxByFiltersAndUser(LocalDate startDate,
                                                                       LocalDate endDate,
                                                                       ResultType resultType,
                                                                       PayStatus payStatus,
                                                                       User user,
                                                                       Pageable pageable) {
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
                        filterByPayStatus(payStatus),
                        filterByResultType(resultType),
                        filterByYearAndMonth(startDate, endDate),
                        Expressions.stringTemplate("REPLACE({0}, '-', '')", ntsTax.suId)
                                .eq(user.getBusinessId().replace("-", ""))
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    private BooleanExpression filterByRegion(RegionType region) {
        if (region == RegionType.ALL || region.getValue().isBlank()) {
            return null;
        }

        return ntsTax.suAddr.contains(region.getValue());
    }


    private BooleanExpression filterBySupplierName(String searchSupplierName) {
        if (ObjectUtils.isEmpty(searchSupplierName)) {
            return null;
        } else {
            return ntsTax.suName.contains(searchSupplierName);
        }
    }

    private BooleanExpression filterByResultType(ResultType resultType) {
        if (ObjectUtils.isEmpty(resultType) || resultType == ResultType.ALL) {
            return null;
        } else {
            return ntsTax.isNormal.eq(resultType);
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