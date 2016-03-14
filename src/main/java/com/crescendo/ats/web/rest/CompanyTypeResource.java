package com.crescendo.ats.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.crescendo.ats.domain.CompanyType;
import com.crescendo.ats.repository.CompanyTypeRepository;
import com.crescendo.ats.repository.search.CompanyTypeSearchRepository;
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
 * REST controller for managing CompanyType.
 */
@RestController
@RequestMapping("/api")
public class CompanyTypeResource {

    private final Logger log = LoggerFactory.getLogger(CompanyTypeResource.class);
        
    @Inject
    private CompanyTypeRepository companyTypeRepository;
    
    @Inject
    private CompanyTypeSearchRepository companyTypeSearchRepository;
    
    /**
     * POST  /companyTypes -> Create a new companyType.
     */
    @RequestMapping(value = "/companyTypes",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CompanyType> createCompanyType(@Valid @RequestBody CompanyType companyType) throws URISyntaxException {
        log.debug("REST request to save CompanyType : {}", companyType);
        if (companyType.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("companyType", "idexists", "A new companyType cannot already have an ID")).body(null);
        }
        CompanyType result = companyTypeRepository.save(companyType);
        companyTypeSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/companyTypes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("companyType", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /companyTypes -> Updates an existing companyType.
     */
    @RequestMapping(value = "/companyTypes",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CompanyType> updateCompanyType(@Valid @RequestBody CompanyType companyType) throws URISyntaxException {
        log.debug("REST request to update CompanyType : {}", companyType);
        if (companyType.getId() == null) {
            return createCompanyType(companyType);
        }
        CompanyType result = companyTypeRepository.save(companyType);
        companyTypeSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("companyType", companyType.getId().toString()))
            .body(result);
    }

    /**
     * GET  /companyTypes -> get all the companyTypes.
     */
    @RequestMapping(value = "/companyTypes",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<CompanyType> getAllCompanyTypes() {
        log.debug("REST request to get all CompanyTypes");
        return companyTypeRepository.findAll();
            }

    /**
     * GET  /companyTypes/:id -> get the "id" companyType.
     */
    @RequestMapping(value = "/companyTypes/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CompanyType> getCompanyType(@PathVariable Long id) {
        log.debug("REST request to get CompanyType : {}", id);
        CompanyType companyType = companyTypeRepository.findOne(id);
        return Optional.ofNullable(companyType)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /companyTypes/:id -> delete the "id" companyType.
     */
    @RequestMapping(value = "/companyTypes/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteCompanyType(@PathVariable Long id) {
        log.debug("REST request to delete CompanyType : {}", id);
        companyTypeRepository.delete(id);
        companyTypeSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("companyType", id.toString())).build();
    }

    /**
     * SEARCH  /_search/companyTypes/:query -> search for the companyType corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/companyTypes/{query:.+}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<CompanyType> searchCompanyTypes(@PathVariable String query) {
        log.debug("REST request to search CompanyTypes for query {}", query);
        return StreamSupport
            .stream(companyTypeSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
