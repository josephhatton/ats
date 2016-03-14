package com.crescendo.ats.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.crescendo.ats.domain.SkillCategory;
import com.crescendo.ats.repository.SkillCategoryRepository;
import com.crescendo.ats.repository.search.SkillCategorySearchRepository;
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
 * REST controller for managing SkillCategory.
 */
@RestController
@RequestMapping("/api")
public class SkillCategoryResource {

    private final Logger log = LoggerFactory.getLogger(SkillCategoryResource.class);
        
    @Inject
    private SkillCategoryRepository skillCategoryRepository;
    
    @Inject
    private SkillCategorySearchRepository skillCategorySearchRepository;
    
    /**
     * POST  /skillCategorys -> Create a new skillCategory.
     */
    @RequestMapping(value = "/skillCategorys",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SkillCategory> createSkillCategory(@Valid @RequestBody SkillCategory skillCategory) throws URISyntaxException {
        log.debug("REST request to save SkillCategory : {}", skillCategory);
        if (skillCategory.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("skillCategory", "idexists", "A new skillCategory cannot already have an ID")).body(null);
        }
        SkillCategory result = skillCategoryRepository.save(skillCategory);
        skillCategorySearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/skillCategorys/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("skillCategory", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /skillCategorys -> Updates an existing skillCategory.
     */
    @RequestMapping(value = "/skillCategorys",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SkillCategory> updateSkillCategory(@Valid @RequestBody SkillCategory skillCategory) throws URISyntaxException {
        log.debug("REST request to update SkillCategory : {}", skillCategory);
        if (skillCategory.getId() == null) {
            return createSkillCategory(skillCategory);
        }
        SkillCategory result = skillCategoryRepository.save(skillCategory);
        skillCategorySearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("skillCategory", skillCategory.getId().toString()))
            .body(result);
    }

    /**
     * GET  /skillCategorys -> get all the skillCategorys.
     */
    @RequestMapping(value = "/skillCategorys",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<SkillCategory> getAllSkillCategorys() {
        log.debug("REST request to get all SkillCategorys");
        return skillCategoryRepository.findAll();
            }

    /**
     * GET  /skillCategorys/:id -> get the "id" skillCategory.
     */
    @RequestMapping(value = "/skillCategorys/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SkillCategory> getSkillCategory(@PathVariable Long id) {
        log.debug("REST request to get SkillCategory : {}", id);
        SkillCategory skillCategory = skillCategoryRepository.findOne(id);
        return Optional.ofNullable(skillCategory)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /skillCategorys/:id -> delete the "id" skillCategory.
     */
    @RequestMapping(value = "/skillCategorys/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteSkillCategory(@PathVariable Long id) {
        log.debug("REST request to delete SkillCategory : {}", id);
        skillCategoryRepository.delete(id);
        skillCategorySearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("skillCategory", id.toString())).build();
    }

    /**
     * SEARCH  /_search/skillCategorys/:query -> search for the skillCategory corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/skillCategorys/{query:.+}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<SkillCategory> searchSkillCategorys(@PathVariable String query) {
        log.debug("REST request to search SkillCategorys for query {}", query);
        return StreamSupport
            .stream(skillCategorySearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
