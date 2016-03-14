package com.crescendo.ats.web.rest;

import com.crescendo.ats.Application;
import com.crescendo.ats.domain.CompanyStatus;
import com.crescendo.ats.repository.CompanyStatusRepository;
import com.crescendo.ats.repository.search.CompanyStatusSearchRepository;

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
 * Test class for the CompanyStatusResource REST controller.
 *
 * @see CompanyStatusResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class CompanyStatusResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    @Inject
    private CompanyStatusRepository companyStatusRepository;

    @Inject
    private CompanyStatusSearchRepository companyStatusSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restCompanyStatusMockMvc;

    private CompanyStatus companyStatus;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CompanyStatusResource companyStatusResource = new CompanyStatusResource();
        ReflectionTestUtils.setField(companyStatusResource, "companyStatusSearchRepository", companyStatusSearchRepository);
        ReflectionTestUtils.setField(companyStatusResource, "companyStatusRepository", companyStatusRepository);
        this.restCompanyStatusMockMvc = MockMvcBuilders.standaloneSetup(companyStatusResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        companyStatus = new CompanyStatus();
        companyStatus.setName(DEFAULT_NAME);
        companyStatus.setDescription(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createCompanyStatus() throws Exception {
        int databaseSizeBeforeCreate = companyStatusRepository.findAll().size();

        // Create the CompanyStatus

        restCompanyStatusMockMvc.perform(post("/api/companyStatuss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(companyStatus)))
                .andExpect(status().isCreated());

        // Validate the CompanyStatus in the database
        List<CompanyStatus> companyStatuss = companyStatusRepository.findAll();
        assertThat(companyStatuss).hasSize(databaseSizeBeforeCreate + 1);
        CompanyStatus testCompanyStatus = companyStatuss.get(companyStatuss.size() - 1);
        assertThat(testCompanyStatus.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCompanyStatus.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = companyStatusRepository.findAll().size();
        // set the field null
        companyStatus.setName(null);

        // Create the CompanyStatus, which fails.

        restCompanyStatusMockMvc.perform(post("/api/companyStatuss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(companyStatus)))
                .andExpect(status().isBadRequest());

        List<CompanyStatus> companyStatuss = companyStatusRepository.findAll();
        assertThat(companyStatuss).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCompanyStatuss() throws Exception {
        // Initialize the database
        companyStatusRepository.saveAndFlush(companyStatus);

        // Get all the companyStatuss
        restCompanyStatusMockMvc.perform(get("/api/companyStatuss?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(companyStatus.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getCompanyStatus() throws Exception {
        // Initialize the database
        companyStatusRepository.saveAndFlush(companyStatus);

        // Get the companyStatus
        restCompanyStatusMockMvc.perform(get("/api/companyStatuss/{id}", companyStatus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(companyStatus.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCompanyStatus() throws Exception {
        // Get the companyStatus
        restCompanyStatusMockMvc.perform(get("/api/companyStatuss/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCompanyStatus() throws Exception {
        // Initialize the database
        companyStatusRepository.saveAndFlush(companyStatus);

		int databaseSizeBeforeUpdate = companyStatusRepository.findAll().size();

        // Update the companyStatus
        companyStatus.setName(UPDATED_NAME);
        companyStatus.setDescription(UPDATED_DESCRIPTION);

        restCompanyStatusMockMvc.perform(put("/api/companyStatuss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(companyStatus)))
                .andExpect(status().isOk());

        // Validate the CompanyStatus in the database
        List<CompanyStatus> companyStatuss = companyStatusRepository.findAll();
        assertThat(companyStatuss).hasSize(databaseSizeBeforeUpdate);
        CompanyStatus testCompanyStatus = companyStatuss.get(companyStatuss.size() - 1);
        assertThat(testCompanyStatus.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCompanyStatus.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void deleteCompanyStatus() throws Exception {
        // Initialize the database
        companyStatusRepository.saveAndFlush(companyStatus);

		int databaseSizeBeforeDelete = companyStatusRepository.findAll().size();

        // Get the companyStatus
        restCompanyStatusMockMvc.perform(delete("/api/companyStatuss/{id}", companyStatus.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<CompanyStatus> companyStatuss = companyStatusRepository.findAll();
        assertThat(companyStatuss).hasSize(databaseSizeBeforeDelete - 1);
    }
}
