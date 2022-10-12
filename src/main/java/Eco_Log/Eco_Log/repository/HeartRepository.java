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
}
