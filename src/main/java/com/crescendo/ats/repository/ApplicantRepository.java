package com.crescendo.ats.repository;

import com.crescendo.ats.domain.Applicant;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Applicant entity.
 */
public interface ApplicantRepository extends JpaRepository<Applicant,Long> {

}
