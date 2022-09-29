package Eco_Log.Eco_Log.config;



import Eco_Log.Eco_Log.config.auth.CustomAuthenticationEntryPoint;
import Eco_Log.Eco_Log.config.auth.JwtRequestFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CorsFilter corsFilter;

    //public static final String FRONT_URL = "http://localhost:8080/api/oauth/kakaotoken";
    public static final String FRONT_URL = "http://localhost:3000";

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{

        return http.csrf().ignoringAntMatchers("/h2-console/**")
                .disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .httpBasic().disable()
                .formLogin().disable()
                .addFilter(corsFilter)

                // UsernamePasswordAuthenticationFilter 직전에 JWT RequestFilter를 실행하기위해 addFilterBefor로 추가해준다.
                .addFilterBefore(new JwtRequestFilter(), UsernamePasswordAuthenticationFilter.class)


                .authorizeRequests()
                ///// 이부분 권한 설정 고민필요
                //.antMatchers(FRONT_URL+"/main/**")
                //.authenticated()
                .anyRequest().permitAll()
                //////
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                .and()
                // h2사용을 위한것 배포때는 없애고 ㄱㄱ
                .headers().frameOptions().disable()
                .and().build();




    }
}
