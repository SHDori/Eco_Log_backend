package Eco_Log.Eco_Log.service;

import Eco_Log.Eco_Log.controller.dto.ProfileDto;
import Eco_Log.Eco_Log.domain.user.Users;
import Eco_Log.Eco_Log.repository.UserRepository;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;


@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class UserServieTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserServie userServie;

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
}