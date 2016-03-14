package com.crescendo.ats.repository;

import com.crescendo.ats.domain.JobOrder;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the JobOrder entity.
 */
public interface JobOrderRepository extends JpaRepository<JobOrder,Long> {

}
