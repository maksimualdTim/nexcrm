package uz.nexgroup.nexcrm.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import uz.nexgroup.nexcrm.model.RefreshToken;
import uz.nexgroup.nexcrm.model.User;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long>{
    Optional<RefreshToken> findByToken(String token);
    Optional<RefreshToken> findByUser(User user);
}
