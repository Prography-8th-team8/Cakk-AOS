package org.prography.domain.model.enums

enum class DistrictType(val districtKr: String, val groupId: Int) {
    DOBONG("도봉", 1),
    GANGBUK("강북", 1),
    SEONGBUK("성북", 1),
    NOWON("노원", 1),
    DONGDAEMUN("동대문", 2),
    JUNGNANG("중랑", 2),
    SEONGDONG("성동", 2),
    GWANGJIN("광진", 2),
    EUNPYEONG("은평", 3),
    MAPO("마포", 3),
    SEODAEMUN("서대문", 3),
    JONGNO("종로", 4),
    JUNG("중구", 4),
    YONGSAN("용산", 4),
    GANGSEO("강서", 5),
    YANGCHEON("양천", 5),
    YEONGDEUNGPO("영등포", 5),
    GURO("구로", 5),
    DONGJAK("동작", 6),
    GWANAK("관악", 6),
    GEUMCHEON("금천", 6),
    SEOCHO("서초", 7),
    GANGNAM("강남", 7),
    GANGDONG("강동", 8),
    SONGPA("송파", 8),
    ;

    companion object {
        fun getName(district: String) = valueOf(district)
    }
}
