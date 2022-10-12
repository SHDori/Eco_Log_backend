package Eco_Log.Eco_Log.domain;


import Eco_Log.Eco_Log.domain.post.Posts;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "heartTable")
public class Heart {


    /**
     * 각각 Post랑 User에 연관관계를 가져갈것이냐
     * follow 테이블처럼 그냥 두 아이디값만 가지고 운용할것이냐 이것이 고민이다
     *
     * 일단 지금생각은 Post에는 List로 포함시키고
     * user는 그냥 id만 보관하려하는데 고민좀해봐야겠다.
     * => 이 방식대로가자.
     * => Post에는 연관관계를 맺어놓으면 하트 표시할때 좋지만
     *    user는 딱히 맺을필요가 안보인다
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "heart_id")
    private Long id;

    @Column(name = "from_user")
    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "posts_id",nullable = false)
    @Column(name = "to_post")
    private Posts posts;

    //== 연관관계 편의 메서드==//
    public void setPost(Posts posts){
        this.posts = posts;
        posts.getHearts().add(this);
    }

    //== 생성 메서드==//
    public static Heart createHeart(Posts posts){
        Heart heart = new Heart();
        heart.setPost(posts);

        return heart;
    }







}
