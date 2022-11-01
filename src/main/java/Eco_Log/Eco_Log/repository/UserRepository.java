package Eco_Log.Eco_Log.repository;

import Eco_Log.Eco_Log.domain.user.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {

    @Query("SELECT u FROM Users u WHERE u.id = ?1")
    Optional<Users> findById(Long id);

    @Query("SELECT u FROM Users u left join u.profiles p WHERE p.email = ?1")
    Users findByEmail(String email);

    @Query("select u from Users u Where u.profiles.isPublic=true And u.profiles.email Like %:word% OR  u.profiles.isPublic=true And u.profiles.nickName Like %:word%")
    List<Users> findUsersBySearchWord(@Param("word") String word);



}
