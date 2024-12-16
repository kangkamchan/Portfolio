package net.fullstack7.mooc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.fullstack7.mooc.domain.Institution;

import java.util.List;
import java.util.Optional;
@Repository
public interface InstitutionRepository extends JpaRepository<Institution, String> {
  List<Institution> findAllByOrderByInstitutionIdAsc();
  Optional<Institution> findByInstitutionId(int institutionId);
}
