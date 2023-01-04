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

        String exception = (String) request.getAttribute(JwtProperties.HEADER_STRING);
        String errorCode;

        if(exception.equals("토큰이 만료되었습니다.")){
            errorCode = "토큰이 만료되었습니다.(TokenExpiredException)";
            setResponse(response,errorCode);
        }

        else if(exception.equals("잘못된 Signature입니다.")) {
            errorCode = "잘못된 Signature입니다.(SignatureVerificationException)";
            setResponse(response, errorCode);
        }else if(exception.equals("유효하지 않은 토큰입니다.")) {
            errorCode = "유효하지 않은 토큰입니다.(JWTVerificationException)";
            setResponse(response, errorCode);
        }else if(exception.equals("잘못된 토큰입니다.")) {
            errorCode = "잘못된 토큰입니다.(IllegalArgumentException)";
            setResponse(response, errorCode);
        }


    }

    private void setResponse(HttpServletResponse response, String errorCode) throws IOException{
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().println(JwtProperties.HEADER_STRING + " : "+errorCode);
    }
}
