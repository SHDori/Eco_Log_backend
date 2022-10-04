package Eco_Log.Eco_Log.domain.post;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "PRconnect")
public class PRconnect {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "prconnect_id")
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "posts_id",nullable = false)
    private Posts posts;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "behaviors_id",nullable = false)
    private Behaviors behaviors;

    //== 연관관계 편의 메서드==//
    public void setPost(Posts posts){
        this.posts = posts;
        posts.getPRconnects().add(this);
    }

    public void setBehavior(Behaviors behaviors){
        this.behaviors = behaviors;
        behaviors.getPRconnects().add(this);
    }


    //== 생성 메서드==//
    public static PRconnect createPRconnect(Posts posts,Behaviors behaviors){
        PRconnect pRconnect = new PRconnect();
        pRconnect.setPost(posts);
        pRconnect.setBehavior(behaviors);

        return pRconnect;
    }

    @Override
    public String toString() {
        return "PRconnect{" +
                "id=" + id +
                ", posts=" + posts +
                ", behaviors=" + behaviors.getName() +
                '}';
    }
}
