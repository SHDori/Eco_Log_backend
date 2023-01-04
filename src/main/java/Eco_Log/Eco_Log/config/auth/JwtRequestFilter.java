package Eco_Log.Eco_Log.config.auth;

import Eco_Log.Eco_Log.config.exeception.JwtException;
import Eco_Log.Eco_Log.domain.jwt.JwtProperties;
import Eco_Log.Eco_Log.repository.UserRepository;
import Eco_Log.Eco_Log.service.JwtService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
@Component
@PropertySource("classpath:secretKey.properties")
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    UserRepository userRepository;

    // 이거문제 해결해야함
    @Value("SECRET_KEY")
    private String secretKey = "SECRET_KEY";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response
            , FilterChain filterChain) throws ServletException, IOException{

        String jwtHeader = ((HttpServletRequest)request).getHeader(JwtProperties.HEADER_STRING);

        // Header가 비어있거나 정해진형식(Bearer) 가 아니면 아무것도 하지않게 return
        if(jwtHeader == null || !jwtHeader.startsWith(JwtProperties.TOKEN_PREFIX)){
            filterChain.doFilter(request,response);
            return;
        }

        String token = jwtHeader.replace(JwtProperties.TOKEN_PREFIX,"");

        // user의 id값
        Long userId = null;
        System.out.println("secretKey => "+secretKey);

        try {
            System.out.println("============>이거보이나? 1");
            userId = JWT.require(Algorithm.HMAC512(secretKey)).build().verify(token)
                        .getClaim("userId").asLong();

            System.out.println("============>이거보이나? 2");
        } catch (TokenExpiredException e){
            e.printStackTrace(); // 401 프론트에 날리자
            request.setAttribute(JwtProperties.HEADER_STRING,"토큰이 만료되었습니다.");
            System.out.println("============>이거보이나? 3");
            throw new JwtException("토큰이 만료되었습니다.");
        }catch (SignatureVerificationException e) {
            e.printStackTrace();
            request.setAttribute(JwtProperties.HEADER_STRING, "잘못된 Signature입니다.");
            System.out.println("============>이거보이나? 4");
            throw new JwtException("잘못된 Signature입니다.");
        } catch (JWTVerificationException e){
            e.printStackTrace();
            request.setAttribute(JwtProperties.HEADER_STRING,"유효하지 않은 토큰입니다.");
            System.out.println("============>이거보이나? 5");
            throw new JwtException("유효하지 않은 토큰입니다.");
        }catch (IllegalArgumentException e) {
            e.printStackTrace();
            request.setAttribute(JwtProperties.HEADER_STRING, "잘못된 토큰입니다.");
            System.out.println("============>이거보이나? 6");
            throw new JwtException("잘못된 토큰입니다.");
        }catch (Exception e){
            e.printStackTrace();
            request.setAttribute(JwtProperties.HEADER_STRING, "잘못된 토큰입니다.");
            System.out.println("============>이거보이나? 7");

            System.out.println(e.getMessage());
            throw new JwtException("잘못된 토큰입니다.");


        }

        request.setAttribute("userId",userId);

        filterChain.doFilter(request,response);

    }
}
