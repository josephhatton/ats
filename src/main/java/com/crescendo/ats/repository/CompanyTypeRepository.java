package com.crescendo.ats.repository;

import com.crescendo.ats.domain.CompanyType;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the CompanyType entity.
 */
public interface CompanyTypeRepository extends JpaRepository<CompanyType,Long> {

}
