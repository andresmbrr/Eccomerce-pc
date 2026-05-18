package example.ms_auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import example.ms_auth.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
}