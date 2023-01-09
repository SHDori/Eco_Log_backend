package Eco_Log.Eco_Log.controller;


import Eco_Log.Eco_Log.controller.dto.*;
import Eco_Log.Eco_Log.domain.jwt.JwtProperties;
import Eco_Log.Eco_Log.domain.user.Users;
import Eco_Log.Eco_Log.service.ProfileService;
import Eco_Log.Eco_Log.service.UserServie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserServie userService;

    @Autowired
    private ProfileService profileService;

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
//        HttpHeaders headers = new HttpHeaders();
//        headers.add(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + jwtToken);
        Map<String,String> jwtResult = new HashMap<>();
        jwtResult.put(JwtProperties.HEADER_STRING,JwtProperties.TOKEN_PREFIX + jwtToken);

//        return ResponseEntity.ok().headers(headers).body("success");

        return ResponseEntity.ok().body(jwtResult);
    }

    /**
     * 네이버 로그인
     *
     */
    @GetMapping("/oauth/navertoken")
    public ResponseEntity getNaverLogin(@RequestParam("code") String code,@RequestParam(required = false) String state){
        System.out.println("Get Naver Login호출완료 => "+code);
        // 1. front에서 넘어온 인가코드로 Naver에 Access토큰을 요청후 받아옴
        OauthToken oauthToken = userService.getNaverAccessToken(code,state);

        System.out.println("naver oauthToken완료");

        // 2. 처음 로그인한 사람이면 kakao에 프로필정보를 받아오고 access토큰을 기반으로 jwt토큰을 발행
        String jwtToken = userService.saveNaverUserAndGetJwtToken(oauthToken.getAccess_token());
        System.out.println("네이버 로그인 JWT 토큰 => "+ jwtToken);

        Map<String,String> jwtResult = new HashMap<>();
        jwtResult.put(JwtProperties.HEADER_STRING,JwtProperties.TOKEN_PREFIX + jwtToken);

        return ResponseEntity.ok().body(jwtResult);
    }


    /**
     * 구글 로그인
     *
     */
    @GetMapping("/oauth/googletoken")
    public ResponseEntity getGoogleLogin(@RequestParam("code") String code){
        System.out.println("Get Google Login호출완료 => "+code);
        // 1. front에서 넘어온 인가코드로 Naver에 Access토큰을 요청후 받아옴
        GoogleOauthToken oauthToken = userService.getGoogleAccessToken(code);

        System.out.println("Google oauthToken완료"+oauthToken.getAccess_token());

        String jwtToken = userService.saveGoogleUserAndGetJwtToken(oauthToken.getAccess_token());
        System.out.println("구글 로그인 JWT 토큰 => "+ jwtToken);

        Map<String,String> jwtResult = new HashMap<>();
        jwtResult.put(JwtProperties.HEADER_STRING,JwtProperties.TOKEN_PREFIX + jwtToken);

        return ResponseEntity.ok().body(jwtResult);

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

//    @GetMapping("/user/summary")
//    public List<SummaryInfoDTO> getUserSummary(HttpServletRequest request){
//        Long userId = (Long) request.getAttribute("userId");
//
//        return userService.findSummaryByUserId(userId);
//    }

    @GetMapping("/user/summary")
    public ResponseEntity<List<SummaryInfoDTO>> getUserSummary(HttpServletRequest request){
        Long userId = (Long) request.getAttribute("userId");
        List<SummaryInfoDTO> responseData = userService.findSummaryByUserId(userId);
        return ResponseEntity.ok().body(responseData);
    }


    // 프로필 조회
//    @GetMapping("/user/profile")
//    public ProfileViewResponseDto getUserProfile(HttpServletRequest request,@RequestParam("targetId")Long targetUserId){
//        Long userId = (Long) request.getAttribute("userId");
//
//        return userService.getUserProfileInfo(userId,targetUserId);
//    }

    @GetMapping("/user/profile")
    public ResponseEntity<ProfileViewResponseDto> getUserProfile(HttpServletRequest request,@RequestParam("targetId")Long targetUserId){
        Long userId = (Long) request.getAttribute("userId");
        ProfileViewResponseDto responseDto = userService.getUserProfileInfo(userId,targetUserId);
        return ResponseEntity.ok().body(responseDto);
    }


    @PostMapping("/user/profile")
    public ResponseEntity profileUpdate(HttpServletRequest request, @RequestBody ProfileUpdateRequestDto updateRequestDto){
        Long userId = (Long) request.getAttribute("userId");
        System.out.println("controller에 요청된 boolean값 => " + updateRequestDto.getPublic());
        Long targetUserId = profileService.update(userId,updateRequestDto);

        return ResponseEntity.ok().body(targetUserId);
    }


    @GetMapping("/user/search")
    public ResponseEntity searchingUser(HttpServletRequest request,@RequestParam("keyword")String keyword){

        Long userId = (Long) request.getAttribute("userId");

        List<UserSearchResponseDto> searchResult = userService.getSearchUserList(userId,keyword);

        return ResponseEntity.ok().body(searchResult);


    }

    @GetMapping("/user/badge")
    public ResponseEntity getUserBadgeState(HttpServletRequest request){
        Long userId = (Long) request.getAttribute("userId");
        UserBadgeResponseDto badgeStateDto = userService.getUserBadgeState(userId);


        return ResponseEntity.ok().body(badgeStateDto);

    }

    @DeleteMapping("/user")
    public ResponseEntity deleteUser(HttpServletRequest request){
        Long userId = (Long) request.getAttribute("userId");
        boolean result = userService.deleteUser(userId);

        if(result){
            return ResponseEntity.ok().body("성공적으로 삭제하였습니다.");
        }else{
            return ResponseEntity.badRequest().body("관리자의 확인이필요합니다.");
        }

    }










}
