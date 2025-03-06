package com.seoulmilk.be.tax.domain;

import com.seoulmilk.be.global.domain.BaseTimeEntity;
import com.seoulmilk.be.tax.domain.type.*;
import com.seoulmilk.be.user.domain.User;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "NTS_TAX")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class NtsTax extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;  // 사용자

    @Column(name = "ISSUE_ID", length = 24, nullable = false)
    private String issueId;  // 승인번호

    @Enumerated(EnumType.STRING)
    @Column(name = "ARAP", length = 2, nullable = false)
    private Arap arap;  // 매출매입구분

    @Column(name = "ISSUE_DT", length = 24)
    private String issueDt;  // 서명일자 발행일자

    @Column(name = "BUKRS", length = 4, nullable = false)
    private String bukrs;  // 회사코드

    @Column(name = "BUPLA", length = 4, nullable = false)
    private String bupla;  // 사업장

    @Column(name = "ISSUE_DATE", length = 12, nullable = false)
    private String issueDate;  // 전자세금계산서 작성일자

    @Column(name = "SU_ID", length = 13, nullable = false)
    private String suId;  // 공급자 사업자등록번호

    @Column(name = "IP_ID", length = 13, nullable = false)
    private String ipId;  // 공급받는자 사업자등록번호

    @Column(name = "CHARGETOTAL", nullable = false)
    private Long chargeTotal;  // 총 공급가액 합계

    @Column(name = "TAXTOTAL", nullable = false)
    private Long taxTotal;  // 총 세액 합계

    @Column(name = "GRANDTOTAL", nullable = false)
    private Long grandTotal;  // 총액 (공급가액 + 세액)

    @Column(name = "ERNAM", length = 12, nullable = false)
    private String ernam;  // 생성자

    @Column(name = "TRANS_DATE", length = 12, nullable = false)
    private String transDate;  // 국세청 전송일자

    @Column(name = "SU_NAME", length = 70)
    private String suName;  // 공급자 사업체명

    @Column(name = "SU_ADDR", length = 150)
    private String suAddr;  // 공급자 주소

    @Column(name = "IP_NAME", length = 70)
    private String ipName;  // 공급받는자 사업체명

    @Column(name = "IP_ADDR", length = 150)
    private String ipAddr;  // 공급받는자 주소

    @Column(name = "IS_NORMAL", length = 1)
    private ResultType isNormal;  // 정상 여부

    @Column(name = "IMAGE_URL", length = 2000)
    private String imageUrl;  // 이미지 URL

    @Enumerated(EnumType.STRING)
    @Column(name = "PAY_STATUS", length = 20)
    private PayStatus payStatus;  // 지급여부

    @Column(name = "PAY_DATE", length = 12)
    private String payDate;  // 지급일자

    @Column(name = "IS_VALIDATED", length = 1)
    private String isValidated;  // 진위 확인 여부

    @Transient
    @Column(name = "INTER_NO", length = 24)
    private String interNo;  // 사업자 관리번호

    @Transient
    @Column(name = "ASP_CODE", length = 8)
    private String aspCode;  // 교부(발행)시스템코드

    @Transient
    @Enumerated(EnumType.STRING)
    @Column(name = "TYPE_CODE", length = 4)
    private TypeCode typeCode;  // 전자세금계산서 종류 구분자

    @Transient
    @Enumerated(EnumType.STRING)
    @Column(name = "PURP_CODE", length = 2)
    private PurpCode purpCode;  // 세금계산서의 영수/청구 구분 지시자

    @Transient
    @Enumerated(EnumType.STRING)
    @Column(name = "AMEND_CODE", length = 150)
    private AmendCode amendCode;  // 전자세금계산서 수정 사유 코드

    @Transient
    @Column(name = "DESC_TEXT1", length = 150)
    private String descText1;  // 비고 1

    @Transient
    @Column(name = "DESC_TEXT2", length = 150)
    private String descText2;  // 비고 2

    @Transient
    @Column(name = "DESC_TEXT3", length = 150)
    private String descText3;  // 비고 3

    @Transient
    @Column(name = "SU_MIN", length = 4)
    private String suMin;  // 공급자 종사업장 식별코드

    @Transient
    @Column(name = "SU_REPRES", length = 30)
    private String suRepres;  // 공급자 대표자명

    @Transient
    @Column(name = "SU_BUSTYPE", length = 40)
    private String suBustype;  // 공급자 업태

    @Transient
    @Column(name = "SU_INDTYPE", length = 40)
    private String suIndtype;  // 공급자 업종

    @Transient
    @Column(name = "SU_DEPTNAME", length = 40)
    private String suDeptname;  // 공급자 담당부서

    @Transient
    @Column(name = "SU_PERSNAME", length = 30)
    private String suPersname;  // 공급자 담당자명

    @Transient
    @Column(name = "SU_TELNO", length = 20)
    private String suTelno;  // 공급자 담당자 전화번호

    @Transient
    @Column(name = "SU_HPNO", length = 20)
    private String suHpno;  // 공급자 담당자 핸드폰

    @Transient
    @Column(name = "SU_EMAIL", length = 40)
    private String suEmail;  // 공급자 담당자 이메일

    @Transient
    @Enumerated(EnumType.STRING)
    @Column(name = "IP_TYPECODE", length = 2)
    private IpTypeCode ipTypecode;  // 공급받는자 사업자등록번호 구분코드

    @Transient
    @Column(name = "IP_MIN", length = 4)
    private String ipMin;  // 공급받는자 종사업장 식별코드

    @Transient
    @Column(name = "IP_REPRES", length = 30)
    private String ipRepres;  // 공급받는자 대표자명

    @Transient
    @Column(name = "IP_BUSTYPE", length = 40)
    private String ipBustype;  // 공급받는자 업태

    @Transient
    @Column(name = "IP_INDTYPE", length = 40)
    private String ipIndtype;  // 공급받는자 업종

    @Transient
    @Column(name = "IP_DEPTNAME1", length = 40)
    private String ipDeptname1;  // 공급받는자 담당부서 1

    @Transient
    @Column(name = "IP_PERSNAME1", length = 30)
    private String ipPersname1;  // 공급받는자 담당자명 1

    @Transient
    @Column(name = "IP_TELNO1", length = 20)
    private String ipTelno1;  // 공급받는자 담당자 전화번호 1

    @Transient
    @Column(name = "IP_HPNO1", length = 20)
    private String ipHpno1;  // 공급받는자 담당자 핸드폰 1

    @Transient
    @Column(name = "IP_EMAIL1", length = 40)
    private String ipEmail1;  // 공급받는자 담당자 이메일 1

    @Transient
    @Column(name = "IP_DEPTNAME2", length = 40)
    private String ipDeptname2;  // 공급받는자 담당부서 2

    @Transient
    @Column(name = "IP_PERSNAME2", length = 30)
    private String ipPersname2;  // 공급받는자 담당자명 2

    @Transient
    @Column(name = "IP_TELNO2", length = 20)
    private String ipTelno2;  // 공급받는자 담당자 전화번호 2

    @Transient
    @Column(name = "IP_HPNO2", length = 20)
    private String ipHpno2;  // 공급받는자 담당자 핸드폰 2

    @Transient
    @Column(name = "IP_EMAIL2", length = 40)
    private String ipEmail2;  // 공급받는자 담당자 이메일 2


    @Builder
    public NtsTax(User user, String issueId, Arap arap, String issueDt, String issueDate, String interNo, String aspCode, TypeCode typeCode, PurpCode purpCode, AmendCode amendCode, String descText1, String descText2, String descText3, String suId, String suMin, String suName, String suRepres, String suAddr, String suBustype, String suIndtype, String suDeptname, String suPersname, String suTelno, String suHpno, String suEmail, IpTypeCode ipTypecode, String ipId, String ipMin, String ipName, String ipRepres, String ipAddr, String ipBustype, String ipIndtype, String ipDeptname1, String ipPersname1, String ipTelno1, String ipHpno1, String ipEmail1, String ipDeptname2, String ipPersname2, String ipTelno2, String ipHpno2, String ipEmail2, Long chargeTotal, Long taxTotal, Long grandTotal, String ernam, String transDate, ResultType isNormal, String imageUrl, PayStatus payStatus, String payDate, String isValidated) {
        this.user = user;
        this.issueId = issueId;
        this.arap = arap;
        this.issueDt = issueDt;
        this.bukrs = "1000";    //default 회사 코드 1000 고정
        this.bupla = "1000";    //default  사업장 코드 1000 고정
        this.issueDate = issueDate;
        this.interNo = interNo;
        this.aspCode = aspCode;
        this.typeCode = typeCode;
        this.purpCode = purpCode;
        this.amendCode = amendCode;
        this.descText1 = descText1;
        this.descText2 = descText2;
        this.descText3 = descText3;
        this.suId = suId;
        this.suMin = suMin;
        this.suName = suName;
        this.suRepres = suRepres;
        this.suAddr = suAddr;
        this.suBustype = suBustype;
        this.suIndtype = suIndtype;
        this.suDeptname = suDeptname;
        this.suPersname = suPersname;
        this.suTelno = suTelno;
        this.suHpno = suHpno;
        this.suEmail = suEmail;
        this.ipTypecode = ipTypecode;
        this.ipId = ipId;
        this.ipMin = ipMin;
        this.ipName = ipName;
        this.ipRepres = ipRepres;
        this.ipAddr = ipAddr;
        this.ipBustype = ipBustype;
        this.ipIndtype = ipIndtype;
        this.ipDeptname1 = ipDeptname1;
        this.ipPersname1 = ipPersname1;
        this.ipTelno1 = ipTelno1;
        this.ipHpno1 = ipHpno1;
        this.ipEmail1 = ipEmail1;
        this.ipDeptname2 = ipDeptname2;
        this.ipPersname2 = ipPersname2;
        this.ipTelno2 = ipTelno2;
        this.ipHpno2 = ipHpno2;
        this.ipEmail2 = ipEmail2;
        this.chargeTotal = chargeTotal;
        this.taxTotal = taxTotal;
        this.grandTotal = grandTotal;
        this.ernam = ernam;
        this.transDate = transDate;
        this.isNormal = isNormal;
        this.imageUrl = imageUrl;
        this.payStatus = payStatus;
        this.payDate = payDate;
        this.isValidated = isValidated;
    }

    public void updateIsNormal(String isNormal) {
        if (isNormal.equals("1")) {
            this.isNormal = ResultType.NORMAL;
            return;
        }
        this.isNormal = ResultType.ABNORMAL;
    }

    // 진위 여부 확인 이후 검증 완료 변경 상태를 위한 메소드 (pr 리뷰 이후에 주석 지우고 merge 예정)
    public void updateIsValidated(String isValidated) {
        this.isValidated = isValidated;
    }
}
