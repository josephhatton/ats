package com.crescendo.ats.web.rest;

import com.crescendo.ats.Application;
import com.crescendo.ats.domain.ApplicantStatus;
import com.crescendo.ats.repository.ApplicantStatusRepository;
import com.crescendo.ats.repository.search.ApplicantStatusSearchRepository;

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
 * Test class for the ApplicantStatusResource REST controller.
 *
 * @see ApplicantStatusResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class ApplicantStatusResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    @Inject
    private ApplicantStatusRepository applicantStatusRepository;

    @Inject
    private ApplicantStatusSearchRepository applicantStatusSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restApplicantStatusMockMvc;

    private ApplicantStatus applicantStatus;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ApplicantStatusResource applicantStatusResource = new ApplicantStatusResource();
        ReflectionTestUtils.setField(applicantStatusResource, "applicantStatusSearchRepository", applicantStatusSearchRepository);
        ReflectionTestUtils.setField(applicantStatusResource, "applicantStatusRepository", applicantStatusRepository);
        this.restApplicantStatusMockMvc = MockMvcBuilders.standaloneSetup(applicantStatusResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        applicantStatus = new ApplicantStatus();
        applicantStatus.setName(DEFAULT_NAME);
        applicantStatus.setDescription(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createApplicantStatus() throws Exception {
        int databaseSizeBeforeCreate = applicantStatusRepository.findAll().size();

        // Create the ApplicantStatus

        restApplicantStatusMockMvc.perform(post("/api/applicantStatuss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(applicantStatus)))
                .andExpect(status().isCreated());

        // Validate the ApplicantStatus in the database
        List<ApplicantStatus> applicantStatuss = applicantStatusRepository.findAll();
        assertThat(applicantStatuss).hasSize(databaseSizeBeforeCreate + 1);
        ApplicantStatus testApplicantStatus = applicantStatuss.get(applicantStatuss.size() - 1);
        assertThat(testApplicantStatus.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testApplicantStatus.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllApplicantStatuss() throws Exception {
        // Initialize the database
        applicantStatusRepository.saveAndFlush(applicantStatus);

        // Get all the applicantStatuss
        restApplicantStatusMockMvc.perform(get("/api/applicantStatuss?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(applicantStatus.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getApplicantStatus() throws Exception {
        // Initialize the database
        applicantStatusRepository.saveAndFlush(applicantStatus);

        // Get the applicantStatus
        restApplicantStatusMockMvc.perform(get("/api/applicantStatuss/{id}", applicantStatus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(applicantStatus.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingApplicantStatus() throws Exception {
        // Get the applicantStatus
        restApplicantStatusMockMvc.perform(get("/api/applicantStatuss/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateApplicantStatus() throws Exception {
        // Initialize the database
        applicantStatusRepository.saveAndFlush(applicantStatus);

		int databaseSizeBeforeUpdate = applicantStatusRepository.findAll().size();

        // Update the applicantStatus
        applicantStatus.setName(UPDATED_NAME);
        applicantStatus.setDescription(UPDATED_DESCRIPTION);

        restApplicantStatusMockMvc.perform(put("/api/applicantStatuss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(applicantStatus)))
                .andExpect(status().isOk());

        // Validate the ApplicantStatus in the database
        List<ApplicantStatus> applicantStatuss = applicantStatusRepository.findAll();
        assertThat(applicantStatuss).hasSize(databaseSizeBeforeUpdate);
        ApplicantStatus testApplicantStatus = applicantStatuss.get(applicantStatuss.size() - 1);
        assertThat(testApplicantStatus.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testApplicantStatus.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void deleteApplicantStatus() throws Exception {
        // Initialize the database
        applicantStatusRepository.saveAndFlush(applicantStatus);

		int databaseSizeBeforeDelete = applicantStatusRepository.findAll().size();

        // Get the applicantStatus
        restApplicantStatusMockMvc.perform(delete("/api/applicantStatuss/{id}", applicantStatus.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<ApplicantStatus> applicantStatuss = applicantStatusRepository.findAll();
        assertThat(applicantStatuss).hasSize(databaseSizeBeforeDelete - 1);
    }
}
