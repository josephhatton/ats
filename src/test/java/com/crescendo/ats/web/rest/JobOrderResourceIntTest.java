package com.crescendo.ats.web.rest;

import com.crescendo.ats.Application;
import com.crescendo.ats.domain.JobOrder;
import com.crescendo.ats.repository.JobOrderRepository;
import com.crescendo.ats.repository.search.JobOrderSearchRepository;

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
 * Test class for the JobOrderResource REST controller.
 *
 * @see JobOrderResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class JobOrderResourceIntTest {

    private static final String DEFAULT_TITLE = "AAAAA";
    private static final String UPDATED_TITLE = "BBBBB";
    private static final String DEFAULT_DURATION = "AAAAA";
    private static final String UPDATED_DURATION = "BBBBB";
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    @Inject
    private JobOrderRepository jobOrderRepository;

    @Inject
    private JobOrderSearchRepository jobOrderSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restJobOrderMockMvc;

    private JobOrder jobOrder;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        JobOrderResource jobOrderResource = new JobOrderResource();
        ReflectionTestUtils.setField(jobOrderResource, "jobOrderSearchRepository", jobOrderSearchRepository);
        ReflectionTestUtils.setField(jobOrderResource, "jobOrderRepository", jobOrderRepository);
        this.restJobOrderMockMvc = MockMvcBuilders.standaloneSetup(jobOrderResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        jobOrder = new JobOrder();
        jobOrder.setTitle(DEFAULT_TITLE);
        jobOrder.setDuration(DEFAULT_DURATION);
        jobOrder.setDescription(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createJobOrder() throws Exception {
        int databaseSizeBeforeCreate = jobOrderRepository.findAll().size();

        // Create the JobOrder

        restJobOrderMockMvc.perform(post("/api/jobOrders")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(jobOrder)))
                .andExpect(status().isCreated());

        // Validate the JobOrder in the database
        List<JobOrder> jobOrders = jobOrderRepository.findAll();
        assertThat(jobOrders).hasSize(databaseSizeBeforeCreate + 1);
        JobOrder testJobOrder = jobOrders.get(jobOrders.size() - 1);
        assertThat(testJobOrder.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testJobOrder.getDuration()).isEqualTo(DEFAULT_DURATION);
        assertThat(testJobOrder.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllJobOrders() throws Exception {
        // Initialize the database
        jobOrderRepository.saveAndFlush(jobOrder);

        // Get all the jobOrders
        restJobOrderMockMvc.perform(get("/api/jobOrders?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(jobOrder.getId().intValue())))
                .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
                .andExpect(jsonPath("$.[*].duration").value(hasItem(DEFAULT_DURATION.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getJobOrder() throws Exception {
        // Initialize the database
        jobOrderRepository.saveAndFlush(jobOrder);

        // Get the jobOrder
        restJobOrderMockMvc.perform(get("/api/jobOrders/{id}", jobOrder.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(jobOrder.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.duration").value(DEFAULT_DURATION.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingJobOrder() throws Exception {
        // Get the jobOrder
        restJobOrderMockMvc.perform(get("/api/jobOrders/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateJobOrder() throws Exception {
        // Initialize the database
        jobOrderRepository.saveAndFlush(jobOrder);

		int databaseSizeBeforeUpdate = jobOrderRepository.findAll().size();

        // Update the jobOrder
        jobOrder.setTitle(UPDATED_TITLE);
        jobOrder.setDuration(UPDATED_DURATION);
        jobOrder.setDescription(UPDATED_DESCRIPTION);

        restJobOrderMockMvc.perform(put("/api/jobOrders")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(jobOrder)))
                .andExpect(status().isOk());

        // Validate the JobOrder in the database
        List<JobOrder> jobOrders = jobOrderRepository.findAll();
        assertThat(jobOrders).hasSize(databaseSizeBeforeUpdate);
        JobOrder testJobOrder = jobOrders.get(jobOrders.size() - 1);
        assertThat(testJobOrder.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testJobOrder.getDuration()).isEqualTo(UPDATED_DURATION);
        assertThat(testJobOrder.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void deleteJobOrder() throws Exception {
        // Initialize the database
        jobOrderRepository.saveAndFlush(jobOrder);

		int databaseSizeBeforeDelete = jobOrderRepository.findAll().size();

        // Get the jobOrder
        restJobOrderMockMvc.perform(delete("/api/jobOrders/{id}", jobOrder.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<JobOrder> jobOrders = jobOrderRepository.findAll();
        assertThat(jobOrders).hasSize(databaseSizeBeforeDelete - 1);
    }
}
