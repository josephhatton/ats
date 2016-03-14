package com.crescendo.ats.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.crescendo.ats.domain.Activity;
import com.crescendo.ats.repository.ActivityRepository;
import com.crescendo.ats.repository.search.ActivitySearchRepository;
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
 * REST controller for managing Activity.
 */
@RestController
@RequestMapping("/api")
public class ActivityResource {

    private final Logger log = LoggerFactory.getLogger(ActivityResource.class);
        
    @Inject
    private ActivityRepository activityRepository;
    
    @Inject
    private ActivitySearchRepository activitySearchRepository;
    
    /**
     * POST  /activitys -> Create a new activity.
     */
    @RequestMapping(value = "/activitys",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Activity> createActivity(@RequestBody Activity activity) throws URISyntaxException {
        log.debug("REST request to save Activity : {}", activity);
        if (activity.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("activity", "idexists", "A new activity cannot already have an ID")).body(null);
        }
        Activity result = activityRepository.save(activity);
        activitySearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/activitys/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("activity", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /activitys -> Updates an existing activity.
     */
    @RequestMapping(value = "/activitys",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Activity> updateActivity(@RequestBody Activity activity) throws URISyntaxException {
        log.debug("REST request to update Activity : {}", activity);
        if (activity.getId() == null) {
            return createActivity(activity);
        }
        Activity result = activityRepository.save(activity);
        activitySearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("activity", activity.getId().toString()))
            .body(result);
    }

    /**
     * GET  /activitys -> get all the activitys.
     */
    @RequestMapping(value = "/activitys",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Activity> getAllActivitys() {
        log.debug("REST request to get all Activitys");
        return activityRepository.findAll();
            }

    /**
     * GET  /activitys/:id -> get the "id" activity.
     */
    @RequestMapping(value = "/activitys/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Activity> getActivity(@PathVariable Long id) {
        log.debug("REST request to get Activity : {}", id);
        Activity activity = activityRepository.findOne(id);
        return Optional.ofNullable(activity)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /activitys/:id -> delete the "id" activity.
     */
    @RequestMapping(value = "/activitys/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteActivity(@PathVariable Long id) {
        log.debug("REST request to delete Activity : {}", id);
        activityRepository.delete(id);
        activitySearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("activity", id.toString())).build();
    }

    /**
     * SEARCH  /_search/activitys/:query -> search for the activity corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/activitys/{query:.+}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Activity> searchActivitys(@PathVariable String query) {
        log.debug("REST request to search Activitys for query {}", query);
        return StreamSupport
            .stream(activitySearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
