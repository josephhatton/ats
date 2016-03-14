package com.crescendo.ats.repository.search;

import com.crescendo.ats.domain.HiringContact;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the HiringContact entity.
 */
public interface HiringContactSearchRepository extends ElasticsearchRepository<HiringContact, Long> {
}
