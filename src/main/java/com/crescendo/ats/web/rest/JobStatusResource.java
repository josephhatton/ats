package com.crescendo.ats.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.crescendo.ats.domain.JobStatus;
import com.crescendo.ats.repository.JobStatusRepository;
import com.crescendo.ats.repository.search.JobStatusSearchRepository;
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
 * REST controller for managing JobStatus.
 */
@RestController
@RequestMapping("/api")
public class JobStatusResource {

    private final Logger log = LoggerFactory.getLogger(JobStatusResource.class);
        
    @Inject
    private JobStatusRepository jobStatusRepository;
    
    @Inject
    private JobStatusSearchRepository jobStatusSearchRepository;
    
    /**
     * POST  /jobStatuss -> Create a new jobStatus.
     */
    @RequestMapping(value = "/jobStatuss",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<JobStatus> createJobStatus(@Valid @RequestBody JobStatus jobStatus) throws URISyntaxException {
        log.debug("REST request to save JobStatus : {}", jobStatus);
        if (jobStatus.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("jobStatus", "idexists", "A new jobStatus cannot already have an ID")).body(null);
        }
        JobStatus result = jobStatusRepository.save(jobStatus);
        jobStatusSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/jobStatuss/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("jobStatus", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /jobStatuss -> Updates an existing jobStatus.
     */
    @RequestMapping(value = "/jobStatuss",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<JobStatus> updateJobStatus(@Valid @RequestBody JobStatus jobStatus) throws URISyntaxException {
        log.debug("REST request to update JobStatus : {}", jobStatus);
        if (jobStatus.getId() == null) {
            return createJobStatus(jobStatus);
        }
        JobStatus result = jobStatusRepository.save(jobStatus);
        jobStatusSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("jobStatus", jobStatus.getId().toString()))
            .body(result);
    }

    /**
     * GET  /jobStatuss -> get all the jobStatuss.
     */
    @RequestMapping(value = "/jobStatuss",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<JobStatus> getAllJobStatuss() {
        log.debug("REST request to get all JobStatuss");
        return jobStatusRepository.findAll();
            }

    /**
     * GET  /jobStatuss/:id -> get the "id" jobStatus.
     */
    @RequestMapping(value = "/jobStatuss/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<JobStatus> getJobStatus(@PathVariable Long id) {
        log.debug("REST request to get JobStatus : {}", id);
        JobStatus jobStatus = jobStatusRepository.findOne(id);
        return Optional.ofNullable(jobStatus)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /jobStatuss/:id -> delete the "id" jobStatus.
     */
    @RequestMapping(value = "/jobStatuss/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteJobStatus(@PathVariable Long id) {
        log.debug("REST request to delete JobStatus : {}", id);
        jobStatusRepository.delete(id);
        jobStatusSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("jobStatus", id.toString())).build();
    }

    /**
     * SEARCH  /_search/jobStatuss/:query -> search for the jobStatus corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/jobStatuss/{query:.+}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<JobStatus> searchJobStatuss(@PathVariable String query) {
        log.debug("REST request to search JobStatuss for query {}", query);
        return StreamSupport
            .stream(jobStatusSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
