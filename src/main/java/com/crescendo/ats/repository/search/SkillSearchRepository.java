package com.crescendo.ats.repository.search;

import com.crescendo.ats.domain.Skill;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Skill entity.
 */
public interface SkillSearchRepository extends ElasticsearchRepository<Skill, Long> {
}
