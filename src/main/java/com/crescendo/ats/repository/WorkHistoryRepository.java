package com.crescendo.ats.repository;

import com.crescendo.ats.domain.WorkHistory;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the WorkHistory entity.
 */
public interface WorkHistoryRepository extends JpaRepository<WorkHistory,Long> {

}
