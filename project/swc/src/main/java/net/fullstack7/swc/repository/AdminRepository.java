package net.fullstack7.swc.repository;

import net.fullstack7.swc.domain.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, String> {
}
