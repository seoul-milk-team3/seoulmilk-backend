package com.seoulmilk.be.tax.persistence.init;

import com.seoulmilk.be.global.util.DummyDataInit;
import com.seoulmilk.be.tax.domain.NtsTax;
import com.seoulmilk.be.tax.domain.type.Arap;
import com.seoulmilk.be.tax.domain.type.PayStatus;
import com.seoulmilk.be.tax.persistence.NtsTaxRepository;
import com.seoulmilk.be.user.domain.User;
import com.seoulmilk.be.user.persistence.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Order(2)
@DummyDataInit
public class NtsTaxDummy implements ApplicationRunner {

    private final NtsTaxRepository ntsTaxRepository;
    private final UserRepository userRepository;

    @Override
    public void run(ApplicationArguments args) {
        if (ntsTaxRepository.count() > 0) {
            log.info("[NtsTax]더미 데이터 존재");
        } else {
            List<NtsTax> taxList = new ArrayList<>();

            User DUMMY_USER1 = userRepository.findById(1L)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            NtsTax DUMMY_TAX1 = NtsTax.builder()
                    .user(DUMMY_USER1) // 사용자
                    .issueId("202408084444333300001111") // 승인번호
                    .arap(Arap.AP) // 매출매입구분
                    .issueDt("2024-08-08") // 서명일자 발행일자
                    .issueDate("20240810") // 전자세금계산서 작성일자
                    .suId("444-33-22222") // 공급자 사업자등록번호
                    .ipId("333-22-88855") // 공급받는자 사업자등록번호
                    .chargeTotal(2752800L) // 총 공급가액 합계 (쉼표 제거)
                    .taxTotal(0L) // 총 세액 합계
                    .grandTotal(2752800L) // 총액 (공급가액 + 세액)
                    .ernam("dummy") // 생성자
                    .transDate("dummy") // 국세청 전송일자
                    .suName("대전중앙더미유통") // 공급자 사업체명
                    .suAddr("대전광역시 더미동111-111") // 공급자 주소
                    .ipName("대전 더미사업") // 공급받는자 사업체명
                    .ipAddr("대전광역시 더미동222-222") // 공급받는자 주소
                    .isNormal("Y") // 정상 여부
                    .imageUrl("https://dummy.com") // 이미지 URL
                    .payStatus(PayStatus.PAID) // 지급여부
                    .payDate("2024-08-08") // 지급일자
                    .build();

            taxList.add(DUMMY_TAX1);
            ntsTaxRepository.saveAll(taxList);
        }
    }
}
