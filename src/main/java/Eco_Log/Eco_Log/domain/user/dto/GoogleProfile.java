package Eco_Log.Eco_Log.domain.user.dto;


import lombok.Data;

@Data
public class GoogleProfile {

    public String id;
    public String name;
    public String given_name;
    public String family_name;
    public String email;
    public String locale;
    public boolean verified_email;

    public String picture;

}
