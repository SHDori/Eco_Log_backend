package Eco_Log.Eco_Log.controller.dto;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonAutoDetect
public class ProfileUpdateRequestDto {

    private String nickName;

    private String selfIntroduce;

    private int isPublic;


    public int getPublic(){
        return this.isPublic;
    }

    @Builder
    public ProfileUpdateRequestDto(String nickName, String selfIntroduce,int isPublic) {
        this.nickName = nickName;
        this.selfIntroduce = selfIntroduce;
        this.isPublic = isPublic;
    }
}
