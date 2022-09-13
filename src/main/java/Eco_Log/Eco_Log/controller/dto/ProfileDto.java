package Eco_Log.Eco_Log.controller.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProfileDto {

    private Long snsId;


    private String profileImg;
    private String email;
    private String selfIntroduce;


    @Builder ProfileDto(Long snsId,String profileImg,String email,String selfIntroduce ){
        this.snsId = snsId;
        this.profileImg = profileImg;
        this.email = email;
        this.selfIntroduce = selfIntroduce;

    }
}
