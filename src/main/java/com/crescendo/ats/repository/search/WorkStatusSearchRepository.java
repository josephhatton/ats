package com.crescendo.ats.repository.search;

import com.crescendo.ats.domain.WorkStatus;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the WorkStatus entity.
 */
public interface WorkStatusSearchRepository extends ElasticsearchRepository<WorkStatus, Long> {
}
