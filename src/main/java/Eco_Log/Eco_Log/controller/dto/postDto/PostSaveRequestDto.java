package Eco_Log.Eco_Log.controller.dto.postDto;

import Eco_Log.Eco_Log.domain.post.Posts;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class PostSaveRequestDto {


    private String comment;

    private String doingDay;

//    private List<String> doingList;
    // 직접 입력 행동
    private List<String> customizedBehaviors;

    private List<String> behaviorList;


    @Builder
    public PostSaveRequestDto(String comment, String doingDay,List<String> customizedBehaviors,List<String>behaviorList){

        this.comment = comment;
        this.doingDay = doingDay;

        this.customizedBehaviors = customizedBehaviors;
        this.behaviorList = behaviorList;
    }

//    public Posts toEntity(){
//        return Posts.builder()
//                .comment(comment)
//                .doingDay(doingDay)
//                .doingList(doingList)
//                .build();
//    }

}
