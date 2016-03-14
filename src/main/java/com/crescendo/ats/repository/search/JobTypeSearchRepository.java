package com.crescendo.ats.repository.search;

import com.crescendo.ats.domain.JobType;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the JobType entity.
 */
public interface JobTypeSearchRepository extends ElasticsearchRepository<JobType, Long> {
}
