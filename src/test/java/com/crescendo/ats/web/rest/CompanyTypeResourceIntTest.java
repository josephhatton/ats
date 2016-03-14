package com.crescendo.ats.web.rest;

import com.crescendo.ats.Application;
import com.crescendo.ats.domain.CompanyType;
import com.crescendo.ats.repository.CompanyTypeRepository;
import com.crescendo.ats.repository.search.CompanyTypeSearchRepository;

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
 * Test class for the CompanyTypeResource REST controller.
 *
 * @see CompanyTypeResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class CompanyTypeResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    @Inject
    private CompanyTypeRepository companyTypeRepository;

    @Inject
    private CompanyTypeSearchRepository companyTypeSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restCompanyTypeMockMvc;

    private CompanyType companyType;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CompanyTypeResource companyTypeResource = new CompanyTypeResource();
        ReflectionTestUtils.setField(companyTypeResource, "companyTypeSearchRepository", companyTypeSearchRepository);
        ReflectionTestUtils.setField(companyTypeResource, "companyTypeRepository", companyTypeRepository);
        this.restCompanyTypeMockMvc = MockMvcBuilders.standaloneSetup(companyTypeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        companyType = new CompanyType();
        companyType.setName(DEFAULT_NAME);
        companyType.setDescription(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createCompanyType() throws Exception {
        int databaseSizeBeforeCreate = companyTypeRepository.findAll().size();

        // Create the CompanyType

        restCompanyTypeMockMvc.perform(post("/api/companyTypes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(companyType)))
                .andExpect(status().isCreated());

        // Validate the CompanyType in the database
        List<CompanyType> companyTypes = companyTypeRepository.findAll();
        assertThat(companyTypes).hasSize(databaseSizeBeforeCreate + 1);
        CompanyType testCompanyType = companyTypes.get(companyTypes.size() - 1);
        assertThat(testCompanyType.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCompanyType.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = companyTypeRepository.findAll().size();
        // set the field null
        companyType.setName(null);

        // Create the CompanyType, which fails.

        restCompanyTypeMockMvc.perform(post("/api/companyTypes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(companyType)))
                .andExpect(status().isBadRequest());

        List<CompanyType> companyTypes = companyTypeRepository.findAll();
        assertThat(companyTypes).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCompanyTypes() throws Exception {
        // Initialize the database
        companyTypeRepository.saveAndFlush(companyType);

        // Get all the companyTypes
        restCompanyTypeMockMvc.perform(get("/api/companyTypes?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(companyType.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getCompanyType() throws Exception {
        // Initialize the database
        companyTypeRepository.saveAndFlush(companyType);

        // Get the companyType
        restCompanyTypeMockMvc.perform(get("/api/companyTypes/{id}", companyType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(companyType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCompanyType() throws Exception {
        // Get the companyType
        restCompanyTypeMockMvc.perform(get("/api/companyTypes/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCompanyType() throws Exception {
        // Initialize the database
        companyTypeRepository.saveAndFlush(companyType);

		int databaseSizeBeforeUpdate = companyTypeRepository.findAll().size();

        // Update the companyType
        companyType.setName(UPDATED_NAME);
        companyType.setDescription(UPDATED_DESCRIPTION);

        restCompanyTypeMockMvc.perform(put("/api/companyTypes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(companyType)))
                .andExpect(status().isOk());

        // Validate the CompanyType in the database
        List<CompanyType> companyTypes = companyTypeRepository.findAll();
        assertThat(companyTypes).hasSize(databaseSizeBeforeUpdate);
        CompanyType testCompanyType = companyTypes.get(companyTypes.size() - 1);
        assertThat(testCompanyType.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCompanyType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void deleteCompanyType() throws Exception {
        // Initialize the database
        companyTypeRepository.saveAndFlush(companyType);

		int databaseSizeBeforeDelete = companyTypeRepository.findAll().size();

        // Get the companyType
        restCompanyTypeMockMvc.perform(delete("/api/companyTypes/{id}", companyType.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<CompanyType> companyTypes = companyTypeRepository.findAll();
        assertThat(companyTypes).hasSize(databaseSizeBeforeDelete - 1);
    }
}
