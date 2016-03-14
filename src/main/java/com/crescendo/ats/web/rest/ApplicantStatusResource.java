package com.crescendo.ats.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.crescendo.ats.domain.ApplicantStatus;
import com.crescendo.ats.repository.ApplicantStatusRepository;
import com.crescendo.ats.repository.search.ApplicantStatusSearchRepository;
import com.crescendo.ats.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing ApplicantStatus.
 */
@RestController
@RequestMapping("/api")
public class ApplicantStatusResource {

    private final Logger log = LoggerFactory.getLogger(ApplicantStatusResource.class);
        
    @Inject
    private ApplicantStatusRepository applicantStatusRepository;
    
    @Inject
    private ApplicantStatusSearchRepository applicantStatusSearchRepository;
    
    /**
     * POST  /applicantStatuss -> Create a new applicantStatus.
     */
    @RequestMapping(value = "/applicantStatuss",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ApplicantStatus> createApplicantStatus(@RequestBody ApplicantStatus applicantStatus) throws URISyntaxException {
        log.debug("REST request to save ApplicantStatus : {}", applicantStatus);
        if (applicantStatus.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("applicantStatus", "idexists", "A new applicantStatus cannot already have an ID")).body(null);
        }
        ApplicantStatus result = applicantStatusRepository.save(applicantStatus);
        applicantStatusSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/applicantStatuss/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("applicantStatus", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /applicantStatuss -> Updates an existing applicantStatus.
     */
    @RequestMapping(value = "/applicantStatuss",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ApplicantStatus> updateApplicantStatus(@RequestBody ApplicantStatus applicantStatus) throws URISyntaxException {
        log.debug("REST request to update ApplicantStatus : {}", applicantStatus);
        if (applicantStatus.getId() == null) {
            return createApplicantStatus(applicantStatus);
        }
        ApplicantStatus result = applicantStatusRepository.save(applicantStatus);
        applicantStatusSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("applicantStatus", applicantStatus.getId().toString()))
            .body(result);
    }

    /**
     * GET  /applicantStatuss -> get all the applicantStatuss.
     */
    @RequestMapping(value = "/applicantStatuss",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<ApplicantStatus> getAllApplicantStatuss() {
        log.debug("REST request to get all ApplicantStatuss");
        return applicantStatusRepository.findAll();
            }

    /**
     * GET  /applicantStatuss/:id -> get the "id" applicantStatus.
     */
    @RequestMapping(value = "/applicantStatuss/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ApplicantStatus> getApplicantStatus(@PathVariable Long id) {
        log.debug("REST request to get ApplicantStatus : {}", id);
        ApplicantStatus applicantStatus = applicantStatusRepository.findOne(id);
        return Optional.ofNullable(applicantStatus)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /applicantStatuss/:id -> delete the "id" applicantStatus.
     */
    @RequestMapping(value = "/applicantStatuss/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteApplicantStatus(@PathVariable Long id) {
        log.debug("REST request to delete ApplicantStatus : {}", id);
        applicantStatusRepository.delete(id);
        applicantStatusSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("applicantStatus", id.toString())).build();
    }

    /**
     * SEARCH  /_search/applicantStatuss/:query -> search for the applicantStatus corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/applicantStatuss/{query:.+}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<ApplicantStatus> searchApplicantStatuss(@PathVariable String query) {
        log.debug("REST request to search ApplicantStatuss for query {}", query);
        return StreamSupport
            .stream(applicantStatusSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
