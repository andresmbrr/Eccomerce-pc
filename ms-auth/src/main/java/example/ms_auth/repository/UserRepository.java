package example.ms_auth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import example.ms_auth.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
}