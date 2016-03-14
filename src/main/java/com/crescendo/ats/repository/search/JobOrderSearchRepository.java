package com.crescendo.ats.repository.search;

import com.crescendo.ats.domain.JobOrder;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the JobOrder entity.
 */
public interface JobOrderSearchRepository extends ElasticsearchRepository<JobOrder, Long> {
}
