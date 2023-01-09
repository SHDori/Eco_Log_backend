package Eco_Log.Eco_Log.repository;


import Eco_Log.Eco_Log.domain.Heart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HeartRepository extends JpaRepository<Heart,Long> {

    // 특정user가 특정게시물에 heart를 눌렀나 확인하는것
    @Query("SELECT h FROM Heart h WHERE h.posts.id = :targetPostId AND h.userId = :uid ")
    Heart findSpecificHeartInfo(@Param("uid") Long uid,@Param("targetPostId") Long targetPostId);

    @Query("SELECT h FROM Heart h WHERE h.posts.id = :targetPostId")
    List<Heart> findAllByPostsId(@Param("targetPostId")Long targetPostId);

    @Query("SELECT h FROM Heart h WHERE h.userId=:uid")
    List<Heart> findAllByUserId(@Param("uid") Long uid);

    // 하트를누른 유저는 나지만 대상게시물 id는 내거가 아닐떄
    @Query("SELECT h FROM Heart h WHERE h.userId = :uid AND NOT h.posts.users.id IN :uid")
    List<Heart> findOtherPostHeartList(@Param("uid") Long uid);

    @Query("SELECT h FROM Heart h WHERE h.userId = :uid AND NOT h.posts.users.id <> :uid")
    List<Heart> findOtherPostHeartListTest(@Param("uid") Long uid);
}
