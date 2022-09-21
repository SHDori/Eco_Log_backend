package Eco_Log.Eco_Log.controller.dto;

import lombok.Data;

/**
 * Login시
 * Front로부터 oauth 토큰을 전달받는데
 * 그 토큰을 감쌀 class
 */
@Data
public class OauthToken {
    private String access_token;
    private String token_type;
    private String refresh_token;
    private int expires_in;
    private String scope;
    private int refresh_token_expires_in;
}
