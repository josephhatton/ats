package com.crescendo.ats.repository;

import com.crescendo.ats.domain.JobStatus;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the JobStatus entity.
 */
public interface JobStatusRepository extends JpaRepository<JobStatus,Long> {

}
