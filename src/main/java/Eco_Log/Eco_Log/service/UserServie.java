package Eco_Log.Eco_Log.service;


import Eco_Log.Eco_Log.controller.dto.*;
import Eco_Log.Eco_Log.domain.user.Profiles;
import Eco_Log.Eco_Log.domain.user.Summary;
import Eco_Log.Eco_Log.domain.user.Users;
import Eco_Log.Eco_Log.domain.user.dto.KakaoProfile;
import Eco_Log.Eco_Log.repository.PRconnectRepository;
import Eco_Log.Eco_Log.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpResponse;

import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.util.List;

import static Eco_Log.Eco_Log.config.SecurityConfig.FRONT_URL;


@RequiredArgsConstructor
@Service
@PropertySource("classpath:oauthInfo.properties")
public class UserServie {

    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final PRconnectRepository pRconnectRepository;
    @Autowired
    private final JwtService jwtService;

    @Value("${kakaoClientId}")
    private String kakaoClientId;

    @Value("${kakaoClientSecret}")
    private String kakaoClientSecret;




    @Transactional
    public Users save(String name,ProfileDto profileDto){

        Profiles profile = Profiles.builder()
                .snsId(profileDto.getSnsId())
                .profileImg(profileDto.getProfileImg())
                .email(profileDto.getEmail())
                .build();
        Summary summary = new Summary();

        Users user = Users.createUser(name,profile,summary);
        userRepository.save(user);
        return user;
    }




    // access Token을 받아모
    public String saveKakaoUserAndGetJwtToken(String token){

        // 1. access 토큰으로 kakao에서 프로필정보를 가져옴
        KakaoProfile profile = findKakaoProfile(token);

        // 2.프로필 정보를 기반으로 기존에있던 user인지 신규user인지 판단
        Users user = userRepository.findByEmail(profile.getKakao_account().getEmail());
        if(user == null){
            ProfileDto profileDto = ProfileDto.builder()
                    .snsId(profile.getId())
                    .profileImg(profile.getKakao_account().getProfile().getProfile_image_url())
                    .email(profile.getKakao_account().getEmail())
                    .build();

            // kakao 이름과 Profile정보를 넣어서 보내줌
            user = save(profile.getKakao_account().getProfile().getNickname() ,profileDto);
        }

        //3. user정보를 기반으로 jwt토큰을 발행후 return
        return jwtService.createToken(user);

    }

    public CurrentUserDto getCurrentUser(HttpServletRequest request){
        System.out.println(request);
        Long userId = (Long) request.getAttribute("userId");

        Users user = userRepository.findById(userId)
                .orElseThrow(()-> new IllegalArgumentException("해당 User가 없습니다. id = "+ userId));
        return CurrentUserDto.builder()
                .userId(user.getId())
                .nickNamem(user.getProfiles().getNickName())
                .email(user.getProfiles().getEmail())
                .build();
    }

    public OauthToken getKakaoAccessToken(String code){

        RestTemplate rt = new RestTemplate();




        // Header생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");


        System.out.println("Header까지 생성완료");
        // Body생성
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        
        // 여기는 Front와 상의 ㄱㄱ
        params.add("client_id", kakaoClientId);
        params.add("redirect_uri", FRONT_URL);
        params.add("code", code);
        params.add("client_secret", kakaoClientSecret);

        

        System.out.println("client_id" + kakaoClientId);
        System.out.println("redirect_url"+ FRONT_URL);
        System.out.println("auth 코드"+ code);
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =
                new HttpEntity<>(params, headers);

        // kakao에 request를 보내고 ResponseEntity로 답을 받는다.
        System.out.println("kakaoTokenRequest =>"+ kakaoTokenRequest);
        ResponseEntity<String> accessTokenResponse = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        System.out.println("accessToekn수신완료");

        // Json응답을 객체로 변환
        ObjectMapper objectMapper = new ObjectMapper();
        OauthToken oauthToken = null;
        try{
            oauthToken = objectMapper.readValue(accessTokenResponse.getBody(),OauthToken.class);
        } catch (JsonProcessingException e){
            e.printStackTrace();
        }

        return oauthToken;

    }


    public KakaoProfile findKakaoProfile(String token){

        RestTemplate rt = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        // Bearer 띄어쓰기 눈여겨볼것
        headers.add("Authorization","Bearer "+token);
        headers.add("Content-type","application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String,String>> kakaoProfileRequest = new HttpEntity<>(headers);

        // Post방식으로 요청해서 Response 객체를 받음
        ResponseEntity<String> kakaoProfileResponse = rt.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoProfileRequest,
                String.class
        );
        // Json응답을 객체로 변환
        ObjectMapper objectMapper = new ObjectMapper();
        KakaoProfile kakaoProfile = null;
        try{
            kakaoProfile = objectMapper.readValue(kakaoProfileResponse.getBody(),KakaoProfile.class);
        }catch (JsonProcessingException e){
            e.printStackTrace();
        }

        return kakaoProfile;

    }


    /**
     * summary 조회
     */
    public List<SummaryInfoDTO> findSummaryByUserId(Long userId){

        return pRconnectRepository.summaryFindByUserID(userId);

    }

    public ProfileViewResponseDto getUserProfileInfo(Long userId){

        Users targetUser = userRepository.findById(userId)
                .orElseThrow(()-> new IllegalArgumentException("해당 User가 없습니다. id = "+ userId));

        List<SummaryInfoDTO> targetUserSummary = findSummaryByUserId(userId);

        // total Count는 추후 수정
        return new ProfileViewResponseDto(userId,targetUser.getProfiles().getNickName(), (long) targetUser.getPosts().size(),targetUserSummary);

    }
}