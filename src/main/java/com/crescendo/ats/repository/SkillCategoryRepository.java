package com.crescendo.ats.repository;

import com.crescendo.ats.domain.SkillCategory;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the SkillCategory entity.
 */
public interface SkillCategoryRepository extends JpaRepository<SkillCategory,Long> {

}
