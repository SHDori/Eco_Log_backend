//package Eco_Log.Eco_Log.service;
//
//import Eco_Log.Eco_Log.controller.dto.ProfileDto;
//import Eco_Log.Eco_Log.domain.user.Users;
//import Eco_Log.Eco_Log.repository.UserRepository;
//import com.auth0.jwt.JWT;
//import com.auth0.jwt.algorithms.Algorithm;
//import org.junit.After;
//import org.junit.Assert;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.context.annotation.PropertySource;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.transaction.annotation.Transactional;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//@Transactional
//@PropertySource("classpath:secretKey.properties")
//public class JwtServiceTest {
//
//    @Autowired
//    UserRepository userRepository;
//
//    @Autowired
//    UserServie userServie;
//
//    @Autowired
//    JwtService jwtService;
//
//    @Value("${secretKey}")
//    private String secretKey;
//
//    @After
//    public void cleanup(){
//        userRepository.deleteAll();
//    }
//
//    @Test
//    public void JWT토큰_확인(){
//
//
//        //given
//
//        Long snsID = 1L;
//        String profileImg = "임의의 이미지주소";
//
//        String email = "narasarang@naver.com";
//        ProfileDto profileDto = ProfileDto.builder()
//                .snsId(snsID)
//                .profileImg(profileImg)
//                .email(email)
//
//                .build();
//        //when
//        Users savedUser = userServie.save("김승환",profileDto);
//
//        String jwtToken = jwtService.createToken(savedUser);
//
//        //then
//
//        Long userCode = JWT.require(Algorithm.HMAC512(secretKey)).build().verify(jwtToken)
//                .getClaim("id").asLong();
//        System.out.println( "secretKey = "+secretKey);
//
//        System.out.println( "jwt Token = "+jwtToken);
//        System.out.println("user의 id => "+savedUser.getId());
//        System.out.println("jwt속의 user id => "+ userCode);
//        Assert.assertEquals("JWT에 들어간 user id와 jwt 복호화후 user id는 같아야한다.",savedUser.getId(),userCode);
//
//    }
//
//}