package com.crescendo.ats.web.rest;

import com.crescendo.ats.Application;
import com.crescendo.ats.domain.Activity;
import com.crescendo.ats.repository.ActivityRepository;
import com.crescendo.ats.repository.search.ActivitySearchRepository;

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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the ActivityResource REST controller.
 *
 * @see ActivityResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class ActivityResourceIntTest {

    private static final String DEFAULT_PRIORITY = "AAAAA";
    private static final String UPDATED_PRIORITY = "BBBBB";

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final String DEFAULT_STATUS = "AAAAA";
    private static final String UPDATED_STATUS = "BBBBB";
    private static final String DEFAULT_COMMENTS = "AAAAA";
    private static final String UPDATED_COMMENTS = "BBBBB";

    @Inject
    private ActivityRepository activityRepository;

    @Inject
    private ActivitySearchRepository activitySearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restActivityMockMvc;

    private Activity activity;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ActivityResource activityResource = new ActivityResource();
        ReflectionTestUtils.setField(activityResource, "activitySearchRepository", activitySearchRepository);
        ReflectionTestUtils.setField(activityResource, "activityRepository", activityRepository);
        this.restActivityMockMvc = MockMvcBuilders.standaloneSetup(activityResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        activity = new Activity();
        activity.setPriority(DEFAULT_PRIORITY);
        activity.setStartDate(DEFAULT_START_DATE);
        activity.setStatus(DEFAULT_STATUS);
        activity.setComments(DEFAULT_COMMENTS);
    }

    @Test
    @Transactional
    public void createActivity() throws Exception {
        int databaseSizeBeforeCreate = activityRepository.findAll().size();

        // Create the Activity

        restActivityMockMvc.perform(post("/api/activitys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(activity)))
                .andExpect(status().isCreated());

        // Validate the Activity in the database
        List<Activity> activitys = activityRepository.findAll();
        assertThat(activitys).hasSize(databaseSizeBeforeCreate + 1);
        Activity testActivity = activitys.get(activitys.size() - 1);
        assertThat(testActivity.getPriority()).isEqualTo(DEFAULT_PRIORITY);
        assertThat(testActivity.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testActivity.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testActivity.getComments()).isEqualTo(DEFAULT_COMMENTS);
    }

    @Test
    @Transactional
    public void getAllActivitys() throws Exception {
        // Initialize the database
        activityRepository.saveAndFlush(activity);

        // Get all the activitys
        restActivityMockMvc.perform(get("/api/activitys?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(activity.getId().intValue())))
                .andExpect(jsonPath("$.[*].priority").value(hasItem(DEFAULT_PRIORITY.toString())))
                .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
                .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
                .andExpect(jsonPath("$.[*].comments").value(hasItem(DEFAULT_COMMENTS.toString())));
    }

    @Test
    @Transactional
    public void getActivity() throws Exception {
        // Initialize the database
        activityRepository.saveAndFlush(activity);

        // Get the activity
        restActivityMockMvc.perform(get("/api/activitys/{id}", activity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(activity.getId().intValue()))
            .andExpect(jsonPath("$.priority").value(DEFAULT_PRIORITY.toString()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.comments").value(DEFAULT_COMMENTS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingActivity() throws Exception {
        // Get the activity
        restActivityMockMvc.perform(get("/api/activitys/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateActivity() throws Exception {
        // Initialize the database
        activityRepository.saveAndFlush(activity);

		int databaseSizeBeforeUpdate = activityRepository.findAll().size();

        // Update the activity
        activity.setPriority(UPDATED_PRIORITY);
        activity.setStartDate(UPDATED_START_DATE);
        activity.setStatus(UPDATED_STATUS);
        activity.setComments(UPDATED_COMMENTS);

        restActivityMockMvc.perform(put("/api/activitys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(activity)))
                .andExpect(status().isOk());

        // Validate the Activity in the database
        List<Activity> activitys = activityRepository.findAll();
        assertThat(activitys).hasSize(databaseSizeBeforeUpdate);
        Activity testActivity = activitys.get(activitys.size() - 1);
        assertThat(testActivity.getPriority()).isEqualTo(UPDATED_PRIORITY);
        assertThat(testActivity.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testActivity.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testActivity.getComments()).isEqualTo(UPDATED_COMMENTS);
    }

    @Test
    @Transactional
    public void deleteActivity() throws Exception {
        // Initialize the database
        activityRepository.saveAndFlush(activity);

		int databaseSizeBeforeDelete = activityRepository.findAll().size();

        // Get the activity
        restActivityMockMvc.perform(delete("/api/activitys/{id}", activity.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Activity> activitys = activityRepository.findAll();
        assertThat(activitys).hasSize(databaseSizeBeforeDelete - 1);
    }
}
