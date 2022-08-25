package Eco_Log.Eco_Log.domain.post;

import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PostsRepositoryTest {

    @Autowired
    PostsRepository postsRepository;

    @After
    public void cleanup(){
        postsRepository.deleteAll();
    }

    @Test
    public void savingPostAndRead(){

        // given
        String userId = "1";
        String comment ="오늘도 수고많았다";

        String doingDay = "2022-8-18";

        List<String> doingList = new ArrayList<>();
        doingList.add("분리수거");
        doingList.add("장바구니쓰기");
        doingList.add("전기아끼기");

        String DoList = String.join(".",doingList);
        System.out.println(DoList);

        postsRepository.save(Posts.builder()
                .userId(userId)
                .doingDay(doingDay)
                .doingList(doingList)
                .comment(comment)
                .build());

        //when
        List<Posts> postsList = postsRepository.findAll();


        //then
        Posts posts = postsList.get(0);
        Assertions.assertThat(posts.getComment()).isEqualTo(comment);
        Assertions.assertThat(posts.getDoingDay()).isEqualTo(doingDay);
        System.out.println(posts.getDoingList());


    }



}
