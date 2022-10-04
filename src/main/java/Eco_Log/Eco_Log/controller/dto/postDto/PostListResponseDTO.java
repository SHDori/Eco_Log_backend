package Eco_Log.Eco_Log.controller.dto.postDto;


import Eco_Log.Eco_Log.domain.post.Posts;
import lombok.Getter;

import java.util.List;

@Getter
public class PostListResponseDTO {

    private Long postId;
    private String userName;
    private String doingDay;
    private String comment;
    private List<String> doingList;
    private List<String> customBehaviorList;

    public PostListResponseDTO(Posts posts){
        this.postId = posts.getId();
        this.userName = posts.getUsers().getName();
        this.doingDay = posts.getDoingDay();
        this.comment = posts.getComment();

        this.customBehaviorList = posts.getCustomBehaviorList();
    }

}
