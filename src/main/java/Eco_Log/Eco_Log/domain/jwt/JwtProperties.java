package Eco_Log.Eco_Log.domain.jwt;



public interface JwtProperties {
    // 나중에 넣어줄 것
    String SECRET = "{}";
    int EXPIRATION_TIME =  864000000;
    String TOKEN_PREFIX = "Bearer ";
    String HEADER_STRING = "Authorization";
}
