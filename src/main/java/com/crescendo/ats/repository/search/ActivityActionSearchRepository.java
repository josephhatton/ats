package com.crescendo.ats.repository.search;

import com.crescendo.ats.domain.ActivityAction;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the ActivityAction entity.
 */
public interface ActivityActionSearchRepository extends ElasticsearchRepository<ActivityAction, Long> {
}
