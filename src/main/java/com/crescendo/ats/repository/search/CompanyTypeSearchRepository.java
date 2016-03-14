package com.crescendo.ats.repository.search;

import com.crescendo.ats.domain.CompanyType;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the CompanyType entity.
 */
public interface CompanyTypeSearchRepository extends ElasticsearchRepository<CompanyType, Long> {
}
