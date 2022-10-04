package Eco_Log.Eco_Log.repository;

import Eco_Log.Eco_Log.controller.dto.SummaryInfoDTO;
import Eco_Log.Eco_Log.domain.post.PRconnect;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PRconnectRepository extends JpaRepository<PRconnect, Long> {

//    @Query("SELECT b.name,COUNT(pr.behaviors.id)\n" +
//            "FROM Posts p, PRconnect pr,Behaviors b \n" +
//            "WHERE p.users.id = 1 AND p.id  = pr.posts.id  AND pr.behaviors.id = b.id \n" +
//            "GROUP BY pr.behaviors.id\n" +
//            "ORDER BY COUNT(pr.behaviors.id) DESC")
//    List<SummaryInfoDTO> summaryFindByUserID(@Param("uid") Long uid);

    @Query("SELECT new Eco_Log.Eco_Log.controller.dto.SummaryInfoDTO(b.id,b.name,count(pr.behaviors.id))    \n" +
            "FROM Posts p, PRconnect pr,Behaviors b \n" +
            "WHERE p.users.id = :uid AND p.id  = pr.posts.id  AND pr.behaviors.id = b.id \n" +
            "GROUP BY pr.behaviors.id\n" +
            "ORDER BY COUNT(pr.behaviors.id) DESC")
    List<SummaryInfoDTO> summaryFindByUserID(@Param("uid") Long uid);

    @Query("select pr from PRconnect pr where pr.posts.id = :pid")
    List<PRconnect> findAllByPostId(@Param("pid") Long pid);
}
