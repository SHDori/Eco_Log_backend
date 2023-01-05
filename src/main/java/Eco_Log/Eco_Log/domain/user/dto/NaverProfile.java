package Eco_Log.Eco_Log.domain.user.dto;


import lombok.Data;

@Data
public class NaverProfile {
    public String resultcode;
    public String message;
    public ProfileInfo response;

    @Data
    public class ProfileInfo{
        public String id;
        public String nickname;
        public String name;
        public String email;
        public String gender;
        public String age;
        public String birthday;
        public String profile_image;
        public String birthyear;
        public String mobile;
    }

}
