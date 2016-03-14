package com.crescendo.ats.web.rest;

import com.crescendo.ats.Application;
import com.crescendo.ats.domain.WorkStatus;
import com.crescendo.ats.repository.WorkStatusRepository;
import com.crescendo.ats.repository.search.WorkStatusSearchRepository;

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
 * Test class for the WorkStatusResource REST controller.
 *
 * @see WorkStatusResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class WorkStatusResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    @Inject
    private WorkStatusRepository workStatusRepository;

    @Inject
    private WorkStatusSearchRepository workStatusSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restWorkStatusMockMvc;

    private WorkStatus workStatus;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        WorkStatusResource workStatusResource = new WorkStatusResource();
        ReflectionTestUtils.setField(workStatusResource, "workStatusSearchRepository", workStatusSearchRepository);
        ReflectionTestUtils.setField(workStatusResource, "workStatusRepository", workStatusRepository);
        this.restWorkStatusMockMvc = MockMvcBuilders.standaloneSetup(workStatusResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        workStatus = new WorkStatus();
        workStatus.setName(DEFAULT_NAME);
        workStatus.setDescription(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createWorkStatus() throws Exception {
        int databaseSizeBeforeCreate = workStatusRepository.findAll().size();

        // Create the WorkStatus

        restWorkStatusMockMvc.perform(post("/api/workStatuss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(workStatus)))
                .andExpect(status().isCreated());

        // Validate the WorkStatus in the database
        List<WorkStatus> workStatuss = workStatusRepository.findAll();
        assertThat(workStatuss).hasSize(databaseSizeBeforeCreate + 1);
        WorkStatus testWorkStatus = workStatuss.get(workStatuss.size() - 1);
        assertThat(testWorkStatus.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testWorkStatus.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = workStatusRepository.findAll().size();
        // set the field null
        workStatus.setName(null);

        // Create the WorkStatus, which fails.

        restWorkStatusMockMvc.perform(post("/api/workStatuss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(workStatus)))
                .andExpect(status().isBadRequest());

        List<WorkStatus> workStatuss = workStatusRepository.findAll();
        assertThat(workStatuss).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllWorkStatuss() throws Exception {
        // Initialize the database
        workStatusRepository.saveAndFlush(workStatus);

        // Get all the workStatuss
        restWorkStatusMockMvc.perform(get("/api/workStatuss?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(workStatus.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getWorkStatus() throws Exception {
        // Initialize the database
        workStatusRepository.saveAndFlush(workStatus);

        // Get the workStatus
        restWorkStatusMockMvc.perform(get("/api/workStatuss/{id}", workStatus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(workStatus.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingWorkStatus() throws Exception {
        // Get the workStatus
        restWorkStatusMockMvc.perform(get("/api/workStatuss/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateWorkStatus() throws Exception {
        // Initialize the database
        workStatusRepository.saveAndFlush(workStatus);

		int databaseSizeBeforeUpdate = workStatusRepository.findAll().size();

        // Update the workStatus
        workStatus.setName(UPDATED_NAME);
        workStatus.setDescription(UPDATED_DESCRIPTION);

        restWorkStatusMockMvc.perform(put("/api/workStatuss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(workStatus)))
                .andExpect(status().isOk());

        // Validate the WorkStatus in the database
        List<WorkStatus> workStatuss = workStatusRepository.findAll();
        assertThat(workStatuss).hasSize(databaseSizeBeforeUpdate);
        WorkStatus testWorkStatus = workStatuss.get(workStatuss.size() - 1);
        assertThat(testWorkStatus.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testWorkStatus.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void deleteWorkStatus() throws Exception {
        // Initialize the database
        workStatusRepository.saveAndFlush(workStatus);

		int databaseSizeBeforeDelete = workStatusRepository.findAll().size();

        // Get the workStatus
        restWorkStatusMockMvc.perform(delete("/api/workStatuss/{id}", workStatus.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<WorkStatus> workStatuss = workStatusRepository.findAll();
        assertThat(workStatuss).hasSize(databaseSizeBeforeDelete - 1);
    }
}
