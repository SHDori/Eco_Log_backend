package Eco_Log.Eco_Log.service;

import Eco_Log.Eco_Log.controller.dto.ProfileDto;
import Eco_Log.Eco_Log.controller.dto.postDto.PostSaveRequestDto;
import Eco_Log.Eco_Log.domain.Heart;
import Eco_Log.Eco_Log.domain.post.Behaviors;
import Eco_Log.Eco_Log.domain.post.Posts;
import Eco_Log.Eco_Log.domain.user.Users;
import Eco_Log.Eco_Log.repository.BehaviorRepository;
import Eco_Log.Eco_Log.repository.HeartRepository;
import Eco_Log.Eco_Log.repository.PostsRepository;
import Eco_Log.Eco_Log.repository.UserRepository;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
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
public class HeartServiceTest {

    @Autowired
    PostsService postsService;

    @Autowired
    PostsRepository postsRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserServie userServie;

    @Autowired
    HeartService heartService;


    @Autowired
    BehaviorRepository behaviorRepository;

    @Before
    public void makeBehaviors(){
        Behaviors behaviors = new Behaviors("친환경 상점 이용");
        behaviorRepository.save(behaviors);

        Behaviors behaviors1 = new Behaviors("중고거래");
        behaviorRepository.save(behaviors1);
        Behaviors behaviors2 = new Behaviors("기부와 나눔");
        behaviorRepository.save(behaviors2);

    }

    @After
    public void cleanup(){
        postsRepository.deleteAll();
        userRepository.deleteAll();
        //heartRepository.deleteAll();

    }

    @Test
    public void 게시물_하트누르기(){

        // given
        Users user1 = createCustomUser(0L,"슬라임");
        Users user2 = createCustomUser(1L, "핑크빈");

        String doingDay = "2022-8-18";
        PostSaveRequestDto saveRequestDto = getSaveRequestDto(doingDay);
        Long postId = postsService.save(user1.getId(),saveRequestDto);


        // when
        heartService.makeHeartInfo(user1.getId(),postId);
        heartService.makeHeartInfo(user2.getId(),postId);


        // then
        Posts testPost = postsRepository.findById(postId)
                .orElseThrow(()-> new IllegalArgumentException("해당 Post가 없습니다. id = "+ postId));
        List<Heart> heartList = heartService.findAllHeartByPostId(testPost.getId());

        boolean user1IsPushHeart = heartService.isHeartPushCheck(user1.getId(),testPost.getId());
        boolean user2IsPushHeart = heartService.isHeartPushCheck(user2.getId(),testPost.getId());
        Assert.assertEquals(testPost.getId()+"번게시물 하트의 수가 2여야한다", 2,testPost.getHearts().size());
        Assert.assertEquals("하트테이블에"+testPost.getId()+"번게시물 의 하트가2여야한다", 2,heartList.size());
        Assert.assertEquals("user1은 "+testPost.getId()+"번 게시물에 하트를 눌렀다", true,user1IsPushHeart);
        Assert.assertEquals("user2은 "+testPost.getId()+"번 게시물에 하트를 눌렀다", true,user2IsPushHeart);
    }

    @Test
    public void 게시물_하트취소하기(){
        // given
        Users user1 = createCustomUser(0L,"슬라임");
        Users user2 = createCustomUser(1L, "핑크빈");

        String doingDay = "2022-8-18";
        PostSaveRequestDto saveRequestDto = getSaveRequestDto(doingDay);
        Long postId = postsService.save(user1.getId(),saveRequestDto);
        heartService.makeHeartInfo(user1.getId(),postId);
        heartService.makeHeartInfo(user2.getId(),postId);

        // when
        heartService.cancelHeart(user2.getId(),postId);


        // then
        Posts testPost = postsRepository.findById(postId)
                .orElseThrow(()-> new IllegalArgumentException("해당 Post가 없습니다. id = "+ postId));
        List<Heart> heartList = heartService.findAllHeartByPostId(testPost.getId());
        boolean user1IsPushHeart = heartService.isHeartPushCheck(user1.getId(),testPost.getId());
        boolean user2IsPushHeart = heartService.isHeartPushCheck(user2.getId(),testPost.getId());

        Assert.assertEquals(testPost.getId()+"번게시물 하트의 수가 1여야한다", 1,testPost.getHearts().size());
        Assert.assertEquals("하트테이블에"+testPost.getId()+"번게시물 의 하트가 1여야한다", 1,heartList.size());
        Assert.assertEquals("user1은 "+testPost.getId()+"번 게시물에 하트를 눌렀다", true,user1IsPushHeart);
        Assert.assertEquals("user2은 "+testPost.getId()+"번 게시물에 하트를 취소했다", false,user2IsPushHeart);

    }

    @Test
    public void 게시물삭제시_하트테이블(){

        // given
        Users user1 = createCustomUser(0L,"슬라임");
        Users user2 = createCustomUser(1L, "핑크빈");

        String doingDay = "2022-8-18";
        PostSaveRequestDto saveRequestDto = getSaveRequestDto(doingDay);
        Long postId = postsService.save(user1.getId(),saveRequestDto);
        heartService.makeHeartInfo(user1.getId(),postId);
        heartService.makeHeartInfo(user2.getId(),postId);
        List<Heart> pre_heartList = heartService.findAllHeartByPostId(postId);
        System.out.println("삭제전 하트테이블의 해당게시물에대한 하트 수 => "+pre_heartList.size());
        //when
        postsService.delete(user1.getId(),postId);

        //then
        List<Heart> heartList = heartService.findAllHeartByPostId(postId);
        Assert.assertEquals("삭제된 "+postId+"번 게시물에 관한 하트정보는 있으면 안된다.", 0,heartList.size());


    }






    private PostSaveRequestDto getSaveRequestDto(String doingDay) {
        String comment ="오늘도 수고많았다";



        List<String> behaviorsList = new ArrayList<>();
        behaviorsList.add("1");
        behaviorsList.add("2");
        behaviorsList.add("3");
        PostSaveRequestDto saveRequestDto = PostSaveRequestDto
                .builder()
                .doingDay(doingDay)
                .behaviorList(behaviorsList)
                .comment(comment)
                .build();
        return saveRequestDto;
    }

    private Users createCustomUser(Long snsId, String name) {
        //given
        Long snsID = snsId;
        String profileImg = "임의의 이미지주소";

        String email = "narasarang@naver.com";
        ProfileDto profileDto = ProfileDto.builder()
                .snsId(snsID)
                .profileImg(profileImg)
                .email(email)

                .build();
        //when
        Users savedUser = userServie.save(name,profileDto);
        return savedUser;
    }

}