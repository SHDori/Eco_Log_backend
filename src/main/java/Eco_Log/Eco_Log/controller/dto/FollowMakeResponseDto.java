package Eco_Log.Eco_Log.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class FollowMakeResponseDto {
    private String message;
    private List<Integer> badgeAchieveList;

    public FollowMakeResponseDto(String message, List<Integer> badgeAchieveList) {
        this.message = message;
        this.badgeAchieveList = badgeAchieveList;
    }
}
