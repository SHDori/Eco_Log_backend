package Eco_Log.Eco_Log.config.auth;

import Eco_Log.Eco_Log.domain.jwt.JwtProperties;
import Eco_Log.Eco_Log.repository.UserRepository;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
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

    @Value("${secretKey}")
    private String secretKey;

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
        Long userCode = null;

        try {
                userCode = JWT.require(Algorithm.HMAC512(secretKey)).build().verify(token)
                        .getClaim("id").asLong();
        } catch (TokenExpiredException e){
            e.printStackTrace();
            request.setAttribute(JwtProperties.HEADER_STRING,"토큰이 만료되었습니다.");
        } catch (JWTVerificationException e){
            e.printStackTrace();
            request.setAttribute(JwtProperties.HEADER_STRING,"유효하지 않은 토큰입니다.");
        }

        request.setAttribute("userCode",userCode);

        filterChain.doFilter(request,response);

    }
}
