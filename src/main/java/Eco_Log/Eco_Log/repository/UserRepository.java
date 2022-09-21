package Eco_Log.Eco_Log.repository;

import Eco_Log.Eco_Log.domain.user.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {

    @Query("SELECT u FROM Users u WHERE u.id = ?1")
    Optional<Users> findById(Long id);

    @Query("SELECT u FROM Users u left join u.profiles p WHERE p.email = ?1")
    Users findByEmail(String email);


}
