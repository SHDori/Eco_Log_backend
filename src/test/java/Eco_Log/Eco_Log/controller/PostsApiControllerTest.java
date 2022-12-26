//package Eco_Log.Eco_Log.controller;
//
//
//import Eco_Log.Eco_Log.controller.dto.postDto.PostSaveRequestDto;
//import Eco_Log.Eco_Log.domain.post.Posts;
//import Eco_Log.Eco_Log.repository.PostsRepository;
//import org.assertj.core.api.Assertions;
//import org.junit.After;
//import org.junit.Test;
//
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.web.client.TestRestTemplate;
//import org.springframework.boot.test.web.server.LocalServerPort;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//public class PostsApiControllerTest {
//
//    @LocalServerPort
//    private int port;
//
//    @Autowired
//    private TestRestTemplate restTemplate;
//
//    @Autowired
//    private PostsRepository postsRepository;
//
//    @After
//    public void tearDown() throws Exception{
//        postsRepository.deleteAll();
//    }
//
//    @Test
//    public void PostsPosting() throws Exception{
//
//        // given
//        String userId = "1";
//        String comment ="오늘도 수고많았다";
//
//        String doingDay = "2022-8-18";
//
//        List<String> doingList = new ArrayList<>();
//        doingList.add("분리수거");
//        doingList.add("장바구니쓰기");
//        doingList.add("전기아끼기");
//
//        PostSaveRequestDto saveRequestDto = PostSaveRequestDto
//                .builder()
//                .doingDay(doingDay)
//                .comment(comment)
//                .build();
//
//
//        String url = "http://localhost:"+port+"/api/post";
//
//        //when
//
//        ResponseEntity<Long> responseEntity = restTemplate.postForEntity(url, saveRequestDto,Long.class);
//
//        //then
//
//        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
//        Assertions.assertThat(responseEntity.getBody()).isGreaterThan(0L);
//
//        List<Posts> postsList = postsRepository.findAll();
//        Posts posts = postsList.get(0);
//        Assertions.assertThat(posts.getComment()).isEqualTo(comment);
//        Assertions.assertThat(posts.getDoingDay()).isEqualTo(doingDay);
//
//
//    }
//
//
//}
