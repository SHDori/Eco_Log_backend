package Eco_Log.Eco_Log.service;


import Eco_Log.Eco_Log.domain.jwt.JwtProperties;
import Eco_Log.Eco_Log.domain.user.Users;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.apache.commons.codec.binary.Base64;
import java.util.Date;


@Service
@Getter
@PropertySource("classpath:secretKey.properties")
public class JwtService {

    @Value("${secretKey}")
    private String secretKey;

    public String createToken(Users user){
        // user정보를 바탕으로 jwt토큰 생성후 반환
        String jwtToken = JWT.create()
                .withSubject(user.getProfiles().getEmail())
                .withExpiresAt(new Date(System.currentTimeMillis()+ JwtProperties.EXPIRATION_TIME))
                .withClaim("userId",user.getId())
                .withClaim("nickname",user.getProfiles().getNickName())
                .sign(Algorithm.HMAC512(secretKey));
        return jwtToken;
    }


}
