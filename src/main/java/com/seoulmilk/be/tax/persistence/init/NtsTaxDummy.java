package com.seoulmilk.be.tax.persistence.init;

import com.seoulmilk.be.global.util.DummyDataInit;
import com.seoulmilk.be.tax.domain.NtsTax;
import com.seoulmilk.be.tax.domain.type.Arap;
import com.seoulmilk.be.tax.domain.type.PayStatus;
import com.seoulmilk.be.tax.domain.type.ResultType;
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
                    .user(DUMMY_USER1)
                    .issueId("202408084444333300001111")
                    .arap(Arap.AP)
                    .issueDt("2024-01-01")
                    .issueDate("20240101")
                    .suId("444-33-22222")
                    .ipId("333-22-88855")
                    .chargeTotal(2752800L)
                    .taxTotal(0L)
                    .grandTotal(2752800L)
                    .ernam("dummy")
                    .transDate("2024-01-01")
                    .suName("대전중앙더미유통")
                    .suAddr("대전광역시 더미동111-111")
                    .ipName("대전 더미사업")
                    .ipAddr("대전광역시 더미동222-222")
                    .isNormal(ResultType.NORMAL)
                    .imageUrl("https://dummy.com")
                    .payStatus(PayStatus.PAID)
                    .payDate("2024-08-08")
                    .build();

            NtsTax DUMMY_TAX2 = NtsTax.builder()
                    .user(DUMMY_USER1)
                    .issueId("202408084444333300001112")
                    .arap(Arap.AP)
                    .issueDt("2024-02-02")
                    .issueDate("20240202")
                    .suId("444-33-22223")
                    .ipId("333-22-88856")
                    .chargeTotal(2752801L)
                    .taxTotal(0L)
                    .grandTotal(2752801L)
                    .ernam("dummy")
                    .transDate("2024-02-02")
                    .suName("서울중앙더미유통")
                    .suAddr("서울광역시 더미동111-111")
                    .ipName("서울 더미사업")
                    .ipAddr("서울 더미동111-111")
                    .isNormal(ResultType.NORMAL)
                    .imageUrl("https://dummy.com")
                    .payStatus(PayStatus.PAID)
                    .payDate("2024-08-09")
                    .build();

            NtsTax DUMMY_TAX3 = NtsTax.builder()
                    .user(DUMMY_USER1)
                    .issueId("202408084444333300001113")
                    .arap(Arap.AP)
                    .issueDt("2024-03-03")
                    .issueDate("20240303")
                    .suId("444-33-22224")
                    .ipId("333-22-88857")
                    .chargeTotal(2752802L)
                    .taxTotal(0L)
                    .grandTotal(2752802L)
                    .ernam("dummy")
                    .transDate("2024-03-03")
                    .suName("광주중앙더미유통")
                    .suAddr("광주광역시 더미동222-222")
                    .ipName("광주 더미사업")
                    .ipAddr("광주 더미동111-111")
                    .isNormal(ResultType.ABNORMAL)
                    .imageUrl("https://dummy.com")
                    .payStatus(PayStatus.PAID)
                    .payDate("2024-08-10")
                    .build();

            NtsTax DUMMY_TAX4 = NtsTax.builder()
                    .user(DUMMY_USER1)
                    .issueId("202408084444333300001114")
                    .arap(Arap.AP)
                    .issueDt("2024-01-11")
                    .issueDate("20240111")
                    .suId("444-33-22225")
                    .ipId("333-22-88858")
                    .chargeTotal(2752803L)
                    .taxTotal(0L)
                    .grandTotal(2752803L)
                    .ernam("dummy")
                    .transDate("2024-01-11")
                    .suName("서울중앙더미유통")
                    .suAddr("서울광역시 더미동222-222")
                    .ipName("서울 더미사업")
                    .ipAddr("서울 더미동222-222")
                    .isNormal(ResultType.NORMAL)
                    .imageUrl("https://dummy.com")
                    .payStatus(PayStatus.PAID)
                    .payDate("2024-08-11")
                    .build();

            NtsTax DUMMY_TAX5 = NtsTax.builder()
                    .user(DUMMY_USER1)
                    .issueId("202408084444333300001115")
                    .arap(Arap.AP)
                    .issueDt("2024-02-12")
                    .issueDate("20240212")
                    .suId("444-33-22226")
                    .ipId("333-22-88859")
                    .chargeTotal(2752804L)
                    .taxTotal(0L)
                    .grandTotal(2752804L)
                    .ernam("dummy")
                    .transDate("2024-02-12")
                    .suName("울산중앙더미유통")
                    .suAddr("울산광역시 더미동222-222")
                    .ipName("울산 더미사업")
                    .ipAddr("울산 더미동111-111")
                    .isNormal(ResultType.ABNORMAL)
                    .imageUrl("https://dummy.com")
                    .payStatus(PayStatus.PAID)
                    .payDate("2024-08-12")
                    .build();

            NtsTax DUMMY_TAX6 = NtsTax.builder()
                    .user(DUMMY_USER1)
                    .issueId("202408084444333300001116")
                    .arap(Arap.AP)
                    .issueDt("2024-03-13")
                    .issueDate("20240313")
                    .suId("444-33-22227")
                    .ipId("333-22-88860")
                    .chargeTotal(2752805L)
                    .taxTotal(0L)
                    .grandTotal(2752805L)
                    .ernam("dummy")
                    .transDate("2024-03-13")
                    .suName("대구중앙더미유통")
                    .suAddr("대구광역시 더미동222-222")
                    .ipName("대구 더미사업")
                    .ipAddr("대구 더미동111-111")
                    .isNormal(ResultType.NORMAL)
                    .imageUrl("https://dummy.com")
                    .payStatus(PayStatus.PAID)
                    .payDate("2024-08-13")
                    .build();

            NtsTax DUMMY_TAX7 = NtsTax.builder()
                    .user(DUMMY_USER1)
                    .issueId("202408084444333300001117")
                    .arap(Arap.AP)
                    .issueDt("2024-03-13")
                    .issueDate("20240714")
                    .suId("444-33-22228")
                    .ipId("333-22-88861")
                    .chargeTotal(2752806L)
                    .taxTotal(0L)
                    .grandTotal(2752806L)
                    .ernam("dummy")
                    .transDate("2024-03-13")
                    .suName("부산중앙더미유통")
                    .suAddr("부산광역시 더미동222-222")
                    .ipName("부산 더미사업")
                    .ipAddr("부산 더미동111-111")
                    .isNormal(ResultType.ABNORMAL)
                    .imageUrl("https://dummy.com")
                    .payStatus(PayStatus.PAID)
                    .payDate("2024-08-14")
                    .build();

            NtsTax DUMMY_TAX8 = NtsTax.builder()
                    .user(DUMMY_USER1)
                    .issueId("202408084444333300001118")
                    .arap(Arap.AP)
                    .issueDt("2024-03-13")
                    .issueDate("20240815")
                    .suId("444-33-22229")
                    .ipId("333-22-88862")
                    .chargeTotal(2752807L)
                    .taxTotal(0L)
                    .grandTotal(2752807L)
                    .ernam("dummy")
                    .transDate("2024-03-13")
                    .suName("경기중앙더미유통")
                    .suAddr("경기도 더미동222-222")
                    .ipName("경기 더미사업")
                    .ipAddr("경기 더미동111-111")
                    .isNormal(ResultType.NORMAL)
                    .imageUrl("https://dummy.com")
                    .payStatus(PayStatus.PAID)
                    .payDate("2024-08-15")
                    .build();


            taxList.add(DUMMY_TAX1);
            taxList.add(DUMMY_TAX2);
            taxList.add(DUMMY_TAX3);
            taxList.add(DUMMY_TAX4);
            taxList.add(DUMMY_TAX5);
            taxList.add(DUMMY_TAX6);
            taxList.add(DUMMY_TAX7);
            taxList.add(DUMMY_TAX8);

            ntsTaxRepository.saveAll(taxList);
        }
    }
}
