package Eco_Log.Eco_Log.controller.dto;


import lombok.Builder;
import lombok.Getter;

@Getter
public class ProfileUpdateRequestDto {

    private String nickName;

    private String selfIntroduce;

    @Builder
    public ProfileUpdateRequestDto(String nickName, String selfIntroduce) {
        this.nickName = nickName;
        this.selfIntroduce = selfIntroduce;
    }
}
