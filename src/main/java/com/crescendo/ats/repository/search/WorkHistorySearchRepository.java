package com.crescendo.ats.repository.search;

import com.crescendo.ats.domain.WorkHistory;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the WorkHistory entity.
 */
public interface WorkHistorySearchRepository extends ElasticsearchRepository<WorkHistory, Long> {
}
