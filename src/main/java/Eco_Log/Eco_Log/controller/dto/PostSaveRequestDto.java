package Eco_Log.Eco_Log.controller.dto;

import Eco_Log.Eco_Log.domain.post.Posts;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class PostSaveRequestDto {

    // 수정필요 연관관계따져서
    private String userId;

    private String comment;

    private String doingDay;

    private List<String> doingList;


    @Builder
    public PostSaveRequestDto(String userId, String comment, String doingDay, List<String> doingList){
        this.userId= userId;
        this.comment = comment;
        this.doingDay = doingDay;
        this.doingList = doingList;
    }

    public Posts toEntity(){
        return Posts.builder()
                .userId(userId)
                .comment(comment)
                .doingDay(doingDay)
                .doingList(doingList)
                .build();
    }

}
