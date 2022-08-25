package Eco_Log.Eco_Log.domain.post;


import Eco_Log.Eco_Log.tool.StringListConverter;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
public class Posts {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 연관 관계 조정
    private String userId;

    // 오늘의 한마디
    private String comment;

    // 수행한 날
    private String doingDay;

    @Convert(converter = StringListConverter.class)
    private List<String> doingList;

    @Builder
    public Posts(String userId, String comment, String doingDay, List<String> doingList){
        this.userId = userId;
        this.comment = comment;
        this.doingDay = doingDay;
        this.doingList = doingList;
    }


}
