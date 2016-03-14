package com.crescendo.ats.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.crescendo.ats.domain.JobOrder;
import com.crescendo.ats.repository.JobOrderRepository;
import com.crescendo.ats.repository.search.JobOrderSearchRepository;
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
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing JobOrder.
 */
@RestController
@RequestMapping("/api")
public class JobOrderResource {

    private final Logger log = LoggerFactory.getLogger(JobOrderResource.class);
        
    @Inject
    private JobOrderRepository jobOrderRepository;
    
    @Inject
    private JobOrderSearchRepository jobOrderSearchRepository;
    
    /**
     * POST  /jobOrders -> Create a new jobOrder.
     */
    @RequestMapping(value = "/jobOrders",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<JobOrder> createJobOrder(@RequestBody JobOrder jobOrder) throws URISyntaxException {
        log.debug("REST request to save JobOrder : {}", jobOrder);
        if (jobOrder.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("jobOrder", "idexists", "A new jobOrder cannot already have an ID")).body(null);
        }
        JobOrder result = jobOrderRepository.save(jobOrder);
        jobOrderSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/jobOrders/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("jobOrder", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /jobOrders -> Updates an existing jobOrder.
     */
    @RequestMapping(value = "/jobOrders",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<JobOrder> updateJobOrder(@RequestBody JobOrder jobOrder) throws URISyntaxException {
        log.debug("REST request to update JobOrder : {}", jobOrder);
        if (jobOrder.getId() == null) {
            return createJobOrder(jobOrder);
        }
        JobOrder result = jobOrderRepository.save(jobOrder);
        jobOrderSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("jobOrder", jobOrder.getId().toString()))
            .body(result);
    }

    /**
     * GET  /jobOrders -> get all the jobOrders.
     */
    @RequestMapping(value = "/jobOrders",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<JobOrder>> getAllJobOrders(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of JobOrders");
        Page<JobOrder> page = jobOrderRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/jobOrders");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /jobOrders/:id -> get the "id" jobOrder.
     */
    @RequestMapping(value = "/jobOrders/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<JobOrder> getJobOrder(@PathVariable Long id) {
        log.debug("REST request to get JobOrder : {}", id);
        JobOrder jobOrder = jobOrderRepository.findOne(id);
        return Optional.ofNullable(jobOrder)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /jobOrders/:id -> delete the "id" jobOrder.
     */
    @RequestMapping(value = "/jobOrders/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteJobOrder(@PathVariable Long id) {
        log.debug("REST request to delete JobOrder : {}", id);
        jobOrderRepository.delete(id);
        jobOrderSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("jobOrder", id.toString())).build();
    }

    /**
     * SEARCH  /_search/jobOrders/:query -> search for the jobOrder corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/jobOrders/{query:.+}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<JobOrder> searchJobOrders(@PathVariable String query) {
        log.debug("REST request to search JobOrders for query {}", query);
        return StreamSupport
            .stream(jobOrderSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
