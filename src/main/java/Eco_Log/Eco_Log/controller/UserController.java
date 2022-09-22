package Eco_Log.Eco_Log.controller;


import Eco_Log.Eco_Log.controller.dto.CurrentUserDto;
import Eco_Log.Eco_Log.controller.dto.OauthToken;
import Eco_Log.Eco_Log.domain.jwt.JwtProperties;
import Eco_Log.Eco_Log.domain.user.Users;
import Eco_Log.Eco_Log.service.UserServie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserServie userService;

    // 카카오 로그인
    // 프론트로부터 인가코드를 받는다.
    @GetMapping("/oauth/kakaotoken")
    public ResponseEntity getLogin(@RequestParam("code") String code){
        System.out.println("GetLogin호출완료"+code);

        // 1. front에서 넘어온 인가코드로 Kakao에 Access토큰을 요청후 받아옴
        OauthToken oauthToken = userService.getKakaoAccessToken(code);

        System.out.println("oauthToken완료");

        // 2. 처음 로그인한 사람이면 kakao에 프로필정보를 받아오고 access토큰을 기반으로 jwt토큰을 발행
        String jwtToken = userService.saveKakaoUserAndGetJwtToken(oauthToken.getAccess_token());
        System.out.println("JWT 토큰 => "+ jwtToken);
        HttpHeaders headers = new HttpHeaders();
        headers.add(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + jwtToken);

        return ResponseEntity.ok().headers(headers).body("success");
    }

    // 인증된 사용자 정보 반환
    @GetMapping("/me")
    public ResponseEntity<Object> getCurrentUser(HttpServletRequest request) { //(1)

        //(2)
        // 어떻게 사용자정보를 보낼지 DTO를 만들어야함.
        CurrentUserDto currentUserDto = userService.getCurrentUser(request);

        //(3)
        return ResponseEntity.ok().body(currentUserDto);
    }



}