package uz.nexgroup.nexcrm.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import uz.nexgroup.nexcrm.model.User;

public interface UserRepository extends JpaRepository<User, Long>{
    Optional<User> findByEmail(String email);
}
