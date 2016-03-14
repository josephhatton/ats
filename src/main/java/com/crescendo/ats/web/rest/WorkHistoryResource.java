package com.crescendo.ats.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.crescendo.ats.domain.WorkHistory;
import com.crescendo.ats.repository.WorkHistoryRepository;
import com.crescendo.ats.repository.search.WorkHistorySearchRepository;
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
 * REST controller for managing WorkHistory.
 */
@RestController
@RequestMapping("/api")
public class WorkHistoryResource {

    private final Logger log = LoggerFactory.getLogger(WorkHistoryResource.class);
        
    @Inject
    private WorkHistoryRepository workHistoryRepository;
    
    @Inject
    private WorkHistorySearchRepository workHistorySearchRepository;
    
    /**
     * POST  /workHistorys -> Create a new workHistory.
     */
    @RequestMapping(value = "/workHistorys",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<WorkHistory> createWorkHistory(@RequestBody WorkHistory workHistory) throws URISyntaxException {
        log.debug("REST request to save WorkHistory : {}", workHistory);
        if (workHistory.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("workHistory", "idexists", "A new workHistory cannot already have an ID")).body(null);
        }
        WorkHistory result = workHistoryRepository.save(workHistory);
        workHistorySearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/workHistorys/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("workHistory", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /workHistorys -> Updates an existing workHistory.
     */
    @RequestMapping(value = "/workHistorys",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<WorkHistory> updateWorkHistory(@RequestBody WorkHistory workHistory) throws URISyntaxException {
        log.debug("REST request to update WorkHistory : {}", workHistory);
        if (workHistory.getId() == null) {
            return createWorkHistory(workHistory);
        }
        WorkHistory result = workHistoryRepository.save(workHistory);
        workHistorySearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("workHistory", workHistory.getId().toString()))
            .body(result);
    }

    /**
     * GET  /workHistorys -> get all the workHistorys.
     */
    @RequestMapping(value = "/workHistorys",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<WorkHistory> getAllWorkHistorys() {
        log.debug("REST request to get all WorkHistorys");
        return workHistoryRepository.findAll();
            }

    /**
     * GET  /workHistorys/:id -> get the "id" workHistory.
     */
    @RequestMapping(value = "/workHistorys/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<WorkHistory> getWorkHistory(@PathVariable Long id) {
        log.debug("REST request to get WorkHistory : {}", id);
        WorkHistory workHistory = workHistoryRepository.findOne(id);
        return Optional.ofNullable(workHistory)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /workHistorys/:id -> delete the "id" workHistory.
     */
    @RequestMapping(value = "/workHistorys/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteWorkHistory(@PathVariable Long id) {
        log.debug("REST request to delete WorkHistory : {}", id);
        workHistoryRepository.delete(id);
        workHistorySearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("workHistory", id.toString())).build();
    }

    /**
     * SEARCH  /_search/workHistorys/:query -> search for the workHistory corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/workHistorys/{query:.+}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<WorkHistory> searchWorkHistorys(@PathVariable String query) {
        log.debug("REST request to search WorkHistorys for query {}", query);
        return StreamSupport
            .stream(workHistorySearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
