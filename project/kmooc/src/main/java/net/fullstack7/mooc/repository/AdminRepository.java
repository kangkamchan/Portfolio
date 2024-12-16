package net.fullstack7.mooc.repository;

import net.fullstack7.mooc.domain.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, String> {
    Optional<Admin> findByAdminIdAndPassword(String adminId, String password);
}
