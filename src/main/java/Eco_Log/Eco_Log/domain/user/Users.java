package Eco_Log.Eco_Log.domain.user;


import Eco_Log.Eco_Log.domain.BaseTimeEntity;
import Eco_Log.Eco_Log.domain.post.Posts;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "`Users`")
public class Users extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "users_id")
    private Long id;


    private String name;
    private UserRole userRole;

    @OneToMany(mappedBy = "users")
    private List<Posts> posts = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL )
    @JoinColumn(name = "profile_id")
    private Profiles profiles;
//
//    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL )
//    private Summary summary;
//
//    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//    @JoinColumn(name = "badge_id")
//    private Badge badge;


    @Override
    public String toString() {
        return "Users{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", posts=" + posts +
                '}';
    }

    //== 연관관계 편의 메서드 ==//
    public void setUserProfile(Profiles profile){
        this.profiles = profile;
        profile.setUser(this);
    }

    //== 생성 메서드==//
    public static Users createUser(String name,Profiles profiles){
        Users user = new Users();
        user.setName(name);
        user.setUserProfile(profiles);
        user.setUserRole(UserRole.NORMAL);
        return user;
    }
}
