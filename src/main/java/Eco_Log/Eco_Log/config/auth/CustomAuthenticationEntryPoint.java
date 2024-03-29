package Eco_Log.Eco_Log.config.auth;

import Eco_Log.Eco_Log.domain.jwt.JwtProperties;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        System.out.println("Commence에 들어오긴하나");
        String exception = (String) request.getAttribute(JwtProperties.HEADER_STRING);
        String errorCode;
        System.out.println("exception이 있나?");
        System.out.println(exception);

        // 만료된토큰에 대해서만
        if(exception.equals("토큰이 만료되었습니다.")){
            System.out.println("토큰이 만료되었습니다.");
            errorCode = "토큰이 만료되었습니다.(TokenExpiredException)";
            setResponse(response,errorCode,0);
        }

        else if(exception.equals("잘못된 Signature입니다.")) {

            System.out.println("잘못된 Signature입니다. 여기까지오나?");
            errorCode = "잘못된 Signature입니다.(SignatureVerificationException)";
            setResponse(response, errorCode,1);
        }else if(exception.equals("유효하지 않은 토큰입니다.")) {
            System.out.println("유효하지 않은 토큰입니다.");
            errorCode = "유효하지 않은 토큰입니다.(JWTVerificationException)";
            setResponse(response, errorCode,2);
        }else if(exception.equals("잘못된 토큰입니다.")) {
            System.out.println("잘못된 토큰입니다.");
            errorCode = "잘못된 토큰입니다.(IllegalArgumentException)";
            setResponse(response, errorCode,3);
        }


    }

    private void setResponse(HttpServletResponse response, String errorCode,int errorNum) throws IOException{
        response.setContentType("application/json;charset=UTF-8");
        if(errorNum==0){
            // 토큰 만료일떄만 이렇게 보냄
            response.setStatus(444);
        }else{
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
        response.getWriter().println(JwtProperties.HEADER_STRING + " : "+errorCode);
    }
}
