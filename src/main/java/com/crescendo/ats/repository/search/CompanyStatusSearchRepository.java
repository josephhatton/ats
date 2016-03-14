package com.crescendo.ats.repository.search;

import com.crescendo.ats.domain.CompanyStatus;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the CompanyStatus entity.
 */
public interface CompanyStatusSearchRepository extends ElasticsearchRepository<CompanyStatus, Long> {
}
