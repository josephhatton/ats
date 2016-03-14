package com.crescendo.ats.repository;

import com.crescendo.ats.domain.JobType;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the JobType entity.
 */
public interface JobTypeRepository extends JpaRepository<JobType,Long> {

}
