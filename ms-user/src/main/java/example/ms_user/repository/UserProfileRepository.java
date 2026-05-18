package example.ms_user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import example.ms_user.model.UserProfile;

public interface UserProfileRepository
        extends JpaRepository<UserProfile, Long> {
}