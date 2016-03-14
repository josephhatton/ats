package com.crescendo.ats.repository;

import com.crescendo.ats.domain.ApplicantStatus;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ApplicantStatus entity.
 */
public interface ApplicantStatusRepository extends JpaRepository<ApplicantStatus,Long> {

}
