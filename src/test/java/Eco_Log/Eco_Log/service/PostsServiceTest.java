package Eco_Log.Eco_Log.service;

import Eco_Log.Eco_Log.controller.dto.PostListResponseDTO;
import Eco_Log.Eco_Log.controller.dto.PostSaveRequestDto;
import Eco_Log.Eco_Log.controller.dto.PostUpdateRequestDto;
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
        userRepository.save(users);

        String doingDay = "2022-8-18";

        PostSaveRequestDto saveRequestDto = getSaveRequestDto(doingDay);

        // when
        Long postsId = postsService.save(users.getId(),saveRequestDto);

        // then
        Users getUsers = userRepository.findById(users.getId())
                .orElseThrow(()-> new IllegalArgumentException("해당 게시물이 없습니다. id = "+ users.getId()));
        Posts getPosts = postsRepository.findById(postsId)
                .orElseThrow(()-> new IllegalArgumentException("해당 게시물이 없습니다. id = "+postsId));

        System.out.println(getPosts.getDoingList());
        System.out.println(getUsers.toString());
        System.out.println(getPosts);

        Assert.assertEquals("User의 정보가 같아야한다.", users.getName(), getUsers.getName());
        Assert.assertEquals("게시글이 잘 저장되어야한다.",saveRequestDto.getComment(),getPosts.getComment());
        Assert.assertEquals("게시글에 유저정보가 잘 들어가있어야한다.",getPosts.getUsers().getId(), getUsers.getId());


    }

    @Test
    public void 게시글_수정(){
        //given
        Users users = createUser();
        userRepository.save(users);
        String doingDay = "2022-8-18";
        PostSaveRequestDto saveRequestDto = getSaveRequestDto(doingDay);
        Long targetPostId = postsService.save(users.getId(),saveRequestDto);

        String chagedComment ="이건 수정된 말이야!";
        List<String> changedDoingList = new ArrayList<>();
        changedDoingList.add("변경된 분리수거");
        changedDoingList.add("변경된 장바구니쓰기");
        changedDoingList.add("변경된 전기아끼기");
        PostUpdateRequestDto updateRequestDto = PostUpdateRequestDto.builder()
                .postId(targetPostId)
                .comment(chagedComment)
                .doingList(changedDoingList)
                .build();
        // when
        postsService.update(users.getId(), updateRequestDto);

        // then
        Posts getPosts = postsRepository.findById(targetPostId)
                .orElseThrow(()-> new IllegalArgumentException("해당 게시물이 없습니다. id = "+targetPostId));

        Assert.assertEquals("Comment가 잘 바뀌어 있어야한다.", chagedComment, getPosts.getComment());
    }


    @Test
    public void 월단위_게시물_조회(){

        //given
        Users users = createUser();
        userRepository.save(users);

        for (int i=1;i<=10;i++){
            String doingDay = "2022-8-"+i;
            PostSaveRequestDto saveRequestDto = getSaveRequestDto(doingDay);
            postsService.save(users.getId(), saveRequestDto);
        }
        for (int i=1;i<=5;i++){
            String doingDay = "2022-7-"+i;
            PostSaveRequestDto saveRequestDto = getSaveRequestDto(doingDay);
            postsService.save(users.getId(), saveRequestDto);
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


        List<String> doingList = new ArrayList<>();
        doingList.add("분리수거");
        doingList.add("장바구니쓰기");
        doingList.add("전기아끼기");
        PostSaveRequestDto saveRequestDto = PostSaveRequestDto
                .builder()
                .doingDay(doingDay)
                .doingList(doingList)
                .comment(comment)
                .build();
        return saveRequestDto;
    }

    private Users createUser() {
        Users users = new Users();
        users.setName("회원1");
        return users;
    }

}