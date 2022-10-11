package Eco_Log.Eco_Log.service;

import Eco_Log.Eco_Log.controller.dto.ProfileDto;
import Eco_Log.Eco_Log.domain.Follow;
import Eco_Log.Eco_Log.domain.user.Users;
import Eco_Log.Eco_Log.repository.FollowRepository;
import Eco_Log.Eco_Log.repository.UserRepository;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class FollowServiceTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserServie userServie;

    @Autowired
    FollowRepository followRepository;

    @Autowired
    FollowService followService;

    @After
    public void cleanup(){
        followRepository.deleteAll();
        userRepository.deleteAll();

    }

    @Test
    public void 팔로우_맺기(){

        // given
        Users users1 = createUser(0L,"김승환");
        System.out.println("user1의 id=>"+users1.getId());
        Users users2 = createUser(1L,"김강민");
        System.out.println("user2의 id=>"+users2.getId());
        Users users3 = createUser(2L,"최지훈");
        System.out.println("user3의 id=>"+users3.getId());



        // when
        // user1은 user2의 팔로워
        followService.makeFollowRelation(users1.getId(),users2.getId());
        followService.makeFollowRelation(users1.getId(),users3.getId());
        followService.makeFollowRelation(users3.getId(),users2.getId());

        //then

        List<Follow> user1FollowingList = followRepository.findMyFollowingByUserId(users1.getId());
        List<Long> user1FollowingUserIdList = followRepository.findMyFollowingUserIdByFromUserId(users1.getId());
        System.out.println(user1FollowingList);
        System.out.println(user1FollowingUserIdList);
        Assert.assertEquals("User1의 Follwing은 2명이어야한다",2, user1FollowingList.size());


    }

    @Test
    public void 팔로우_끊기(){

        // given
        // given
        Users users1 = createUser(0L,"김승환");
        Users users2 = createUser(1L,"김강민");
        Users users3 = createUser(2L,"최지훈");

        // user1은 user2의 팔로워
        followService.makeFollowRelation(users1.getId(),users2.getId());
        followService.makeFollowRelation(users1.getId(),users3.getId());
        followService.makeFollowRelation(users3.getId(),users2.getId());


        // when

        followService.deleteFollowRelation(users1.getId(),users2.getId());

        //then

        List<Follow> user1FollowingList = followRepository.findMyFollowingByUserId(users1.getId());
        System.out.println(user1FollowingList);
        Assert.assertEquals("User1의 Follwing은 1명이어야한다",1, user1FollowingList.size());


    }




    private Users createUser(Long snsId,String name) {
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