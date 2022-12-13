package Eco_Log.Eco_Log.controller.dto.postDto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class PostSaveResponseDto {

    private Long postId;
    private List<Integer> badgeAchieveList;

    public PostSaveResponseDto(Long postId, List<Integer> badgeAchieveList) {
        this.postId = postId;
        this.badgeAchieveList = badgeAchieveList;
    }
}
