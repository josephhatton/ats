package com.crescendo.ats.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.crescendo.ats.domain.CompanyStatus;
import com.crescendo.ats.repository.CompanyStatusRepository;
import com.crescendo.ats.repository.search.CompanyStatusSearchRepository;
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
 * REST controller for managing CompanyStatus.
 */
@RestController
@RequestMapping("/api")
public class CompanyStatusResource {

    private final Logger log = LoggerFactory.getLogger(CompanyStatusResource.class);
        
    @Inject
    private CompanyStatusRepository companyStatusRepository;
    
    @Inject
    private CompanyStatusSearchRepository companyStatusSearchRepository;
    
    /**
     * POST  /companyStatuss -> Create a new companyStatus.
     */
    @RequestMapping(value = "/companyStatuss",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CompanyStatus> createCompanyStatus(@Valid @RequestBody CompanyStatus companyStatus) throws URISyntaxException {
        log.debug("REST request to save CompanyStatus : {}", companyStatus);
        if (companyStatus.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("companyStatus", "idexists", "A new companyStatus cannot already have an ID")).body(null);
        }
        CompanyStatus result = companyStatusRepository.save(companyStatus);
        companyStatusSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/companyStatuss/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("companyStatus", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /companyStatuss -> Updates an existing companyStatus.
     */
    @RequestMapping(value = "/companyStatuss",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CompanyStatus> updateCompanyStatus(@Valid @RequestBody CompanyStatus companyStatus) throws URISyntaxException {
        log.debug("REST request to update CompanyStatus : {}", companyStatus);
        if (companyStatus.getId() == null) {
            return createCompanyStatus(companyStatus);
        }
        CompanyStatus result = companyStatusRepository.save(companyStatus);
        companyStatusSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("companyStatus", companyStatus.getId().toString()))
            .body(result);
    }

    /**
     * GET  /companyStatuss -> get all the companyStatuss.
     */
    @RequestMapping(value = "/companyStatuss",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<CompanyStatus> getAllCompanyStatuss() {
        log.debug("REST request to get all CompanyStatuss");
        return companyStatusRepository.findAll();
            }

    /**
     * GET  /companyStatuss/:id -> get the "id" companyStatus.
     */
    @RequestMapping(value = "/companyStatuss/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CompanyStatus> getCompanyStatus(@PathVariable Long id) {
        log.debug("REST request to get CompanyStatus : {}", id);
        CompanyStatus companyStatus = companyStatusRepository.findOne(id);
        return Optional.ofNullable(companyStatus)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /companyStatuss/:id -> delete the "id" companyStatus.
     */
    @RequestMapping(value = "/companyStatuss/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteCompanyStatus(@PathVariable Long id) {
        log.debug("REST request to delete CompanyStatus : {}", id);
        companyStatusRepository.delete(id);
        companyStatusSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("companyStatus", id.toString())).build();
    }

    /**
     * SEARCH  /_search/companyStatuss/:query -> search for the companyStatus corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/companyStatuss/{query:.+}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<CompanyStatus> searchCompanyStatuss(@PathVariable String query) {
        log.debug("REST request to search CompanyStatuss for query {}", query);
        return StreamSupport
            .stream(companyStatusSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
