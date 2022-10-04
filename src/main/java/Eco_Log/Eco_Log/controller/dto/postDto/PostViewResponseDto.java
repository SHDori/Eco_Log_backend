package Eco_Log.Eco_Log.controller.dto.postDto;

import Eco_Log.Eco_Log.controller.dto.UserSimpleInfoInPostDto;
import lombok.Getter;

import java.util.List;

@Getter
public class PostViewResponseDto {

    private Long postId;

    private UserSimpleInfoInPostDto userInfo;

    private List<String> customBehaviorList;

    private List<String> behaviorList;

    private String comment;

    public PostViewResponseDto(Long postId, UserSimpleInfoInPostDto userInfo, List<String> customBehaviorList, List<String> behaviorList, String comment) {
        this.postId = postId;
        this.userInfo = userInfo;
        this.customBehaviorList = customBehaviorList;
        this.behaviorList = behaviorList;
        this.comment = comment;
    }
}
