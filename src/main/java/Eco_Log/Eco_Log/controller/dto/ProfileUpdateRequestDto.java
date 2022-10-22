package Eco_Log.Eco_Log.controller.dto;


import lombok.Builder;
import lombok.Getter;

@Getter
public class ProfileUpdateRequestDto {

    private String nickName;

    private String selfIntroduce;

    private boolean isPublic;

    public ProfileUpdateRequestDto() {
    }

    @Builder
    public ProfileUpdateRequestDto(String nickName, String selfIntroduce,boolean isPublic) {
        this.nickName = nickName;
        this.selfIntroduce = selfIntroduce;
        this.isPublic = isPublic;
    }
}
