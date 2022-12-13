package Eco_Log.Eco_Log.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class UserBadgeResponseDto {
    private Long userId;
    private List<Character> badgeStateList;

    public UserBadgeResponseDto(Long userId, List<Character> badgeStateList) {
        this.userId = userId;
        this.badgeStateList = badgeStateList;
    }

    @Override
    public String toString() {
        return "UserBadgeResponseDto{" +
                "userId=" + userId +
                ", badgeStateList=" + badgeStateList +
                '}';
    }
}
