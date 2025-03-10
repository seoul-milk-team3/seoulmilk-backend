package com.seoulmilk.be.tax.domain.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RegionType {
    ALL("전체"),
    INCHEON("인천"),
    SEOUL("서울"),
    BUSAN("부산"),
    DAEGU("대구"),
    GWANGJU("광주"),
    DAEJEON("대전"),
    ULSAN("울산"),
    SEJONG("세종"),
    GYEONGGI("경기"),
    GANGWON("강원"),
    CHUNGBUK("충북"),
    CHUNGNAM("충남"),
    JEONBUK("전북"),
    JEONNAM("전남"),
    GYEONGBUK("경북"),
    GYEONGNAM("경남"),
    JEJU("제주");

    private final String value;

    public static RegionType fromString(String value) {
        if (value == null || value.isBlank()) {
            return ALL;
        }

        for (RegionType r : RegionType.values()) {
            if (r.getValue().equals(value)) {
                return r;
            }
        }

        return ALL;
    }
}