package Eco_Log.Eco_Log.domain.user;

import Eco_Log.Eco_Log.domain.BaseTimeEntity;
import Eco_Log.Eco_Log.domain.user.randomNickName.RandomNicknamaMaker;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "user_profile")
public class Profiles extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "profile_id")
    private Long id;


    @Column(name = "sns_id")
    private Long snsId;

    @Column(name = "user_profileImg")
    private String profileImg;
    @Column(name = "user_nickname")
    private String nickName;
    @Column(name = "sns_email")
    private String email;
    @Column(name = "selfIntroduce")
    private String selfIntroduce;

    @Column(name = "user_role")
    private String userRole;

    @OneToOne(mappedBy = "profiles",fetch = FetchType.LAZY)
    private Users user;

    @Builder
    public Profiles(Long snsId, String profileImg, String email, String selfIntroduce,String userRole){
        this.snsId = snsId;
        this.profileImg = profileImg;
        this.email = email;
        this.selfIntroduce = selfIntroduce;
        this.userRole = userRole;

        /**
         * 랜덤 nick name생성
         */
        RandomNicknamaMaker randomNicknamaMaker=new RandomNicknamaMaker();
        this.nickName = randomNicknamaMaker.getRandomNickName();

    }

}
