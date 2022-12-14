package Eco_Log.Eco_Log.domain.user;


import Eco_Log.Eco_Log.domain.BaseTimeEntity;
import Eco_Log.Eco_Log.domain.Follow;
import Eco_Log.Eco_Log.domain.post.PRconnect;
import Eco_Log.Eco_Log.domain.post.Posts;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
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


    // sns이름
    private String name;
    private UserRole userRole;

    @OneToMany(mappedBy = "users")
    private List<Posts> posts = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL )
    @JoinColumn(name = "profile_id")
    private Profiles profiles;
//
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL )
    private Summary summary;

    private String recentRecordDate;
    private int continuousRecord;

    private String badgeState;

//    //////////// Follow 관련
////     내가 follow하는 사람들 => 내가 fromUser인경우
//    @OneToMany(mappedBy = "users")
//    private List<Follow> followingList = new ArrayList<>();
//
////    // 나를 follow하는 사람들 => 내가 toUser인경우
//    @OneToMany(mappedBy = "users")
//    private List<Follow> followers = new ArrayList<>();



    /////////////

//
//    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//    @JoinColumn(name = "badge_id")
//    private Badge badge;

    public void deletePost(Posts targetPost){
        this.posts.remove(targetPost);
    }

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

    public void setUserSummary(Summary summary){
        this.summary = summary;
        summary.setUser(this);
    }

    //== 생성 메서드==//
    public static Users createUser(String name,Profiles profiles,Summary summary){
        Users user = new Users();
        user.setName(name);
        user.setBadgeState("0000000000");
        user.setRecentRecordDate(String.valueOf(LocalDate.now()).substring(0,10));
        user.setContinuousRecord(0);
        ////////////////////////
        // 일단 임시로 이렇게
        System.out.println("---------------------------------------");

        user.setUserSummary(summary);
        System.out.println(summary);
        /////////////////////////
        user.setUserProfile(profiles);
        user.setUserRole(UserRole.NORMAL);
        return user;
    }
}
