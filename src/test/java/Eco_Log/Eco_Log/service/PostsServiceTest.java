package Eco_Log.Eco_Log.service;

import Eco_Log.Eco_Log.controller.dto.PostListResponseDTO;
import Eco_Log.Eco_Log.controller.dto.PostSaveRequestDto;
import Eco_Log.Eco_Log.controller.dto.PostUpdateRequestDto;
import Eco_Log.Eco_Log.controller.dto.ProfileDto;
import Eco_Log.Eco_Log.domain.post.Posts;
import Eco_Log.Eco_Log.domain.user.Users;
import Eco_Log.Eco_Log.repository.PostsRepository;
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

    @After
    public void cleanup(){
        postsRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void 게시글_저장(){
        //given
        Users users = createUser();


        String doingDay = "2022-8-18";

        PostSaveRequestDto saveRequestDto = getSaveRequestDto(doingDay,users.getId());

        // when
        Long postsId = postsService.save(saveRequestDto);

        // then
        Users getUsers = userRepository.findById(users.getId())
                .orElseThrow(()-> new IllegalArgumentException("해당 게시물이 없습니다. id = "+ users.getId()));
        Posts getPosts = postsRepository.findById(postsId)
                .orElseThrow(()-> new IllegalArgumentException("해당 게시물이 없습니다. id = "+postsId));
        System.out.print("Post에서 getDoingList를 하면 =>");
        System.out.println(getPosts.getDoingList());
        System.out.println(getUsers.toString());
        System.out.println(getPosts);

        Assert.assertEquals("User의 정보가 같아야한다.", users.getName(), getUsers.getName());
        Assert.assertEquals("게시글이 잘 저장되어야한다.",saveRequestDto.getComment(),getPosts.getComment());
        Assert.assertEquals("게시글에 유저정보가 잘 들어가있어야한다.",getPosts.getUsers().getId(), getUsers.getId());


    }

    @Test
    public void 게시글_저장_요약정보까지(){
        //given
        Users users = createUser();


        String doingDay = "2022-8-18";

        PostSaveRequestDto saveRequestDto = getSaveRequestDto(doingDay,users.getId());

        // when
        Long postsId = postsService.save(saveRequestDto);

        // then
        Users getUsers = userRepository.findById(users.getId())
                .orElseThrow(()-> new IllegalArgumentException("해당 게시물이 없습니다. id = "+ users.getId()));
        Posts getPosts = postsRepository.findById(postsId)
                .orElseThrow(()-> new IllegalArgumentException("해당 게시물이 없습니다. id = "+postsId));

        System.out.print("User의 Summary => ");
        System.out.println(getUsers.getSummary().toString());

        Assert.assertEquals("User의 정보가 같아야한다.", users.getName(), getUsers.getName());
        Assert.assertEquals("User의 SummaryCount가 잘 올라가야한다",2,getUsers.getSummary().getCountOfConsumption());
    }

    @Test
    public void 게시글_수정(){
        //given
        Users users = createUser();


        String doingDay = "2022-8-18";
        PostSaveRequestDto saveRequestDto = getSaveRequestDto(doingDay,users.getId());
        Long targetPostId = postsService.save(saveRequestDto);
        System.out.print("User의 변경전 Summary => ");
        System.out.println(users.getSummary().toString());

        String chagedComment ="이건 수정된 말이야!";
        List<String> changedDoingList = new ArrayList<>();
        changedDoingList.add("한끼 채식");
        changedDoingList.add("배달음식 자제");
        changedDoingList.add("잔반 없음");
        changedDoingList.add("텀블러 소지");
        PostUpdateRequestDto updateRequestDto = PostUpdateRequestDto.builder()
                .postId(targetPostId)
                .comment(chagedComment)
                .doingList(changedDoingList)
                .build();
        // when
        postsService.update(users.getId(), updateRequestDto);

        System.out.print("User의 수정후 Summary => ");
        System.out.println(users.getSummary().toString());
        // then
        Posts getPosts = postsRepository.findById(targetPostId)
                .orElseThrow(()-> new IllegalArgumentException("해당 게시물이 없습니다. id = "+targetPostId));

        Assert.assertEquals("Comment가 잘 바뀌어 있어야한다.", chagedComment, getPosts.getComment());
        Assert.assertEquals("기존의 SummaryCount가 잘 내려가야한다",0,users.getSummary().getCountOfConsumption());
        Assert.assertEquals("User의 SummaryCount가 잘 올라가야한다",3,users.getSummary().getCountOfEat());
    }

    @Test
    public void 게시글_삭제_요약정보까지(){
        //given
        Users users = createUser();
        String doingDay = "2022-8-18";

        PostSaveRequestDto saveRequestDto = getSaveRequestDto(doingDay,users.getId());
        // when
        Long postsId = postsService.save(saveRequestDto);
        System.out.print("User의 삭제전 Summary => ");
        System.out.println(users.getSummary().toString());

        postsService.delete(users.getId(),postsId);

        // then
        Users getUsers = userRepository.findById(users.getId())
                .orElseThrow(()-> new IllegalArgumentException("해당 게시물이 없습니다. id = "+ users.getId()));

        System.out.print("User의 Summary => ");
        System.out.println(getUsers.getSummary().toString());

        Assert.assertEquals("User의 SummaryCount가 잘 내려가야한다",0,getUsers.getSummary().getCountOfConsumption());
    }


    @Test
    public void 월단위_게시물_조회(){

        //given
        Users users = createUser();
        userRepository.save(users);

        for (int i=1;i<=10;i++){
            String doingDay = "2022-8-"+i;
            PostSaveRequestDto saveRequestDto = getSaveRequestDto(doingDay,users.getId());
            postsService.save(saveRequestDto);
        }
        for (int i=1;i<=5;i++){
            String doingDay = "2022-7-"+i;
            PostSaveRequestDto saveRequestDto = getSaveRequestDto(doingDay,users.getId());
            postsService.save(saveRequestDto);
        }

        // when
        String targetMonth = "2022-8";
        List<PostListResponseDTO> findPostList = postsService.findPostByMonth(users.getId(), targetMonth);
        // then

        System.out.println(findPostList.size());
        Assert.assertEquals("8월 게시물 10개만 조회되어야 한다.", 10, findPostList.size());
    }



    private PostSaveRequestDto getSaveRequestDto(String doingDay,Long userId) {
        String comment ="오늘도 수고많았다";


        List<String> doingList = new ArrayList<>();
        doingList.add("친환경 상점 이용");
        doingList.add("소비 없는 하루");
        doingList.add("대중교통 이용");
        PostSaveRequestDto saveRequestDto = PostSaveRequestDto
                .builder()
                .userId(String.valueOf(userId))
                .doingDay(doingDay)
                .doingList(doingList)
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

}