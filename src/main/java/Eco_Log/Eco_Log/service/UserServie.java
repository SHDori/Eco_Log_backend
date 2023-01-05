package Eco_Log.Eco_Log.service;


import Eco_Log.Eco_Log.controller.dto.*;
import Eco_Log.Eco_Log.domain.Follow;
import Eco_Log.Eco_Log.domain.user.Profiles;
import Eco_Log.Eco_Log.domain.user.Summary;
import Eco_Log.Eco_Log.domain.user.Users;
import Eco_Log.Eco_Log.domain.user.dto.KakaoProfile;
import Eco_Log.Eco_Log.domain.user.dto.NaverProfile;
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
import java.util.ArrayList;
import java.util.List;

import static Eco_Log.Eco_Log.config.SecurityConfig.FRONT_URL;


@RequiredArgsConstructor
@Service
@PropertySource("classpath:oauthInfo.properties")
public class UserServie {


    private final UserRepository userRepository;

    private final PRconnectRepository pRconnectRepository;

    private final JwtService jwtService;

    private final PostsService postsService;

    private final FollowService followService;

    @Value("${kakaoClientId}")
    private String kakaoClientId;

    @Value("${kakaoClientSecret}")
    private String kakaoClientSecret;

    @Value("${naverClientId}")
    private String naverClientId;

    @Value("${naverClientSecret}")
    private String naverClientSecret;




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


    public String saveNaverUserAndGetJwtToken(String token){

        NaverProfile profile = findNaverProfile(token);

        // 2.프로필 정보를 기반으로 기존에있던 user인지 신규user인지 판단

        Users user = userRepository.findByEmail(profile.getResponse().getEmail());

        if(user == null){
            ProfileDto profileDto = ProfileDto.builder()
                    .snsId(0L)
                    .profileImg(profile.getResponse().getProfile_image())
                    .email(profile.getResponse().getEmail())
                    .build();

            // naver 이름과 Profile정보를 넣어서 보내줌
            user = save(profile.getResponse().getNickname(),profileDto);
        }

        return jwtService.createToken(user);

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
    @Transactional(readOnly = true)
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

    public OauthToken getNaverAccessToken(String code,String state){
        RestTemplate rt = new RestTemplate();

        // Header생성
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        LinkedMultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.set("grant_type", "authorization_code");
        params.set("client_id", naverClientId);
        params.set("client_secret",naverClientSecret);
        params.set("code",code);
        params.set("state",state);

        HttpEntity<MultiValueMap<String, String>> naverTokenRequest = new HttpEntity<>(params, headers);
        ResponseEntity<String> accessTokenResponse = rt.exchange(
                "https://nid.naver.com/oauth2.0/token",
                HttpMethod.POST,
                naverTokenRequest,
                String.class
        );
        System.out.println("Naver accessToekn수신완료");
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

    public NaverProfile findNaverProfile(String token){

        RestTemplate rt = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        // Bearer 띄어쓰기 눈여겨볼것
        headers.add("Authorization","Bearer "+token);
        headers.add("Content-type","application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String,String>> naverProfileRequest = new HttpEntity<>(headers);

        // Post방식으로 요청해서 Response 객체를 받음
        ResponseEntity<String> naverProfileResponse = rt.exchange(
                "https://openapi.naver.com/v1/nid/me",
                HttpMethod.POST,
                naverProfileRequest,
                String.class
        );

        // Json응답을 객체로 변환
        ObjectMapper objectMapper = new ObjectMapper();
        NaverProfile naverProfile = null;

        try{
            naverProfile = objectMapper.readValue(naverProfileResponse.getBody(),NaverProfile.class);
        }catch (JsonProcessingException e){
            e.printStackTrace();
        }

        return naverProfile;

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
    @Transactional(readOnly = true)
    public List<SummaryInfoDTO> findSummaryByUserId(Long userId){

        return pRconnectRepository.summaryFindByUserID(userId);

    }

    /**
     * User Badge조회
     */
    @Transactional(readOnly = true)
    public UserBadgeResponseDto getUserBadgeState(Long userId){

        Users targetUser = userRepository.findById(userId)
                .orElseThrow(()-> new IllegalArgumentException("해당 User가 없습니다. id = "+ userId));

        String badgeState = targetUser.getBadgeState();
        List<Character> badgeStateList = new ArrayList<>();

        for(int i=0;i<badgeState.length();i++){
            badgeStateList.add(badgeState.charAt(i));
        }

        return new UserBadgeResponseDto(userId,badgeStateList);

    }


    /**
     * 유저 프로필 조회
     * => 내가 해당 유저를 팔로우하는지 찾아서 넣어야한다.
     * @param targetUserId
     * @return
     */
    @Transactional(readOnly = true)
    public ProfileViewResponseDto getUserProfileInfo(Long requestUserId,Long targetUserId){

        Users targetUser = userRepository.findById(targetUserId)
                .orElseThrow(()-> new IllegalArgumentException("해당 User가 없습니다. id = "+ targetUserId));

        List<SummaryInfoDTO> targetUserSummary = findSummaryByUserId(targetUserId);
        // 유저의정보를넣었다.
        ProfileViewResponseDto resultProfileDto = new ProfileViewResponseDto(targetUserId,targetUser.getProfiles().getNickName(), (long) targetUser.getPosts().size(),targetUserSummary,targetUser.getProfiles().getSelfIntroduce(),targetUser.getProfiles().isPublic(),targetUser.getCreatedDate(),targetUser.getProfiles().getBehaviorCount(),targetUser.getProfiles().getCustomBehaviorCount());
        // total Count는 추후 수정

        // 만약 내 프로필이라면
        if(requestUserId == targetUserId) {
            resultProfileDto.setMyProfile(true);
        }else {
            //내 프로필이 아니라면 targetuser가 내가 follower한사람인지 체크
            boolean isFollow = followService.isFallowCheck(requestUserId,targetUserId);
            resultProfileDto.setAlreadyFollow(isFollow);
        }
        resultProfileDto.setRecentlyCustomBehaviorList(postsService.getCustomBehaviorList());
        //
        return  resultProfileDto;

    }

    /**
     * 유저 검색
     * => 유저 검색을 email이나 닉네임으로 하고
     *    유저의 닉네임, 자기소개, 팔로우여부, 자주 기록한 실천을 반환
     */
    @Transactional(readOnly = true)
    public List<UserSearchResponseDto> getSearchUserList(Long requestUserId,String word){
        List<UserSearchResponseDto> searchResultUserList = new ArrayList<>();
        //1. 넘겨온 값으로 모든 유저를 검색한다.
        List<Users> serchedUserList = userRepository.findUsersBySearchWord(word);
        //2. 유저리스트를 돈다

        for(Users targetUser : serchedUserList){
            Long targetUserId = targetUser.getId();
            //3. 돌며 각각의 유저 summary를 넣어준다.
            List<SummaryInfoDTO> targetUserSummary = findSummaryByUserId(targetUserId);
            UserSearchResponseDto searchResponseDto = new UserSearchResponseDto(targetUser.getId(), targetUser.getProfiles().getNickName(),targetUser.getProfiles().getSelfIntroduce(),targetUserSummary);
            //4. 각각의 유저가 팔로우이미 한사람인지 체크한다.
            boolean isFollow = followService.isFallowCheck(requestUserId,targetUserId);
            searchResponseDto.setAlreadyFollow(isFollow);
            searchResultUserList.add(searchResponseDto);
        }


        //5. 반환한다

        return searchResultUserList;
    }
}