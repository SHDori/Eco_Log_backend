package Eco_Log.Eco_Log.repository;

import Eco_Log.Eco_Log.domain.Follow;
import Eco_Log.Eco_Log.domain.composite_key.FollowPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FollowRepository extends JpaRepository<Follow, FollowPK> {

    // 내가 From 이면 to들은 내가 follow요청한 사람들들
    // 내 팔로잉 조회(follow정보 모두)
    @Query("select f FROM Follow f where f.fromUser = :userId")
    List<Follow> findMyFollowingByUserId(@Param("userId")Long userId);

    // 내 팔로잉 id만 조회
    @Query("select f.toUser FROM Follow f where f.fromUser = :userId")
    List<Long> findMyFollowingUserIdByFromUserId(@Param("userId")Long userId);

    // 내 팔로워 id만 조회
    @Query("select f.fromUser FROM Follow f where f.toUser = :userId")
    List<Long> findMyFollowerUserIdByToUserId(@Param("userId")Long userId);

    @Query("select f FROM Follow f where f.fromUser = :fromUserId and f.toUser = :toUserId")
    Optional<Follow> findSpecificFollowInfo(@Param("fromUserId")Long fromUserId, @Param("toUserId")Long toUserId);


    //오류로 잘라먹히면 안되니까 없으면 Null값을 뱉는애를 하나만들자
    @Query("select f FROM Follow f where f.fromUser = :fromUserId and f.toUser = :toUserId")
    Follow findSpecificFollowInfoNoOptional(@Param("fromUserId")Long fromUserId, @Param("toUserId")Long toUserId);



}
