package Eco_Log.Eco_Log.service;

import Eco_Log.Eco_Log.controller.dto.*;
import Eco_Log.Eco_Log.controller.dto.postDto.PostListResponseDTO;
import Eco_Log.Eco_Log.controller.dto.postDto.PostSaveRequestDto;
import Eco_Log.Eco_Log.controller.dto.postDto.PostUpdateRequestDto;
import Eco_Log.Eco_Log.domain.post.Behaviors;
import Eco_Log.Eco_Log.domain.post.Posts;
import Eco_Log.Eco_Log.domain.user.Users;
import Eco_Log.Eco_Log.repository.BehaviorRepository;
import Eco_Log.Eco_Log.repository.PRconnectRepository;
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

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class PostsServiceTest {

    @Autowired
    PostsRepository postsRepository;

    @Autowired
    UserServie userServie;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PostsService postsService;

    @Autowired
    BehaviorRepository behaviorRepository;
    @Autowired
    PRconnectRepository pRconnectRepository;

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

    }



    @Test
    public void 게시글_저장(){
        //given
        Users users = createUser();
        Users users1 = createUser_1();

        String doingDay = "2022-8-18";

        PostSaveRequestDto saveRequestDto = getSaveRequestDto(doingDay);
        PostSaveRequestDto saveRequestDto_1 = getSaveRequestDto_1(doingDay);
        PostSaveRequestDto saveRequestDto_2 = getSaveRequestDto_1(doingDay);
        // when
        Long postsId = postsService.save(users.getId(),saveRequestDto);
        Long postsId_1 = postsService.save(users.getId(),saveRequestDto_1);

        Long postsId_2 = postsService.save(users1.getId(),saveRequestDto_2);


        // then
        Users getUsers = userRepository.findById(users.getId())
                .orElseThrow(()-> new IllegalArgumentException("해당 게시물이 없습니다. id = "+ users.getId()));
        Posts getPosts = postsRepository.findById(postsId)
                .orElseThrow(()-> new IllegalArgumentException("해당 게시물이 없습니다. id = "+postsId));
        List<SummaryInfoDTO> userSummaryInfoList = pRconnectRepository.summaryFindByUserID(users.getId());
        List<SummaryInfoDTO> otherUserSummaryInfoList = pRconnectRepository.summaryFindByUserID(users1.getId());

        System.out.println("기존 user"+users);
        System.out.println("user summary정보=>");
        System.out.println(userSummaryInfoList);

        System.out.println("다른 user"+users1);
        System.out.println("anOther user summary정보=>");
        System.out.println(otherUserSummaryInfoList);

        System.out.println(getUsers.toString());
        System.out.println(getPosts);

        Assert.assertEquals("User의 정보가 같아야한다.", users.getName(), getUsers.getName());
        Assert.assertEquals("게시글이 잘 저장되어야한다.",saveRequestDto.getComment(),getPosts.getComment());
        Assert.assertEquals("게시글에 유저정보가 잘 들어가있어야한다.",getPosts.getUsers().getId(), getUsers.getId());

        Assert.assertEquals("User의 Summary정보의 길이는 3이어야한다.", 3, userSummaryInfoList.size());
    }



    @Test
    public void 게시글_수정(){
        //given
        Users users = createUser();


        String doingDay = "2022-8-18";
        PostSaveRequestDto saveRequestDto = getSaveRequestDto(doingDay);
        Long targetPostId = postsService.save(users.getId(),saveRequestDto);
        List<SummaryInfoDTO> userSummaryInfoList = pRconnectRepository.summaryFindByUserID(users.getId());
        System.out.println("user summary정보=>");
        System.out.println(userSummaryInfoList);

        String chagedComment ="이건 수정된 말이야!";

        List<String> changedBehaviorsList = new ArrayList<>();
        changedBehaviorsList.add("2");
        changedBehaviorsList.add("3");
        PostUpdateRequestDto updateRequestDto = PostUpdateRequestDto.builder()
                .postId(targetPostId)
                .comment(chagedComment)
                .behaviorList(changedBehaviorsList)
                .build();
        // when
        postsService.update(users.getId(), updateRequestDto);


        // then
        Posts getPosts = postsRepository.findById(targetPostId)
                .orElseThrow(()-> new IllegalArgumentException("해당 게시물이 없습니다. id = "+targetPostId));

        userSummaryInfoList = pRconnectRepository.summaryFindByUserID(users.getId());
        System.out.println("user summary정보=>");
        System.out.println(userSummaryInfoList);

        Assert.assertEquals("User의 Summary정보의 길이는 2이어야한다.", 2, userSummaryInfoList.size());
        Assert.assertEquals("Comment가 잘 바뀌어 있어야한다.", chagedComment, getPosts.getComment());

    }

    @Test
    public void 게시글_삭제_요약정보까지(){
        //given
        Users users = createUser();
        String doingDay = "2022-8-18";

        PostSaveRequestDto saveRequestDto = getSaveRequestDto(doingDay);
        PostSaveRequestDto saveRequestDto_1 = getSaveRequestDto_1(doingDay);
        // when
        Long postsId = postsService.save(users.getId(),saveRequestDto);
        Long postsId_1 = postsService.save(users.getId(),saveRequestDto_1);


        postsService.delete(users.getId(),postsId);

        // then
        Users getUsers = userRepository.findById(users.getId())
                .orElseThrow(()-> new IllegalArgumentException("해당 게시물이 없습니다. id = "+ users.getId()));

        List<SummaryInfoDTO> userSummaryInfoList = pRconnectRepository.summaryFindByUserID(users.getId());

        System.out.println("user summary정보=>");
        System.out.println(userSummaryInfoList);


        Assert.assertEquals("User의 Post수가 잘 줄어들어야한다",1,users.getPosts().size());
        Assert.assertEquals("Summary가 잘 줄어들어야한다",1,userSummaryInfoList.size());
    }


    @Test
    public void 월단위_게시물_조회(){

        //given
        Users users = createUser();
        userRepository.save(users);

        for (int i=1;i<=10;i++){
            String doingDay = "2022-8-"+i;
            PostSaveRequestDto saveRequestDto = getSaveRequestDto(doingDay);
            postsService.save(users.getId(),saveRequestDto);
        }
        for (int i=1;i<=5;i++){
            String doingDay = "2022-7-"+i;
            PostSaveRequestDto saveRequestDto = getSaveRequestDto(doingDay);
            postsService.save(users.getId(),saveRequestDto);
        }

        // when
        String targetMonth = "2022-8";
        List<PostListResponseDTO> findPostList = postsService.findPostByMonth(users.getId(), targetMonth);
        // then

        System.out.println(findPostList.size());
        Assert.assertEquals("8월 게시물 10개만 조회되어야 한다.", 10, findPostList.size());
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
    private PostSaveRequestDto getSaveRequestDto_1(String doingDay) {
        String comment ="오늘도 지구를 위하여!";



        List<String> behaviorsList = new ArrayList<>();

        behaviorsList.add("2");

        PostSaveRequestDto saveRequestDto = PostSaveRequestDto
                .builder()
                .behaviorList(behaviorsList)
                .doingDay(doingDay)

                .comment(comment)
                .build();
        return saveRequestDto;
    }



    private Users createUser() {
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
        return savedUser;
    }

    private Users createUser_1() {
        //given
        Long snsID = 2L;
        String profileImg = "임의의 이미지주소";

        String email = "zzang@naver.com";
        ProfileDto profileDto = ProfileDto.builder()
                .snsId(snsID)
                .profileImg(profileImg)
                .email(email)

                .build();
        //when
        Users savedUser = userServie.save("승도리",profileDto);
        return savedUser;
    }

}