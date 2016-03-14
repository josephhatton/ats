package com.crescendo.ats.repository;

import com.crescendo.ats.domain.HiringContact;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the HiringContact entity.
 */
public interface HiringContactRepository extends JpaRepository<HiringContact,Long> {

}
