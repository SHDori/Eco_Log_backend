package Eco_Log.Eco_Log.controller.dto.followDto;


import lombok.Getter;

@Getter
public class FollowViewResponseDto {

    private Long userId;
    private String nickName;
    private String selfIntroduce;

    public FollowViewResponseDto(Long userId, String nickName, String selfIntroduce) {
        this.userId = userId;
        this.nickName = nickName;
        this.selfIntroduce = selfIntroduce;
    }
}
