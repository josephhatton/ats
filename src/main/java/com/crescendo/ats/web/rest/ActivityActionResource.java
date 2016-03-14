package com.crescendo.ats.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.crescendo.ats.domain.ActivityAction;
import com.crescendo.ats.repository.ActivityActionRepository;
import com.crescendo.ats.repository.search.ActivityActionSearchRepository;
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
 * REST controller for managing ActivityAction.
 */
@RestController
@RequestMapping("/api")
public class ActivityActionResource {

    private final Logger log = LoggerFactory.getLogger(ActivityActionResource.class);
        
    @Inject
    private ActivityActionRepository activityActionRepository;
    
    @Inject
    private ActivityActionSearchRepository activityActionSearchRepository;
    
    /**
     * POST  /activityActions -> Create a new activityAction.
     */
    @RequestMapping(value = "/activityActions",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ActivityAction> createActivityAction(@Valid @RequestBody ActivityAction activityAction) throws URISyntaxException {
        log.debug("REST request to save ActivityAction : {}", activityAction);
        if (activityAction.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("activityAction", "idexists", "A new activityAction cannot already have an ID")).body(null);
        }
        ActivityAction result = activityActionRepository.save(activityAction);
        activityActionSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/activityActions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("activityAction", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /activityActions -> Updates an existing activityAction.
     */
    @RequestMapping(value = "/activityActions",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ActivityAction> updateActivityAction(@Valid @RequestBody ActivityAction activityAction) throws URISyntaxException {
        log.debug("REST request to update ActivityAction : {}", activityAction);
        if (activityAction.getId() == null) {
            return createActivityAction(activityAction);
        }
        ActivityAction result = activityActionRepository.save(activityAction);
        activityActionSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("activityAction", activityAction.getId().toString()))
            .body(result);
    }

    /**
     * GET  /activityActions -> get all the activityActions.
     */
    @RequestMapping(value = "/activityActions",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<ActivityAction> getAllActivityActions() {
        log.debug("REST request to get all ActivityActions");
        return activityActionRepository.findAll();
            }

    /**
     * GET  /activityActions/:id -> get the "id" activityAction.
     */
    @RequestMapping(value = "/activityActions/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ActivityAction> getActivityAction(@PathVariable Long id) {
        log.debug("REST request to get ActivityAction : {}", id);
        ActivityAction activityAction = activityActionRepository.findOne(id);
        return Optional.ofNullable(activityAction)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /activityActions/:id -> delete the "id" activityAction.
     */
    @RequestMapping(value = "/activityActions/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteActivityAction(@PathVariable Long id) {
        log.debug("REST request to delete ActivityAction : {}", id);
        activityActionRepository.delete(id);
        activityActionSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("activityAction", id.toString())).build();
    }

    /**
     * SEARCH  /_search/activityActions/:query -> search for the activityAction corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/activityActions/{query:.+}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<ActivityAction> searchActivityActions(@PathVariable String query) {
        log.debug("REST request to search ActivityActions for query {}", query);
        return StreamSupport
            .stream(activityActionSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
