package com.crescendo.ats.web.rest;

import com.crescendo.ats.Application;
import com.crescendo.ats.domain.Applicant;
import com.crescendo.ats.repository.ApplicantRepository;
import com.crescendo.ats.repository.search.ApplicantSearchRepository;

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
 * Test class for the ApplicantResource REST controller.
 *
 * @see ApplicantResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class ApplicantResourceIntTest {

    private static final String DEFAULT_FIRST_NAME = "AAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBB";
    private static final String DEFAULT_MIDDLE_NAME = "AAAAA";
    private static final String UPDATED_MIDDLE_NAME = "BBBBB";
    private static final String DEFAULT_LAST_NAME = "AAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBB";
    private static final String DEFAULT_TITLE = "AAAAA";
    private static final String UPDATED_TITLE = "BBBBB";
    private static final String DEFAULT_EMAIL1 = "AAAAA";
    private static final String UPDATED_EMAIL1 = "BBBBB";
    private static final String DEFAULT_HOME_PHONE = "AAAAA";
    private static final String UPDATED_HOME_PHONE = "BBBBB";
    private static final String DEFAULT_CELL_PHONE = "AAAAA";
    private static final String UPDATED_CELL_PHONE = "BBBBB";

    private static final Integer DEFAULT_IS_DELETED = 1;
    private static final Integer UPDATED_IS_DELETED = 2;
    private static final String DEFAULT_NICK_NAME = "AAAAA";
    private static final String UPDATED_NICK_NAME = "BBBBB";
    private static final String DEFAULT_WORK_PHONE = "AAAAA";
    private static final String UPDATED_WORK_PHONE = "BBBBB";
    private static final String DEFAULT_EMAIL2 = "AAAAA";
    private static final String UPDATED_EMAIL2 = "BBBBB";

    @Inject
    private ApplicantRepository applicantRepository;

    @Inject
    private ApplicantSearchRepository applicantSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restApplicantMockMvc;

    private Applicant applicant;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ApplicantResource applicantResource = new ApplicantResource();
        ReflectionTestUtils.setField(applicantResource, "applicantSearchRepository", applicantSearchRepository);
        ReflectionTestUtils.setField(applicantResource, "applicantRepository", applicantRepository);
        this.restApplicantMockMvc = MockMvcBuilders.standaloneSetup(applicantResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        applicant = new Applicant();
        applicant.setFirstName(DEFAULT_FIRST_NAME);
        applicant.setMiddleName(DEFAULT_MIDDLE_NAME);
        applicant.setLastName(DEFAULT_LAST_NAME);
        applicant.setTitle(DEFAULT_TITLE);
        applicant.setEmail1(DEFAULT_EMAIL1);
        applicant.setHomePhone(DEFAULT_HOME_PHONE);
        applicant.setCellPhone(DEFAULT_CELL_PHONE);
        applicant.setIsDeleted(DEFAULT_IS_DELETED);
        applicant.setNickName(DEFAULT_NICK_NAME);
        applicant.setWorkPhone(DEFAULT_WORK_PHONE);
        applicant.setEmail2(DEFAULT_EMAIL2);
    }

    @Test
    @Transactional
    public void createApplicant() throws Exception {
        int databaseSizeBeforeCreate = applicantRepository.findAll().size();

        // Create the Applicant

        restApplicantMockMvc.perform(post("/api/applicants")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(applicant)))
                .andExpect(status().isCreated());

        // Validate the Applicant in the database
        List<Applicant> applicants = applicantRepository.findAll();
        assertThat(applicants).hasSize(databaseSizeBeforeCreate + 1);
        Applicant testApplicant = applicants.get(applicants.size() - 1);
        assertThat(testApplicant.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testApplicant.getMiddleName()).isEqualTo(DEFAULT_MIDDLE_NAME);
        assertThat(testApplicant.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testApplicant.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testApplicant.getEmail1()).isEqualTo(DEFAULT_EMAIL1);
        assertThat(testApplicant.getHomePhone()).isEqualTo(DEFAULT_HOME_PHONE);
        assertThat(testApplicant.getCellPhone()).isEqualTo(DEFAULT_CELL_PHONE);
        assertThat(testApplicant.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
        assertThat(testApplicant.getNickName()).isEqualTo(DEFAULT_NICK_NAME);
        assertThat(testApplicant.getWorkPhone()).isEqualTo(DEFAULT_WORK_PHONE);
        assertThat(testApplicant.getEmail2()).isEqualTo(DEFAULT_EMAIL2);
    }

    @Test
    @Transactional
    public void checkIsDeletedIsRequired() throws Exception {
        int databaseSizeBeforeTest = applicantRepository.findAll().size();
        // set the field null
        applicant.setIsDeleted(null);

        // Create the Applicant, which fails.

        restApplicantMockMvc.perform(post("/api/applicants")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(applicant)))
                .andExpect(status().isBadRequest());

        List<Applicant> applicants = applicantRepository.findAll();
        assertThat(applicants).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllApplicants() throws Exception {
        // Initialize the database
        applicantRepository.saveAndFlush(applicant);

        // Get all the applicants
        restApplicantMockMvc.perform(get("/api/applicants?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(applicant.getId().intValue())))
                .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME.toString())))
                .andExpect(jsonPath("$.[*].middleName").value(hasItem(DEFAULT_MIDDLE_NAME.toString())))
                .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME.toString())))
                .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
                .andExpect(jsonPath("$.[*].email1").value(hasItem(DEFAULT_EMAIL1.toString())))
                .andExpect(jsonPath("$.[*].homePhone").value(hasItem(DEFAULT_HOME_PHONE.toString())))
                .andExpect(jsonPath("$.[*].cellPhone").value(hasItem(DEFAULT_CELL_PHONE.toString())))
                .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED)))
                .andExpect(jsonPath("$.[*].nickName").value(hasItem(DEFAULT_NICK_NAME.toString())))
                .andExpect(jsonPath("$.[*].workPhone").value(hasItem(DEFAULT_WORK_PHONE.toString())))
                .andExpect(jsonPath("$.[*].email2").value(hasItem(DEFAULT_EMAIL2.toString())));
    }

    @Test
    @Transactional
    public void getApplicant() throws Exception {
        // Initialize the database
        applicantRepository.saveAndFlush(applicant);

        // Get the applicant
        restApplicantMockMvc.perform(get("/api/applicants/{id}", applicant.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(applicant.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME.toString()))
            .andExpect(jsonPath("$.middleName").value(DEFAULT_MIDDLE_NAME.toString()))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME.toString()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.email1").value(DEFAULT_EMAIL1.toString()))
            .andExpect(jsonPath("$.homePhone").value(DEFAULT_HOME_PHONE.toString()))
            .andExpect(jsonPath("$.cellPhone").value(DEFAULT_CELL_PHONE.toString()))
            .andExpect(jsonPath("$.isDeleted").value(DEFAULT_IS_DELETED))
            .andExpect(jsonPath("$.nickName").value(DEFAULT_NICK_NAME.toString()))
            .andExpect(jsonPath("$.workPhone").value(DEFAULT_WORK_PHONE.toString()))
            .andExpect(jsonPath("$.email2").value(DEFAULT_EMAIL2.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingApplicant() throws Exception {
        // Get the applicant
        restApplicantMockMvc.perform(get("/api/applicants/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateApplicant() throws Exception {
        // Initialize the database
        applicantRepository.saveAndFlush(applicant);

		int databaseSizeBeforeUpdate = applicantRepository.findAll().size();

        // Update the applicant
        applicant.setFirstName(UPDATED_FIRST_NAME);
        applicant.setMiddleName(UPDATED_MIDDLE_NAME);
        applicant.setLastName(UPDATED_LAST_NAME);
        applicant.setTitle(UPDATED_TITLE);
        applicant.setEmail1(UPDATED_EMAIL1);
        applicant.setHomePhone(UPDATED_HOME_PHONE);
        applicant.setCellPhone(UPDATED_CELL_PHONE);
        applicant.setIsDeleted(UPDATED_IS_DELETED);
        applicant.setNickName(UPDATED_NICK_NAME);
        applicant.setWorkPhone(UPDATED_WORK_PHONE);
        applicant.setEmail2(UPDATED_EMAIL2);

        restApplicantMockMvc.perform(put("/api/applicants")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(applicant)))
                .andExpect(status().isOk());

        // Validate the Applicant in the database
        List<Applicant> applicants = applicantRepository.findAll();
        assertThat(applicants).hasSize(databaseSizeBeforeUpdate);
        Applicant testApplicant = applicants.get(applicants.size() - 1);
        assertThat(testApplicant.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testApplicant.getMiddleName()).isEqualTo(UPDATED_MIDDLE_NAME);
        assertThat(testApplicant.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testApplicant.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testApplicant.getEmail1()).isEqualTo(UPDATED_EMAIL1);
        assertThat(testApplicant.getHomePhone()).isEqualTo(UPDATED_HOME_PHONE);
        assertThat(testApplicant.getCellPhone()).isEqualTo(UPDATED_CELL_PHONE);
        assertThat(testApplicant.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        assertThat(testApplicant.getNickName()).isEqualTo(UPDATED_NICK_NAME);
        assertThat(testApplicant.getWorkPhone()).isEqualTo(UPDATED_WORK_PHONE);
        assertThat(testApplicant.getEmail2()).isEqualTo(UPDATED_EMAIL2);
    }

    @Test
    @Transactional
    public void deleteApplicant() throws Exception {
        // Initialize the database
        applicantRepository.saveAndFlush(applicant);

		int databaseSizeBeforeDelete = applicantRepository.findAll().size();

        // Get the applicant
        restApplicantMockMvc.perform(delete("/api/applicants/{id}", applicant.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Applicant> applicants = applicantRepository.findAll();
        assertThat(applicants).hasSize(databaseSizeBeforeDelete - 1);
    }
}
