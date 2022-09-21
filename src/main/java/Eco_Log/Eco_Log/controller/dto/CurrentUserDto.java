package Eco_Log.Eco_Log.controller.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CurrentUserDto {



    private Long userId;

    private String nickName;
    private String email;

    @Builder
    public CurrentUserDto(Long userId, String nickNamem ,String email){
        this.userId = userId;
        this.nickName = nickNamem;
        this.email = email;
    }




}
