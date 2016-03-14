package com.crescendo.ats.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.crescendo.ats.domain.HiringContact;
import com.crescendo.ats.repository.HiringContactRepository;
import com.crescendo.ats.repository.search.HiringContactSearchRepository;
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
 * REST controller for managing HiringContact.
 */
@RestController
@RequestMapping("/api")
public class HiringContactResource {

    private final Logger log = LoggerFactory.getLogger(HiringContactResource.class);
        
    @Inject
    private HiringContactRepository hiringContactRepository;
    
    @Inject
    private HiringContactSearchRepository hiringContactSearchRepository;
    
    /**
     * POST  /hiringContacts -> Create a new hiringContact.
     */
    @RequestMapping(value = "/hiringContacts",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<HiringContact> createHiringContact(@Valid @RequestBody HiringContact hiringContact) throws URISyntaxException {
        log.debug("REST request to save HiringContact : {}", hiringContact);
        if (hiringContact.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("hiringContact", "idexists", "A new hiringContact cannot already have an ID")).body(null);
        }
        HiringContact result = hiringContactRepository.save(hiringContact);
        hiringContactSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/hiringContacts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("hiringContact", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /hiringContacts -> Updates an existing hiringContact.
     */
    @RequestMapping(value = "/hiringContacts",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<HiringContact> updateHiringContact(@Valid @RequestBody HiringContact hiringContact) throws URISyntaxException {
        log.debug("REST request to update HiringContact : {}", hiringContact);
        if (hiringContact.getId() == null) {
            return createHiringContact(hiringContact);
        }
        HiringContact result = hiringContactRepository.save(hiringContact);
        hiringContactSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("hiringContact", hiringContact.getId().toString()))
            .body(result);
    }

    /**
     * GET  /hiringContacts -> get all the hiringContacts.
     */
    @RequestMapping(value = "/hiringContacts",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<HiringContact> getAllHiringContacts() {
        log.debug("REST request to get all HiringContacts");
        return hiringContactRepository.findAll();
            }

    /**
     * GET  /hiringContacts/:id -> get the "id" hiringContact.
     */
    @RequestMapping(value = "/hiringContacts/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<HiringContact> getHiringContact(@PathVariable Long id) {
        log.debug("REST request to get HiringContact : {}", id);
        HiringContact hiringContact = hiringContactRepository.findOne(id);
        return Optional.ofNullable(hiringContact)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /hiringContacts/:id -> delete the "id" hiringContact.
     */
    @RequestMapping(value = "/hiringContacts/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteHiringContact(@PathVariable Long id) {
        log.debug("REST request to delete HiringContact : {}", id);
        hiringContactRepository.delete(id);
        hiringContactSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("hiringContact", id.toString())).build();
    }

    /**
     * SEARCH  /_search/hiringContacts/:query -> search for the hiringContact corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/hiringContacts/{query:.+}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<HiringContact> searchHiringContacts(@PathVariable String query) {
        log.debug("REST request to search HiringContacts for query {}", query);
        return StreamSupport
            .stream(hiringContactSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
