package Eco_Log.Eco_Log.controller.dto.postDto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class PostSaveResponseDto {

    private Long postId;
    private List<Integer> badgeAcheiveList;

    public PostSaveResponseDto(Long postId, List<Integer> badgeAcheiveList) {
        this.postId = postId;
        this.badgeAcheiveList = badgeAcheiveList;
    }
}
