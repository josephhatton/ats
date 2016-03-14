package com.crescendo.ats.repository;

import com.crescendo.ats.domain.ActivityAction;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ActivityAction entity.
 */
public interface ActivityActionRepository extends JpaRepository<ActivityAction,Long> {

}
