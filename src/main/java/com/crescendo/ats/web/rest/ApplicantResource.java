package com.crescendo.ats.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.crescendo.ats.domain.Applicant;
import com.crescendo.ats.repository.ApplicantRepository;
import com.crescendo.ats.repository.search.ApplicantSearchRepository;
import com.crescendo.ats.web.rest.util.HeaderUtil;
import com.crescendo.ats.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Applicant.
 */
@RestController
@RequestMapping("/api")
public class ApplicantResource {

    private final Logger log = LoggerFactory.getLogger(ApplicantResource.class);
        
    @Inject
    private ApplicantRepository applicantRepository;
    
    @Inject
    private ApplicantSearchRepository applicantSearchRepository;
    
    /**
     * POST  /applicants -> Create a new applicant.
     */
    @RequestMapping(value = "/applicants",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Applicant> createApplicant(@Valid @RequestBody Applicant applicant) throws URISyntaxException {
        log.debug("REST request to save Applicant : {}", applicant);
        if (applicant.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("applicant", "idexists", "A new applicant cannot already have an ID")).body(null);
        }
        Applicant result = applicantRepository.save(applicant);
        applicantSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/applicants/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("applicant", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /applicants -> Updates an existing applicant.
     */
    @RequestMapping(value = "/applicants",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Applicant> updateApplicant(@Valid @RequestBody Applicant applicant) throws URISyntaxException {
        log.debug("REST request to update Applicant : {}", applicant);
        if (applicant.getId() == null) {
            return createApplicant(applicant);
        }
        Applicant result = applicantRepository.save(applicant);
        applicantSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("applicant", applicant.getId().toString()))
            .body(result);
    }

    /**
     * GET  /applicants -> get all the applicants.
     */
    @RequestMapping(value = "/applicants",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Applicant>> getAllApplicants(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Applicants");
        Page<Applicant> page = applicantRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/applicants");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /applicants/:id -> get the "id" applicant.
     */
    @RequestMapping(value = "/applicants/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Applicant> getApplicant(@PathVariable Long id) {
        log.debug("REST request to get Applicant : {}", id);
        Applicant applicant = applicantRepository.findOne(id);
        return Optional.ofNullable(applicant)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /applicants/:id -> delete the "id" applicant.
     */
    @RequestMapping(value = "/applicants/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteApplicant(@PathVariable Long id) {
        log.debug("REST request to delete Applicant : {}", id);
        applicantRepository.delete(id);
        applicantSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("applicant", id.toString())).build();
    }

    /**
     * SEARCH  /_search/applicants/:query -> search for the applicant corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/applicants/{query:.+}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Applicant> searchApplicants(@PathVariable String query) {
        log.debug("REST request to search Applicants for query {}", query);
        return StreamSupport
            .stream(applicantSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
