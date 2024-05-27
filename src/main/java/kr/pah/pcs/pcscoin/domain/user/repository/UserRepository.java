package kr.pah.pcs.pcscoin.domain.user.repository;

import kr.pah.pcs.pcscoin.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> getUserByName(String username);

    Optional<User> getUserByToken(String token);

    Optional<User> getUserByEmailAndIsDeleteFalse(String email);

    Optional<User> getUserByPhoneAndIsDeleteFalse(String phone);

}
