package Eco_Log.Eco_Log.controller.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 유저 검색
 * => 유저 검색을 email이나 닉네임으로 하고
 *    유저의 닉네임, 자기소개, 팔로우여부, 자주 기록한 실천을 반환
 */
@Getter
@NoArgsConstructor
public class UserSearchResponseDto {

    private Long userId;
    private String nickName;
    private String selfIntroduce;
    private List<SummaryInfoDTO> userSummary;
    private boolean isAlreadyFollow;

    public void setAlreadyFollow(boolean alreadyFollow) {
        isAlreadyFollow = alreadyFollow;
    }

    public UserSearchResponseDto(Long userId, String nickName, String selfIntroduce, List<SummaryInfoDTO> userSummary) {
        this.userId = userId;
        this.nickName = nickName;
        this.selfIntroduce = selfIntroduce;
        this.userSummary = userSummary;
    }

    @Override
    public String toString() {
        return "UserSearchResponseDto{" +
                "userId=" + userId +
                ", nickName='" + nickName + '\'' +
                ", selfIntroduce='" + selfIntroduce + '\'' +
                ", userSummary=" + userSummary +
                ", isAlreadyFollow=" + isAlreadyFollow +
                '}';
    }
}
