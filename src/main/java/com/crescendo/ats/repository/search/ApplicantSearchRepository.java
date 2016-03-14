package com.crescendo.ats.repository.search;

import com.crescendo.ats.domain.Applicant;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Applicant entity.
 */
public interface ApplicantSearchRepository extends ElasticsearchRepository<Applicant, Long> {
}
