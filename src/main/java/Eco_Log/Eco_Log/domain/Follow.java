package Eco_Log.Eco_Log.domain;


import Eco_Log.Eco_Log.domain.post.Posts;
import Eco_Log.Eco_Log.domain.user.Users;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "`Follow`")
@IdClass(Eco_Log.Eco_Log.domain.composite_key.FollowPK.class)
public class Follow {

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "follow_id")
//    private Long id;



    @Id
    @Column(name ="from_user",insertable = false,updatable = false)
    private Long fromUser;

    @Id
    @Column(name ="to_user",insertable = false,updatable = false)
    private Long toUser;

    //== 연관관계 편의 메서드==//
//    public void setFromToUser(Users fromUser, Users toUser){
//        this.toUser = toUser;
//        this.fromUser = fromUser;
//        toUser.getFollowers().add(this);
//        fromUser.getFollowingList().add(this);
//    }


    @Builder
    public Follow(Long fromUser, Long toUser) {
        this.fromUser = fromUser;
        this.toUser = toUser;
    }


    @Override
    public String toString() {
        return "Follow{" +
                "fromUser=" + fromUser +
                ", toUser=" + toUser +
                "팔로우 관계를 시작합니다)";
    }
}
