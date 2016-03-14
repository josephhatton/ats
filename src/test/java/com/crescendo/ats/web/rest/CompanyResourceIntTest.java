package com.crescendo.ats.web.rest;

import com.crescendo.ats.Application;
import com.crescendo.ats.domain.Company;
import com.crescendo.ats.repository.CompanyRepository;
import com.crescendo.ats.repository.search.CompanySearchRepository;

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
 * Test class for the CompanyResource REST controller.
 *
 * @see CompanyResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class CompanyResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";
    private static final String DEFAULT_WEBSITE = "AAAAA";
    private static final String UPDATED_WEBSITE = "BBBBB";
    private static final String DEFAULT_INDUSTRY = "AAAAA";
    private static final String UPDATED_INDUSTRY = "BBBBB";
    private static final String DEFAULT_REVENUE = "AAAAA";
    private static final String UPDATED_REVENUE = "BBBBB";
    private static final String DEFAULT_EMPLOYEES = "AAAAA";
    private static final String UPDATED_EMPLOYEES = "BBBBB";

    private static final Integer DEFAULT_IS_DELETED = 1;
    private static final Integer UPDATED_IS_DELETED = 2;

    @Inject
    private CompanyRepository companyRepository;

    @Inject
    private CompanySearchRepository companySearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restCompanyMockMvc;

    private Company company;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CompanyResource companyResource = new CompanyResource();
        ReflectionTestUtils.setField(companyResource, "companySearchRepository", companySearchRepository);
        ReflectionTestUtils.setField(companyResource, "companyRepository", companyRepository);
        this.restCompanyMockMvc = MockMvcBuilders.standaloneSetup(companyResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        company = new Company();
        company.setName(DEFAULT_NAME);
        company.setWebsite(DEFAULT_WEBSITE);
        company.setIndustry(DEFAULT_INDUSTRY);
        company.setRevenue(DEFAULT_REVENUE);
        company.setEmployees(DEFAULT_EMPLOYEES);
        company.setIsDeleted(DEFAULT_IS_DELETED);
    }

    @Test
    @Transactional
    public void createCompany() throws Exception {
        int databaseSizeBeforeCreate = companyRepository.findAll().size();

        // Create the Company

        restCompanyMockMvc.perform(post("/api/companys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(company)))
                .andExpect(status().isCreated());

        // Validate the Company in the database
        List<Company> companys = companyRepository.findAll();
        assertThat(companys).hasSize(databaseSizeBeforeCreate + 1);
        Company testCompany = companys.get(companys.size() - 1);
        assertThat(testCompany.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCompany.getWebsite()).isEqualTo(DEFAULT_WEBSITE);
        assertThat(testCompany.getIndustry()).isEqualTo(DEFAULT_INDUSTRY);
        assertThat(testCompany.getRevenue()).isEqualTo(DEFAULT_REVENUE);
        assertThat(testCompany.getEmployees()).isEqualTo(DEFAULT_EMPLOYEES);
        assertThat(testCompany.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = companyRepository.findAll().size();
        // set the field null
        company.setName(null);

        // Create the Company, which fails.

        restCompanyMockMvc.perform(post("/api/companys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(company)))
                .andExpect(status().isBadRequest());

        List<Company> companys = companyRepository.findAll();
        assertThat(companys).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkIsDeletedIsRequired() throws Exception {
        int databaseSizeBeforeTest = companyRepository.findAll().size();
        // set the field null
        company.setIsDeleted(null);

        // Create the Company, which fails.

        restCompanyMockMvc.perform(post("/api/companys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(company)))
                .andExpect(status().isBadRequest());

        List<Company> companys = companyRepository.findAll();
        assertThat(companys).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCompanys() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companys
        restCompanyMockMvc.perform(get("/api/companys?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(company.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].website").value(hasItem(DEFAULT_WEBSITE.toString())))
                .andExpect(jsonPath("$.[*].industry").value(hasItem(DEFAULT_INDUSTRY.toString())))
                .andExpect(jsonPath("$.[*].revenue").value(hasItem(DEFAULT_REVENUE.toString())))
                .andExpect(jsonPath("$.[*].employees").value(hasItem(DEFAULT_EMPLOYEES.toString())))
                .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED)));
    }

    @Test
    @Transactional
    public void getCompany() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get the company
        restCompanyMockMvc.perform(get("/api/companys/{id}", company.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(company.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.website").value(DEFAULT_WEBSITE.toString()))
            .andExpect(jsonPath("$.industry").value(DEFAULT_INDUSTRY.toString()))
            .andExpect(jsonPath("$.revenue").value(DEFAULT_REVENUE.toString()))
            .andExpect(jsonPath("$.employees").value(DEFAULT_EMPLOYEES.toString()))
            .andExpect(jsonPath("$.isDeleted").value(DEFAULT_IS_DELETED));
    }

    @Test
    @Transactional
    public void getNonExistingCompany() throws Exception {
        // Get the company
        restCompanyMockMvc.perform(get("/api/companys/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCompany() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

		int databaseSizeBeforeUpdate = companyRepository.findAll().size();

        // Update the company
        company.setName(UPDATED_NAME);
        company.setWebsite(UPDATED_WEBSITE);
        company.setIndustry(UPDATED_INDUSTRY);
        company.setRevenue(UPDATED_REVENUE);
        company.setEmployees(UPDATED_EMPLOYEES);
        company.setIsDeleted(UPDATED_IS_DELETED);

        restCompanyMockMvc.perform(put("/api/companys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(company)))
                .andExpect(status().isOk());

        // Validate the Company in the database
        List<Company> companys = companyRepository.findAll();
        assertThat(companys).hasSize(databaseSizeBeforeUpdate);
        Company testCompany = companys.get(companys.size() - 1);
        assertThat(testCompany.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCompany.getWebsite()).isEqualTo(UPDATED_WEBSITE);
        assertThat(testCompany.getIndustry()).isEqualTo(UPDATED_INDUSTRY);
        assertThat(testCompany.getRevenue()).isEqualTo(UPDATED_REVENUE);
        assertThat(testCompany.getEmployees()).isEqualTo(UPDATED_EMPLOYEES);
        assertThat(testCompany.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
    }

    @Test
    @Transactional
    public void deleteCompany() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

		int databaseSizeBeforeDelete = companyRepository.findAll().size();

        // Get the company
        restCompanyMockMvc.perform(delete("/api/companys/{id}", company.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Company> companys = companyRepository.findAll();
        assertThat(companys).hasSize(databaseSizeBeforeDelete - 1);
    }
}
