package Eco_Log.Eco_Log.controller.dto;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonAutoDetect
public class ProfileUpdateRequestDto {

    private String nickName;

    private String selfIntroduce;

    private boolean isPublic;



    @Builder
    public ProfileUpdateRequestDto(String nickName, String selfIntroduce,boolean isPublic) {
        this.nickName = nickName;
        this.selfIntroduce = selfIntroduce;
        this.isPublic = isPublic;
    }
}
