package Eco_Log.Eco_Log.repository;

import Eco_Log.Eco_Log.domain.post.Posts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostsRepository extends JpaRepository<Posts,Long> {


    // 게시물 월단위로 조회
    @Query("SELECT p FROM Posts p WHERE p.doingDay LIKE %:month% AND p.users.id = :uid")
    List<Posts> findByMonth(@Param("uid") Long uid, @Param("month") String month);

    @Query("SELECT p FROM Posts p WHERE p.doingDay LIKE %:day% AND p.users.id = :uid")
    Posts findByDay(@Param("uid") Long uid, @Param("day") String day);

}
