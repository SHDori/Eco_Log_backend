package Eco_Log.Eco_Log.service;

import Eco_Log.Eco_Log.controller.dto.ProfileDto;
import Eco_Log.Eco_Log.controller.dto.ProfileUpdateRequestDto;
import Eco_Log.Eco_Log.controller.dto.UserSearchResponseDto;
import Eco_Log.Eco_Log.controller.dto.postDto.PostSaveRequestDto;
import Eco_Log.Eco_Log.domain.post.Behaviors;
import Eco_Log.Eco_Log.domain.user.Users;
import Eco_Log.Eco_Log.repository.BehaviorRepository;
import Eco_Log.Eco_Log.repository.UserRepository;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class UserServieTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserServie userServie;
    @Autowired
    PostsService postsService;

    @Autowired
    ProfileService profileService;


    @Autowired
    BehaviorRepository behaviorRepository;

    @Autowired
    FollowService followService;
    @After
    public void cleanup(){
        userRepository.deleteAll();
    }


    @Test
    public void 유저_저장(){
        //given
        Long snsID = 1L;
        String profileImg = "임의의 이미지주소";

        String email = "narasarang@naver.com";
        ProfileDto profileDto = ProfileDto.builder()
                .snsId(snsID)
                .profileImg(profileImg)
                .email(email)

                .build();
        //when
        Users savedUser = userServie.save("김승환",profileDto);


        //then
        Users getUser = userRepository.findById(savedUser.getId())
                .orElseThrow(()-> new IllegalArgumentException("해당 게시물이 없습니다. id = "+ savedUser.getId()));
        Users emailTestUser = userRepository.findByEmail(email);



        Assert.assertEquals("User의 이름이 잘 들어갔는가", "김승환", getUser.getName());
        Assert.assertEquals("User의 id가 잘 들어갔는가", savedUser.getId(), getUser.getProfiles().getUser().getId());
        Assert.assertEquals("Profile에 정보가 잘 들어갔는지", email, getUser.getProfiles().getEmail());

        Assert.assertEquals("User가 Profile의 email로 잘 조회 되는가", email, emailTestUser.getProfiles().getEmail());

        System.out.println("Summary정보=>"+getUser.getSummary().toString());
        System.out.println("실제 email=> "+email);
        System.out.println("조회된 user email"+emailTestUser.getProfiles().getEmail());
        System.out.println(getUser.getProfiles().getNickName());


    }


    @Test
    public void 유저_검색_이메일(){
        // given
        Users users1 = createUser(0L,"김승환","vw9801@naver.com");
        Users users2 = createUser(1L,"김강민","kgm1515@gmail.com");
        Users users3 = createUser(2L,"최지훈","cjh1515@naver.com");

        makeBehaviors();
        // 1이 2를팔로우
        // 1이 2를 검색시 이미 팔로우가 되어있어야함
        followService.makeFollowRelation(users1.getId(),users2.getId());
        PostSaveRequestDto saveRequestDto = getSaveRequestDto("2022-08-22");
        Long postsId = postsService.save(users2.getId(),saveRequestDto);
        // when

        List<UserSearchResponseDto> searchResult = userServie.getSearchUserList(users1.getId(),"1515");


        //then
        System.out.println(searchResult);
        Assert.assertEquals("1515의 검색결과는 2명이 나와야한다", 2, searchResult.size());



    }

    @Test
    public void 유저_검색_닉네임(){
        // given
        Users users1 = createUser(0L,"김승환","vw9801@naver.com");
        Users users2 = createUser(1L,"김강민","kgm1515@gmail.com");
        Users users3 = createUser(2L,"최지훈","cjh1515@naver.com");

        makeBehaviors();
        // 1이 2를팔로우
        // 1이 2를 검색시 이미 팔로우가 되어있어야함
        followService.makeFollowRelation(users1.getId(),users2.getId());
        PostSaveRequestDto saveRequestDto = getSaveRequestDto("2022-08-22");
        Long postsId = postsService.save(users2.getId(),saveRequestDto);

        String changedNickName = "지구방위대원";
        String changedSelfIntroduction="지구를 지키는 우리가 진정한 지구방위대!";

        ProfileUpdateRequestDto updateRequestDto =
                new ProfileUpdateRequestDto(changedNickName,changedSelfIntroduction,0);
        profileService.update(users2.getId(),updateRequestDto);

        ProfileUpdateRequestDto updateRequestDto2 =
                new ProfileUpdateRequestDto("지구방위본부_사령관",changedSelfIntroduction,1);
        profileService.update(users3.getId(),updateRequestDto2);

        // when

        List<UserSearchResponseDto> searchResult = userServie.getSearchUserList(users1.getId(),"지구방위");
        List<UserSearchResponseDto> searchResult2 = userServie.getSearchUserList(users1.getId(),"1515");

        //then
        System.out.println(searchResult);
        // 한명은 비공개로 하였으니 2명중 1명만 검색
        Assert.assertEquals("지구방위 검색결과는 1명이 나와야한다", 1, searchResult.size());
        Assert.assertEquals("지구방위 검색결과는 1명이 나와야한다", 1, searchResult2.size());


    }


    private Users createUser(Long snsId,String name,String email) {
        //given
        Long snsID = snsId;
        String profileImg = "임의의 이미지주소";

        ProfileDto profileDto = ProfileDto.builder()
                .snsId(snsID)
                .profileImg(profileImg)
                .email(email)

                .build();
        //when
        Users savedUser = userServie.save(name,profileDto);
        return savedUser;
    }

    public void makeBehaviors(){
        Behaviors behaviors = new Behaviors("친환경 상점 이용");
        behaviorRepository.save(behaviors);

        Behaviors behaviors1 = new Behaviors("중고거래");
        behaviorRepository.save(behaviors1);
        Behaviors behaviors2 = new Behaviors("기부와 나눔");
        behaviorRepository.save(behaviors2);

    }

    private PostSaveRequestDto getSaveRequestDto(String doingDay) {
        String comment ="오늘도 수고많았다";



        List<String> behaviorsList = new ArrayList<>();
        behaviorsList.add("1");
        behaviorsList.add("2");
        behaviorsList.add("3");
        List<String> customBehaviorsList = new ArrayList<>();
        PostSaveRequestDto saveRequestDto = PostSaveRequestDto
                .builder()
                .doingDay(doingDay)
                .behaviorList(behaviorsList)
                .comment(comment)
                .customizedBehaviors(customBehaviorsList)
                .build();
        return saveRequestDto;
    }
}