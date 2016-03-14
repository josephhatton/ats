package com.crescendo.ats.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.crescendo.ats.domain.JobType;
import com.crescendo.ats.repository.JobTypeRepository;
import com.crescendo.ats.repository.search.JobTypeSearchRepository;
import com.crescendo.ats.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * REST controller for managing JobType.
 */
@RestController
@RequestMapping("/api")
public class JobTypeResource {

    private final Logger log = LoggerFactory.getLogger(JobTypeResource.class);
        
    @Inject
    private JobTypeRepository jobTypeRepository;
    
    @Inject
    private JobTypeSearchRepository jobTypeSearchRepository;
    
    /**
     * POST  /jobTypes -> Create a new jobType.
     */
    @RequestMapping(value = "/jobTypes",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<JobType> createJobType(@Valid @RequestBody JobType jobType) throws URISyntaxException {
        log.debug("REST request to save JobType : {}", jobType);
        if (jobType.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("jobType", "idexists", "A new jobType cannot already have an ID")).body(null);
        }
        JobType result = jobTypeRepository.save(jobType);
        jobTypeSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/jobTypes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("jobType", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /jobTypes -> Updates an existing jobType.
     */
    @RequestMapping(value = "/jobTypes",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<JobType> updateJobType(@Valid @RequestBody JobType jobType) throws URISyntaxException {
        log.debug("REST request to update JobType : {}", jobType);
        if (jobType.getId() == null) {
            return createJobType(jobType);
        }
        JobType result = jobTypeRepository.save(jobType);
        jobTypeSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("jobType", jobType.getId().toString()))
            .body(result);
    }

    /**
     * GET  /jobTypes -> get all the jobTypes.
     */
    @RequestMapping(value = "/jobTypes",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<JobType> getAllJobTypes() {
        log.debug("REST request to get all JobTypes");
        return jobTypeRepository.findAll();
            }

    /**
     * GET  /jobTypes/:id -> get the "id" jobType.
     */
    @RequestMapping(value = "/jobTypes/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<JobType> getJobType(@PathVariable Long id) {
        log.debug("REST request to get JobType : {}", id);
        JobType jobType = jobTypeRepository.findOne(id);
        return Optional.ofNullable(jobType)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /jobTypes/:id -> delete the "id" jobType.
     */
    @RequestMapping(value = "/jobTypes/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteJobType(@PathVariable Long id) {
        log.debug("REST request to delete JobType : {}", id);
        jobTypeRepository.delete(id);
        jobTypeSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("jobType", id.toString())).build();
    }

    /**
     * SEARCH  /_search/jobTypes/:query -> search for the jobType corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/jobTypes/{query:.+}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<JobType> searchJobTypes(@PathVariable String query) {
        log.debug("REST request to search JobTypes for query {}", query);
        return StreamSupport
            .stream(jobTypeSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
