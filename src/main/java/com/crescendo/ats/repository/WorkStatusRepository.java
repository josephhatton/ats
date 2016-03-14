package com.crescendo.ats.repository;

import com.crescendo.ats.domain.WorkStatus;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the WorkStatus entity.
 */
public interface WorkStatusRepository extends JpaRepository<WorkStatus,Long> {

}
