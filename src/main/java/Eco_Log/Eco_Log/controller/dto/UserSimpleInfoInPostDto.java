package Eco_Log.Eco_Log.controller.dto;


import lombok.Getter;

@Getter
public class UserSimpleInfoInPostDto {

    private Long userId;
    private String userNickname;
    private String selfIntroduce;

    public UserSimpleInfoInPostDto(Long userId, String userNickname, String selfIntroduce) {
        this.userId = userId;
        this.userNickname = userNickname;
        this.selfIntroduce = selfIntroduce;
    }
}
