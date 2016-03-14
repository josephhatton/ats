package com.crescendo.ats.web.rest;

import com.crescendo.ats.Application;
import com.crescendo.ats.domain.HiringContact;
import com.crescendo.ats.repository.HiringContactRepository;
import com.crescendo.ats.repository.search.HiringContactSearchRepository;

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
 * Test class for the HiringContactResource REST controller.
 *
 * @see HiringContactResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class HiringContactResourceIntTest {

    private static final String DEFAULT_FIRST_NAME = "AAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBB";
    private static final String DEFAULT_LAST_NAME = "AAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBB";
    private static final String DEFAULT_NICK_NAME = "AAAAA";
    private static final String UPDATED_NICK_NAME = "BBBBB";
    private static final String DEFAULT_PHONE1 = "AAAAA";
    private static final String UPDATED_PHONE1 = "BBBBB";
    private static final String DEFAULT_PHONE2 = "AAAAA";
    private static final String UPDATED_PHONE2 = "BBBBB";
    private static final String DEFAULT_EMAIL1 = "AAAAA";
    private static final String UPDATED_EMAIL1 = "BBBBB";
    private static final String DEFAULT_JOB_TITLE = "AAAAA";
    private static final String UPDATED_JOB_TITLE = "BBBBB";
    private static final String DEFAULT_REFERRAL_SOURCE = "AAAAA";
    private static final String UPDATED_REFERRAL_SOURCE = "BBBBB";
    private static final String DEFAULT_CONTACT_TYPE = "AAAAA";
    private static final String UPDATED_CONTACT_TYPE = "BBBBB";
    private static final String DEFAULT_EMAIL2 = "AAAAA";
    private static final String UPDATED_EMAIL2 = "BBBBB";
    private static final String DEFAULT_MIDDLE_NAME = "AAAAA";
    private static final String UPDATED_MIDDLE_NAME = "BBBBB";

    @Inject
    private HiringContactRepository hiringContactRepository;

    @Inject
    private HiringContactSearchRepository hiringContactSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restHiringContactMockMvc;

    private HiringContact hiringContact;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        HiringContactResource hiringContactResource = new HiringContactResource();
        ReflectionTestUtils.setField(hiringContactResource, "hiringContactSearchRepository", hiringContactSearchRepository);
        ReflectionTestUtils.setField(hiringContactResource, "hiringContactRepository", hiringContactRepository);
        this.restHiringContactMockMvc = MockMvcBuilders.standaloneSetup(hiringContactResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        hiringContact = new HiringContact();
        hiringContact.setFirstName(DEFAULT_FIRST_NAME);
        hiringContact.setLastName(DEFAULT_LAST_NAME);
        hiringContact.setNickName(DEFAULT_NICK_NAME);
        hiringContact.setPhone1(DEFAULT_PHONE1);
        hiringContact.setPhone2(DEFAULT_PHONE2);
        hiringContact.setEmail1(DEFAULT_EMAIL1);
        hiringContact.setJobTitle(DEFAULT_JOB_TITLE);
        hiringContact.setReferralSource(DEFAULT_REFERRAL_SOURCE);
        hiringContact.setContactType(DEFAULT_CONTACT_TYPE);
        hiringContact.setEmail2(DEFAULT_EMAIL2);
        hiringContact.setMiddleName(DEFAULT_MIDDLE_NAME);
    }

    @Test
    @Transactional
    public void createHiringContact() throws Exception {
        int databaseSizeBeforeCreate = hiringContactRepository.findAll().size();

        // Create the HiringContact

        restHiringContactMockMvc.perform(post("/api/hiringContacts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(hiringContact)))
                .andExpect(status().isCreated());

        // Validate the HiringContact in the database
        List<HiringContact> hiringContacts = hiringContactRepository.findAll();
        assertThat(hiringContacts).hasSize(databaseSizeBeforeCreate + 1);
        HiringContact testHiringContact = hiringContacts.get(hiringContacts.size() - 1);
        assertThat(testHiringContact.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testHiringContact.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testHiringContact.getNickName()).isEqualTo(DEFAULT_NICK_NAME);
        assertThat(testHiringContact.getPhone1()).isEqualTo(DEFAULT_PHONE1);
        assertThat(testHiringContact.getPhone2()).isEqualTo(DEFAULT_PHONE2);
        assertThat(testHiringContact.getEmail1()).isEqualTo(DEFAULT_EMAIL1);
        assertThat(testHiringContact.getJobTitle()).isEqualTo(DEFAULT_JOB_TITLE);
        assertThat(testHiringContact.getReferralSource()).isEqualTo(DEFAULT_REFERRAL_SOURCE);
        assertThat(testHiringContact.getContactType()).isEqualTo(DEFAULT_CONTACT_TYPE);
        assertThat(testHiringContact.getEmail2()).isEqualTo(DEFAULT_EMAIL2);
        assertThat(testHiringContact.getMiddleName()).isEqualTo(DEFAULT_MIDDLE_NAME);
    }

    @Test
    @Transactional
    public void checkFirstNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = hiringContactRepository.findAll().size();
        // set the field null
        hiringContact.setFirstName(null);

        // Create the HiringContact, which fails.

        restHiringContactMockMvc.perform(post("/api/hiringContacts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(hiringContact)))
                .andExpect(status().isBadRequest());

        List<HiringContact> hiringContacts = hiringContactRepository.findAll();
        assertThat(hiringContacts).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLastNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = hiringContactRepository.findAll().size();
        // set the field null
        hiringContact.setLastName(null);

        // Create the HiringContact, which fails.

        restHiringContactMockMvc.perform(post("/api/hiringContacts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(hiringContact)))
                .andExpect(status().isBadRequest());

        List<HiringContact> hiringContacts = hiringContactRepository.findAll();
        assertThat(hiringContacts).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllHiringContacts() throws Exception {
        // Initialize the database
        hiringContactRepository.saveAndFlush(hiringContact);

        // Get all the hiringContacts
        restHiringContactMockMvc.perform(get("/api/hiringContacts?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(hiringContact.getId().intValue())))
                .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME.toString())))
                .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME.toString())))
                .andExpect(jsonPath("$.[*].nickName").value(hasItem(DEFAULT_NICK_NAME.toString())))
                .andExpect(jsonPath("$.[*].phone1").value(hasItem(DEFAULT_PHONE1.toString())))
                .andExpect(jsonPath("$.[*].phone2").value(hasItem(DEFAULT_PHONE2.toString())))
                .andExpect(jsonPath("$.[*].email1").value(hasItem(DEFAULT_EMAIL1.toString())))
                .andExpect(jsonPath("$.[*].jobTitle").value(hasItem(DEFAULT_JOB_TITLE.toString())))
                .andExpect(jsonPath("$.[*].referralSource").value(hasItem(DEFAULT_REFERRAL_SOURCE.toString())))
                .andExpect(jsonPath("$.[*].contactType").value(hasItem(DEFAULT_CONTACT_TYPE.toString())))
                .andExpect(jsonPath("$.[*].email2").value(hasItem(DEFAULT_EMAIL2.toString())))
                .andExpect(jsonPath("$.[*].middleName").value(hasItem(DEFAULT_MIDDLE_NAME.toString())));
    }

    @Test
    @Transactional
    public void getHiringContact() throws Exception {
        // Initialize the database
        hiringContactRepository.saveAndFlush(hiringContact);

        // Get the hiringContact
        restHiringContactMockMvc.perform(get("/api/hiringContacts/{id}", hiringContact.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(hiringContact.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME.toString()))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME.toString()))
            .andExpect(jsonPath("$.nickName").value(DEFAULT_NICK_NAME.toString()))
            .andExpect(jsonPath("$.phone1").value(DEFAULT_PHONE1.toString()))
            .andExpect(jsonPath("$.phone2").value(DEFAULT_PHONE2.toString()))
            .andExpect(jsonPath("$.email1").value(DEFAULT_EMAIL1.toString()))
            .andExpect(jsonPath("$.jobTitle").value(DEFAULT_JOB_TITLE.toString()))
            .andExpect(jsonPath("$.referralSource").value(DEFAULT_REFERRAL_SOURCE.toString()))
            .andExpect(jsonPath("$.contactType").value(DEFAULT_CONTACT_TYPE.toString()))
            .andExpect(jsonPath("$.email2").value(DEFAULT_EMAIL2.toString()))
            .andExpect(jsonPath("$.middleName").value(DEFAULT_MIDDLE_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingHiringContact() throws Exception {
        // Get the hiringContact
        restHiringContactMockMvc.perform(get("/api/hiringContacts/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateHiringContact() throws Exception {
        // Initialize the database
        hiringContactRepository.saveAndFlush(hiringContact);

		int databaseSizeBeforeUpdate = hiringContactRepository.findAll().size();

        // Update the hiringContact
        hiringContact.setFirstName(UPDATED_FIRST_NAME);
        hiringContact.setLastName(UPDATED_LAST_NAME);
        hiringContact.setNickName(UPDATED_NICK_NAME);
        hiringContact.setPhone1(UPDATED_PHONE1);
        hiringContact.setPhone2(UPDATED_PHONE2);
        hiringContact.setEmail1(UPDATED_EMAIL1);
        hiringContact.setJobTitle(UPDATED_JOB_TITLE);
        hiringContact.setReferralSource(UPDATED_REFERRAL_SOURCE);
        hiringContact.setContactType(UPDATED_CONTACT_TYPE);
        hiringContact.setEmail2(UPDATED_EMAIL2);
        hiringContact.setMiddleName(UPDATED_MIDDLE_NAME);

        restHiringContactMockMvc.perform(put("/api/hiringContacts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(hiringContact)))
                .andExpect(status().isOk());

        // Validate the HiringContact in the database
        List<HiringContact> hiringContacts = hiringContactRepository.findAll();
        assertThat(hiringContacts).hasSize(databaseSizeBeforeUpdate);
        HiringContact testHiringContact = hiringContacts.get(hiringContacts.size() - 1);
        assertThat(testHiringContact.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testHiringContact.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testHiringContact.getNickName()).isEqualTo(UPDATED_NICK_NAME);
        assertThat(testHiringContact.getPhone1()).isEqualTo(UPDATED_PHONE1);
        assertThat(testHiringContact.getPhone2()).isEqualTo(UPDATED_PHONE2);
        assertThat(testHiringContact.getEmail1()).isEqualTo(UPDATED_EMAIL1);
        assertThat(testHiringContact.getJobTitle()).isEqualTo(UPDATED_JOB_TITLE);
        assertThat(testHiringContact.getReferralSource()).isEqualTo(UPDATED_REFERRAL_SOURCE);
        assertThat(testHiringContact.getContactType()).isEqualTo(UPDATED_CONTACT_TYPE);
        assertThat(testHiringContact.getEmail2()).isEqualTo(UPDATED_EMAIL2);
        assertThat(testHiringContact.getMiddleName()).isEqualTo(UPDATED_MIDDLE_NAME);
    }

    @Test
    @Transactional
    public void deleteHiringContact() throws Exception {
        // Initialize the database
        hiringContactRepository.saveAndFlush(hiringContact);

		int databaseSizeBeforeDelete = hiringContactRepository.findAll().size();

        // Get the hiringContact
        restHiringContactMockMvc.perform(delete("/api/hiringContacts/{id}", hiringContact.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<HiringContact> hiringContacts = hiringContactRepository.findAll();
        assertThat(hiringContacts).hasSize(databaseSizeBeforeDelete - 1);
    }
}
