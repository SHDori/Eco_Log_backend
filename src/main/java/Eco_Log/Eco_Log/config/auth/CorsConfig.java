//package Eco_Log.Eco_Log.config.auth;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
//import org.springframework.web.filter.CorsFilter;
//
//@Configuration
//public class CorsConfig {
//
//    @Bean
//    public CorsFilter corsFilter(){
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        CorsConfiguration corsConfig = new CorsConfiguration();
//        corsConfig.setAllowCredentials(true);
//        corsConfig.addAllowedOriginPattern("*");
//        corsConfig.addAllowedHeader("*");
//        corsConfig.addExposedHeader("*");
//        corsConfig.addAllowedMethod("*");
//        source.registerCorsConfiguration("/api/**",corsConfig);
//        return new CorsFilter(source);
//    }
//}
