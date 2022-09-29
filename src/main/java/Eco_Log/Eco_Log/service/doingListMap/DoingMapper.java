package Eco_Log.Eco_Log.service.doingListMap;


import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
public class DoingMapper {

    public Map<String, Integer> mappingList = new HashMap<>() {{
        put("친환경 상점 이용", 0);
        put("소비 없는 하루", 0);
        put("중고거래", 0);

        put("대중교통 이용", 1);
        put("20분 이하 거리 도보", 1);
        put("텀블러 소지", 1);
        put("손수건 사용", 1);

        put("행주 사용", 2);
        put("음식물 쓰래기 건조", 2);
        put("천연 수세미 사용", 2);

        put("한끼 채식", 3);
        put("배달음식 자제", 3);
        put("잔반 없음", 3);
        put("냉난방 적정온도 유지", 4);
        put("거리 쓰래기 수거", 4);
    }};
}
