package Eco_Log.Eco_Log.domain.post;


import Eco_Log.Eco_Log.controller.dto.PostSaveRequestDto;
import Eco_Log.Eco_Log.domain.BaseTimeEntity;
import Eco_Log.Eco_Log.domain.user.Users;
import Eco_Log.Eco_Log.tool.StringListConverter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Posts extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 연관 관계 조정
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id",nullable = false)
    private Users users;

    // 오늘의 한마디
    private String comment;

    // 수행한 날
    private String doingDay;

    @Convert(converter = StringListConverter.class)
    private List<String> doingList;

    @Convert(converter = StringListConverter.class)
    private List<String> customBehaviorList;

    //== 연관관계 편의 메서드==//
    public void setUsers(Users users){
        this.users = users;
        users.getPosts().add(this);
    }

    //== 생성 메서드==//
    public static Posts createPost(Users users, PostSaveRequestDto saveRequestDto){
        Posts posts = new Posts();
        posts.setUsers(users);
        posts.setComment(saveRequestDto.getComment());
        posts.setDoingList(saveRequestDto.getDoingList());
        posts.setCustomBehaviorList(saveRequestDto.getCustomizedBehaviors());

        return posts;

    }


}
