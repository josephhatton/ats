package com.crescendo.ats.repository.search;

import com.crescendo.ats.domain.ApplicantStatus;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the ApplicantStatus entity.
 */
public interface ApplicantStatusSearchRepository extends ElasticsearchRepository<ApplicantStatus, Long> {
}
