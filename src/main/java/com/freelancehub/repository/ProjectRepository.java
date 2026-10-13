package com.freelancehub.repository;

import com.freelancehub.model.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    Page<Project> findByClientId(Long clientId, Pageable pageable);

    @Query("SELECT p FROM Project p WHERE " +
           "(:status IS NULL OR p.status = :status) AND " +
           "(:keyword IS NULL OR LOWER(p.title) LIKE LOWER(CONCAT('%',:keyword,'%')) " +
           " OR LOWER(p.description) LIKE LOWER(CONCAT('%',:keyword,'%')))")
    Page<Project> search(@Param("status") Project.Status status,
                         @Param("keyword") String keyword,
                         Pageable pageable);
}
