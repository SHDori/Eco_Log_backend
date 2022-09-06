package Eco_Log.Eco_Log.controller.dto;


import lombok.Builder;
import lombok.Getter;

import java.util.List;

/**
 * 게시물 수정 요청 dto
 * 수정할수 있는것은
 * comment, 행동list, 직접 행동 list
 *
 * postId 는 수정할 게시물 id
 */

@Getter
public class PostUpdateRequestDto {

    private Long postId;

    // 수정필요 연관관계따져서
    private String comment;

    private List<String> doingList;
    // 직접 입력 행동
    private List<String> customizedBehaviors;

    @Builder
    public PostUpdateRequestDto(Long postId,String comment, List<String> doingList,List<String> customizedBehaviors){

        this.postId = postId;
        this.comment = comment;
        this.doingList = doingList;
        this.customizedBehaviors = customizedBehaviors;
    }
}
