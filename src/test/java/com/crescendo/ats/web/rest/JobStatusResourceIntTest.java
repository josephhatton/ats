package com.crescendo.ats.web.rest;

import com.crescendo.ats.Application;
import com.crescendo.ats.domain.JobStatus;
import com.crescendo.ats.repository.JobStatusRepository;
import com.crescendo.ats.repository.search.JobStatusSearchRepository;

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
 * Test class for the JobStatusResource REST controller.
 *
 * @see JobStatusResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class JobStatusResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    @Inject
    private JobStatusRepository jobStatusRepository;

    @Inject
    private JobStatusSearchRepository jobStatusSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restJobStatusMockMvc;

    private JobStatus jobStatus;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        JobStatusResource jobStatusResource = new JobStatusResource();
        ReflectionTestUtils.setField(jobStatusResource, "jobStatusSearchRepository", jobStatusSearchRepository);
        ReflectionTestUtils.setField(jobStatusResource, "jobStatusRepository", jobStatusRepository);
        this.restJobStatusMockMvc = MockMvcBuilders.standaloneSetup(jobStatusResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        jobStatus = new JobStatus();
        jobStatus.setName(DEFAULT_NAME);
        jobStatus.setDescription(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createJobStatus() throws Exception {
        int databaseSizeBeforeCreate = jobStatusRepository.findAll().size();

        // Create the JobStatus

        restJobStatusMockMvc.perform(post("/api/jobStatuss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(jobStatus)))
                .andExpect(status().isCreated());

        // Validate the JobStatus in the database
        List<JobStatus> jobStatuss = jobStatusRepository.findAll();
        assertThat(jobStatuss).hasSize(databaseSizeBeforeCreate + 1);
        JobStatus testJobStatus = jobStatuss.get(jobStatuss.size() - 1);
        assertThat(testJobStatus.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testJobStatus.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = jobStatusRepository.findAll().size();
        // set the field null
        jobStatus.setName(null);

        // Create the JobStatus, which fails.

        restJobStatusMockMvc.perform(post("/api/jobStatuss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(jobStatus)))
                .andExpect(status().isBadRequest());

        List<JobStatus> jobStatuss = jobStatusRepository.findAll();
        assertThat(jobStatuss).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllJobStatuss() throws Exception {
        // Initialize the database
        jobStatusRepository.saveAndFlush(jobStatus);

        // Get all the jobStatuss
        restJobStatusMockMvc.perform(get("/api/jobStatuss?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(jobStatus.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getJobStatus() throws Exception {
        // Initialize the database
        jobStatusRepository.saveAndFlush(jobStatus);

        // Get the jobStatus
        restJobStatusMockMvc.perform(get("/api/jobStatuss/{id}", jobStatus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(jobStatus.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingJobStatus() throws Exception {
        // Get the jobStatus
        restJobStatusMockMvc.perform(get("/api/jobStatuss/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateJobStatus() throws Exception {
        // Initialize the database
        jobStatusRepository.saveAndFlush(jobStatus);

		int databaseSizeBeforeUpdate = jobStatusRepository.findAll().size();

        // Update the jobStatus
        jobStatus.setName(UPDATED_NAME);
        jobStatus.setDescription(UPDATED_DESCRIPTION);

        restJobStatusMockMvc.perform(put("/api/jobStatuss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(jobStatus)))
                .andExpect(status().isOk());

        // Validate the JobStatus in the database
        List<JobStatus> jobStatuss = jobStatusRepository.findAll();
        assertThat(jobStatuss).hasSize(databaseSizeBeforeUpdate);
        JobStatus testJobStatus = jobStatuss.get(jobStatuss.size() - 1);
        assertThat(testJobStatus.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testJobStatus.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void deleteJobStatus() throws Exception {
        // Initialize the database
        jobStatusRepository.saveAndFlush(jobStatus);

		int databaseSizeBeforeDelete = jobStatusRepository.findAll().size();

        // Get the jobStatus
        restJobStatusMockMvc.perform(delete("/api/jobStatuss/{id}", jobStatus.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<JobStatus> jobStatuss = jobStatusRepository.findAll();
        assertThat(jobStatuss).hasSize(databaseSizeBeforeDelete - 1);
    }
}
