package Eco_Log.Eco_Log.controller.dto;

import Eco_Log.Eco_Log.domain.post.Posts;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class PostSaveRequestDto {

    private String userId;
    private String comment;

    private String doingDay;

    private List<String> doingList;
    // 직접 입력 행동
    private List<String> customizedBehaviors;


    @Builder
    public PostSaveRequestDto(String userId,String comment, String doingDay, List<String> doingList,List<String> customizedBehaviors){
        this.userId = userId;
        this.comment = comment;
        this.doingDay = doingDay;
        this.doingList = doingList;
        this.customizedBehaviors = customizedBehaviors;
    }

//    public Posts toEntity(){
//        return Posts.builder()
//                .comment(comment)
//                .doingDay(doingDay)
//                .doingList(doingList)
//                .build();
//    }

}
