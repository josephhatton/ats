package com.crescendo.ats.repository.search;

import com.crescendo.ats.domain.JobStatus;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the JobStatus entity.
 */
public interface JobStatusSearchRepository extends ElasticsearchRepository<JobStatus, Long> {
}
