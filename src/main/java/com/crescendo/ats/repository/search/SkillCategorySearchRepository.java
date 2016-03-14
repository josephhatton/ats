package com.crescendo.ats.repository.search;

import com.crescendo.ats.domain.SkillCategory;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the SkillCategory entity.
 */
public interface SkillCategorySearchRepository extends ElasticsearchRepository<SkillCategory, Long> {
}
