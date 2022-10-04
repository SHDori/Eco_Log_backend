package Eco_Log.Eco_Log.repository;


import Eco_Log.Eco_Log.controller.dto.BehaviorsDto;
import Eco_Log.Eco_Log.domain.post.Behaviors;
import Eco_Log.Eco_Log.domain.post.Posts;
import Eco_Log.Eco_Log.domain.user.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BehaviorRepository extends JpaRepository<Behaviors,Long> {

    @Query("SELECT b FROM Behaviors b WHERE b.id = ?1")
    Optional<Behaviors> findById(Long id);


    @Query("SELECT new Eco_Log.Eco_Log.controller.dto.BehaviorsDto(b.id,b.name) from Behaviors b")
    List<BehaviorsDto> getAllBehavior();



}
