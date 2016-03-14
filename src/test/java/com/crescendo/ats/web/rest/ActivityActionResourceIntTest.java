package com.crescendo.ats.web.rest;

import com.crescendo.ats.Application;
import com.crescendo.ats.domain.ActivityAction;
import com.crescendo.ats.repository.ActivityActionRepository;
import com.crescendo.ats.repository.search.ActivityActionSearchRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the ActivityActionResource REST controller.
 *
 * @see ActivityActionResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class ActivityActionResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    @Inject
    private ActivityActionRepository activityActionRepository;

    @Inject
    private ActivityActionSearchRepository activityActionSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restActivityActionMockMvc;

    private ActivityAction activityAction;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ActivityActionResource activityActionResource = new ActivityActionResource();
        ReflectionTestUtils.setField(activityActionResource, "activityActionSearchRepository", activityActionSearchRepository);
        ReflectionTestUtils.setField(activityActionResource, "activityActionRepository", activityActionRepository);
        this.restActivityActionMockMvc = MockMvcBuilders.standaloneSetup(activityActionResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        activityAction = new ActivityAction();
        activityAction.setName(DEFAULT_NAME);
        activityAction.setDescription(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createActivityAction() throws Exception {
        int databaseSizeBeforeCreate = activityActionRepository.findAll().size();

        // Create the ActivityAction

        restActivityActionMockMvc.perform(post("/api/activityActions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(activityAction)))
                .andExpect(status().isCreated());

        // Validate the ActivityAction in the database
        List<ActivityAction> activityActions = activityActionRepository.findAll();
        assertThat(activityActions).hasSize(databaseSizeBeforeCreate + 1);
        ActivityAction testActivityAction = activityActions.get(activityActions.size() - 1);
        assertThat(testActivityAction.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testActivityAction.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = activityActionRepository.findAll().size();
        // set the field null
        activityAction.setName(null);

        // Create the ActivityAction, which fails.

        restActivityActionMockMvc.perform(post("/api/activityActions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(activityAction)))
                .andExpect(status().isBadRequest());

        List<ActivityAction> activityActions = activityActionRepository.findAll();
        assertThat(activityActions).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllActivityActions() throws Exception {
        // Initialize the database
        activityActionRepository.saveAndFlush(activityAction);

        // Get all the activityActions
        restActivityActionMockMvc.perform(get("/api/activityActions?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(activityAction.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getActivityAction() throws Exception {
        // Initialize the database
        activityActionRepository.saveAndFlush(activityAction);

        // Get the activityAction
        restActivityActionMockMvc.perform(get("/api/activityActions/{id}", activityAction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(activityAction.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingActivityAction() throws Exception {
        // Get the activityAction
        restActivityActionMockMvc.perform(get("/api/activityActions/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateActivityAction() throws Exception {
        // Initialize the database
        activityActionRepository.saveAndFlush(activityAction);

		int databaseSizeBeforeUpdate = activityActionRepository.findAll().size();

        // Update the activityAction
        activityAction.setName(UPDATED_NAME);
        activityAction.setDescription(UPDATED_DESCRIPTION);

        restActivityActionMockMvc.perform(put("/api/activityActions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(activityAction)))
                .andExpect(status().isOk());

        // Validate the ActivityAction in the database
        List<ActivityAction> activityActions = activityActionRepository.findAll();
        assertThat(activityActions).hasSize(databaseSizeBeforeUpdate);
        ActivityAction testActivityAction = activityActions.get(activityActions.size() - 1);
        assertThat(testActivityAction.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testActivityAction.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void deleteActivityAction() throws Exception {
        // Initialize the database
        activityActionRepository.saveAndFlush(activityAction);

		int databaseSizeBeforeDelete = activityActionRepository.findAll().size();

        // Get the activityAction
        restActivityActionMockMvc.perform(delete("/api/activityActions/{id}", activityAction.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<ActivityAction> activityActions = activityActionRepository.findAll();
        assertThat(activityActions).hasSize(databaseSizeBeforeDelete - 1);
    }
}
