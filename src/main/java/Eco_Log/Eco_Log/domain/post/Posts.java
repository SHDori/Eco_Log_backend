package Eco_Log.Eco_Log.domain.post;


import Eco_Log.Eco_Log.controller.dto.postDto.PostSaveRequestDto;
import Eco_Log.Eco_Log.controller.dto.postDto.PostUpdateRequestDto;
import Eco_Log.Eco_Log.domain.BaseTimeEntity;
import Eco_Log.Eco_Log.domain.Heart;
import Eco_Log.Eco_Log.domain.user.Users;
import Eco_Log.Eco_Log.tool.StringListConverter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "Posts")
public class Posts extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "posts_id")
    private Long id;

    // 연관 관계 조정
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id",nullable = false)
    private Users users;

    // 오늘의 한마디
    private String comment;

    // 수행한 날
    private String doingDay;




    // 실천 Id값만만
    @OneToMany(mappedBy = "posts")
    private List<PRconnect> pRconnects = new ArrayList<>();



    @Convert(converter = StringListConverter.class)
    private List<String> customBehaviorList;

    @OneToMany(mappedBy = "posts")
    private List<Heart> hearts = new ArrayList<>();


    //== 연관관계 편의 메서드==//
    public void setUsers(Users users){
        this.users = users;
        users.getPosts().add(this);
    }

    //== 생성 메서드==//
    public static Posts createPost(Users users, PostSaveRequestDto saveRequestDto){
        Posts posts = new Posts();
        posts.setUsers(users);
        posts.setDoingDay(saveRequestDto.getDoingDay());
        posts.setComment(saveRequestDto.getComment());

        posts.setCustomBehaviorList(saveRequestDto.getCustomizedBehaviors());


        // 연결테이블 생성



        return posts;

    }

    //== 게시물 수정 ==//
    public Long update(PostUpdateRequestDto updateRequestDto){
        this.setComment(updateRequestDto.getComment());
        this.setCustomBehaviorList(updateRequestDto.getCustomizedBehaviors());


        return this.getId();
    }

    @Override
    public String toString() {
        return "Posts{" +
                "users=" + users.getName() +

                '}';
    }
}
