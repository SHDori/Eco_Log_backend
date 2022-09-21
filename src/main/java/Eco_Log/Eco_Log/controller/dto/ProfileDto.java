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



    @Builder
    public ProfileDto(Long snsId,String profileImg,String email){
        this.snsId = snsId;
        this.profileImg = profileImg;
        this.email = email;


    }
}
