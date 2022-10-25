package Eco_Log.Eco_Log.service;

import Eco_Log.Eco_Log.controller.dto.ProfileDto;
import Eco_Log.Eco_Log.controller.dto.ProfileUpdateRequestDto;
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


@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class ProfileServiceTest {

    @Autowired
    UserServie userServie;
    @Autowired
    UserRepository userRepository;

    @Autowired
    ProfileService profileService;




    @After
    public void cleanup(){
        userRepository.deleteAll();

    }


    @Test
    public void 프로필_수정(){

        //givne
        Users users = createUser();

        String changedNickName = "지구방위대";
        String changedSelfIntroduction="지구를 지키는 우리가 진정한 지구방위대!";

        ProfileUpdateRequestDto updateRequestDto =
                new ProfileUpdateRequestDto(changedNickName,changedSelfIntroduction,0);

        // when

        profileService.update(users.getId(),updateRequestDto);

        //then
        Users getUsers = userRepository.findById(users.getId())
                .orElseThrow(()-> new IllegalArgumentException("해당 게시물이 없습니다. id = "+ users.getId()));


        Assert.assertEquals("닉네임이 잘 변경되어야한다.", changedNickName, getUsers.getProfiles().getNickName());

        Assert.assertEquals("자기소개가 잘 변경되어야한다.", changedSelfIntroduction, getUsers.getProfiles().getSelfIntroduce());
        Assert.assertEquals("공개여부가 잘 변경되어야한다.",false,getUsers.getProfiles().isPublic());

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
