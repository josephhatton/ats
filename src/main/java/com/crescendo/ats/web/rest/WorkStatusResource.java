package com.crescendo.ats.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.crescendo.ats.domain.WorkStatus;
import com.crescendo.ats.repository.WorkStatusRepository;
import com.crescendo.ats.repository.search.WorkStatusSearchRepository;
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
 * REST controller for managing WorkStatus.
 */
@RestController
@RequestMapping("/api")
public class WorkStatusResource {

    private final Logger log = LoggerFactory.getLogger(WorkStatusResource.class);
        
    @Inject
    private WorkStatusRepository workStatusRepository;
    
    @Inject
    private WorkStatusSearchRepository workStatusSearchRepository;
    
    /**
     * POST  /workStatuss -> Create a new workStatus.
     */
    @RequestMapping(value = "/workStatuss",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<WorkStatus> createWorkStatus(@Valid @RequestBody WorkStatus workStatus) throws URISyntaxException {
        log.debug("REST request to save WorkStatus : {}", workStatus);
        if (workStatus.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("workStatus", "idexists", "A new workStatus cannot already have an ID")).body(null);
        }
        WorkStatus result = workStatusRepository.save(workStatus);
        workStatusSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/workStatuss/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("workStatus", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /workStatuss -> Updates an existing workStatus.
     */
    @RequestMapping(value = "/workStatuss",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<WorkStatus> updateWorkStatus(@Valid @RequestBody WorkStatus workStatus) throws URISyntaxException {
        log.debug("REST request to update WorkStatus : {}", workStatus);
        if (workStatus.getId() == null) {
            return createWorkStatus(workStatus);
        }
        WorkStatus result = workStatusRepository.save(workStatus);
        workStatusSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("workStatus", workStatus.getId().toString()))
            .body(result);
    }

    /**
     * GET  /workStatuss -> get all the workStatuss.
     */
    @RequestMapping(value = "/workStatuss",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<WorkStatus> getAllWorkStatuss() {
        log.debug("REST request to get all WorkStatuss");
        return workStatusRepository.findAll();
            }

    /**
     * GET  /workStatuss/:id -> get the "id" workStatus.
     */
    @RequestMapping(value = "/workStatuss/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<WorkStatus> getWorkStatus(@PathVariable Long id) {
        log.debug("REST request to get WorkStatus : {}", id);
        WorkStatus workStatus = workStatusRepository.findOne(id);
        return Optional.ofNullable(workStatus)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /workStatuss/:id -> delete the "id" workStatus.
     */
    @RequestMapping(value = "/workStatuss/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteWorkStatus(@PathVariable Long id) {
        log.debug("REST request to delete WorkStatus : {}", id);
        workStatusRepository.delete(id);
        workStatusSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("workStatus", id.toString())).build();
    }

    /**
     * SEARCH  /_search/workStatuss/:query -> search for the workStatus corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/workStatuss/{query:.+}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<WorkStatus> searchWorkStatuss(@PathVariable String query) {
        log.debug("REST request to search WorkStatuss for query {}", query);
        return StreamSupport
            .stream(workStatusSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
