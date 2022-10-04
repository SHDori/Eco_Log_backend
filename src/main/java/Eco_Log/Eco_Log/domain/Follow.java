//package Eco_Log.Eco_Log.domain;
//
//
//import Eco_Log.Eco_Log.domain.post.Posts;
//import Eco_Log.Eco_Log.domain.user.Users;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//
//import javax.persistence.*;
//
//@Getter
//@Setter
//@NoArgsConstructor
//@Entity
//@Table(name = "`follow`")
//public class Follow {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "follow_id")
//    private Long id;
//
//
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "users_id",nullable = false)
//    private Users fromUser;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "users_id",nullable = false)
//    private Users toUser;
//
//    //== 연관관계 편의 메서드==//
//    public void setFromToUser(Users fromUser, Users toUser){
//        this.toUser = toUser;
//        this.fromUser = fromUser;
//        toUser.getFollowerList().add(this);
//        fromUser.getFollowingList().add(this);
//    }
//
//
//
//    //== 생성 메서드==//
//    public static Follow createFollow(Users fromUser, Users toUser){
//        Follow follow = new Follow();
//        follow.setFromToUser(fromUser,toUser);
//        return follow;
//
//    }
//
//}
