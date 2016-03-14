package com.crescendo.ats.repository;

import com.crescendo.ats.domain.CompanyStatus;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the CompanyStatus entity.
 */
public interface CompanyStatusRepository extends JpaRepository<CompanyStatus,Long> {

}
