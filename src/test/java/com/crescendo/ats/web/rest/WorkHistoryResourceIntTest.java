package com.crescendo.ats.web.rest;

import com.crescendo.ats.Application;
import com.crescendo.ats.domain.WorkHistory;
import com.crescendo.ats.repository.WorkHistoryRepository;
import com.crescendo.ats.repository.search.WorkHistorySearchRepository;

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
 * Test class for the WorkHistoryResource REST controller.
 *
 * @see WorkHistoryResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class WorkHistoryResourceIntTest {

    private static final String DEFAULT_COMPANY = "AAAAA";
    private static final String UPDATED_COMPANY = "BBBBB";
    private static final String DEFAULT_TITLE = "AAAAA";
    private static final String UPDATED_TITLE = "BBBBB";

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Double DEFAULT_STARTING_COMPENSATION = 1D;
    private static final Double UPDATED_STARTING_COMPENSATION = 2D;

    private static final Double DEFAULT_ENDING_COMPENSATION = 1D;
    private static final Double UPDATED_ENDING_COMPENSATION = 2D;

    private static final Integer DEFAULT_COMPENSATION_TYPE = 1;
    private static final Integer UPDATED_COMPENSATION_TYPE = 2;
    private static final String DEFAULT_SUPERVISOR = "AAAAA";
    private static final String UPDATED_SUPERVISOR = "BBBBB";
    private static final String DEFAULT_SUPERVISOR_TITLE = "AAAAA";
    private static final String UPDATED_SUPERVISOR_TITLE = "BBBBB";
    private static final String DEFAULT_SUPERVISOR_PHONE = "AAAAA";
    private static final String UPDATED_SUPERVISOR_PHONE = "BBBBB";
    private static final String DEFAULT_DUTIES = "AAAAA";
    private static final String UPDATED_DUTIES = "BBBBB";
    private static final String DEFAULT_REASON_FOR_LEAVING = "AAAAA";
    private static final String UPDATED_REASON_FOR_LEAVING = "BBBBB";

    @Inject
    private WorkHistoryRepository workHistoryRepository;

    @Inject
    private WorkHistorySearchRepository workHistorySearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restWorkHistoryMockMvc;

    private WorkHistory workHistory;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        WorkHistoryResource workHistoryResource = new WorkHistoryResource();
        ReflectionTestUtils.setField(workHistoryResource, "workHistorySearchRepository", workHistorySearchRepository);
        ReflectionTestUtils.setField(workHistoryResource, "workHistoryRepository", workHistoryRepository);
        this.restWorkHistoryMockMvc = MockMvcBuilders.standaloneSetup(workHistoryResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        workHistory = new WorkHistory();
        workHistory.setCompany(DEFAULT_COMPANY);
        workHistory.setTitle(DEFAULT_TITLE);
        workHistory.setStartDate(DEFAULT_START_DATE);
        workHistory.setEndDate(DEFAULT_END_DATE);
        workHistory.setStartingCompensation(DEFAULT_STARTING_COMPENSATION);
        workHistory.setEndingCompensation(DEFAULT_ENDING_COMPENSATION);
        workHistory.setCompensationType(DEFAULT_COMPENSATION_TYPE);
        workHistory.setSupervisor(DEFAULT_SUPERVISOR);
        workHistory.setSupervisorTitle(DEFAULT_SUPERVISOR_TITLE);
        workHistory.setSupervisorPhone(DEFAULT_SUPERVISOR_PHONE);
        workHistory.setDuties(DEFAULT_DUTIES);
        workHistory.setReasonForLeaving(DEFAULT_REASON_FOR_LEAVING);
    }

    @Test
    @Transactional
    public void createWorkHistory() throws Exception {
        int databaseSizeBeforeCreate = workHistoryRepository.findAll().size();

        // Create the WorkHistory

        restWorkHistoryMockMvc.perform(post("/api/workHistorys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(workHistory)))
                .andExpect(status().isCreated());

        // Validate the WorkHistory in the database
        List<WorkHistory> workHistorys = workHistoryRepository.findAll();
        assertThat(workHistorys).hasSize(databaseSizeBeforeCreate + 1);
        WorkHistory testWorkHistory = workHistorys.get(workHistorys.size() - 1);
        assertThat(testWorkHistory.getCompany()).isEqualTo(DEFAULT_COMPANY);
        assertThat(testWorkHistory.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testWorkHistory.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testWorkHistory.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testWorkHistory.getStartingCompensation()).isEqualTo(DEFAULT_STARTING_COMPENSATION);
        assertThat(testWorkHistory.getEndingCompensation()).isEqualTo(DEFAULT_ENDING_COMPENSATION);
        assertThat(testWorkHistory.getCompensationType()).isEqualTo(DEFAULT_COMPENSATION_TYPE);
        assertThat(testWorkHistory.getSupervisor()).isEqualTo(DEFAULT_SUPERVISOR);
        assertThat(testWorkHistory.getSupervisorTitle()).isEqualTo(DEFAULT_SUPERVISOR_TITLE);
        assertThat(testWorkHistory.getSupervisorPhone()).isEqualTo(DEFAULT_SUPERVISOR_PHONE);
        assertThat(testWorkHistory.getDuties()).isEqualTo(DEFAULT_DUTIES);
        assertThat(testWorkHistory.getReasonForLeaving()).isEqualTo(DEFAULT_REASON_FOR_LEAVING);
    }

    @Test
    @Transactional
    public void getAllWorkHistorys() throws Exception {
        // Initialize the database
        workHistoryRepository.saveAndFlush(workHistory);

        // Get all the workHistorys
        restWorkHistoryMockMvc.perform(get("/api/workHistorys?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(workHistory.getId().intValue())))
                .andExpect(jsonPath("$.[*].company").value(hasItem(DEFAULT_COMPANY.toString())))
                .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
                .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
                .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
                .andExpect(jsonPath("$.[*].startingCompensation").value(hasItem(DEFAULT_STARTING_COMPENSATION.doubleValue())))
                .andExpect(jsonPath("$.[*].endingCompensation").value(hasItem(DEFAULT_ENDING_COMPENSATION.doubleValue())))
                .andExpect(jsonPath("$.[*].compensationType").value(hasItem(DEFAULT_COMPENSATION_TYPE)))
                .andExpect(jsonPath("$.[*].supervisor").value(hasItem(DEFAULT_SUPERVISOR.toString())))
                .andExpect(jsonPath("$.[*].supervisorTitle").value(hasItem(DEFAULT_SUPERVISOR_TITLE.toString())))
                .andExpect(jsonPath("$.[*].supervisorPhone").value(hasItem(DEFAULT_SUPERVISOR_PHONE.toString())))
                .andExpect(jsonPath("$.[*].duties").value(hasItem(DEFAULT_DUTIES.toString())))
                .andExpect(jsonPath("$.[*].reasonForLeaving").value(hasItem(DEFAULT_REASON_FOR_LEAVING.toString())));
    }

    @Test
    @Transactional
    public void getWorkHistory() throws Exception {
        // Initialize the database
        workHistoryRepository.saveAndFlush(workHistory);

        // Get the workHistory
        restWorkHistoryMockMvc.perform(get("/api/workHistorys/{id}", workHistory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(workHistory.getId().intValue()))
            .andExpect(jsonPath("$.company").value(DEFAULT_COMPANY.toString()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.startingCompensation").value(DEFAULT_STARTING_COMPENSATION.doubleValue()))
            .andExpect(jsonPath("$.endingCompensation").value(DEFAULT_ENDING_COMPENSATION.doubleValue()))
            .andExpect(jsonPath("$.compensationType").value(DEFAULT_COMPENSATION_TYPE))
            .andExpect(jsonPath("$.supervisor").value(DEFAULT_SUPERVISOR.toString()))
            .andExpect(jsonPath("$.supervisorTitle").value(DEFAULT_SUPERVISOR_TITLE.toString()))
            .andExpect(jsonPath("$.supervisorPhone").value(DEFAULT_SUPERVISOR_PHONE.toString()))
            .andExpect(jsonPath("$.duties").value(DEFAULT_DUTIES.toString()))
            .andExpect(jsonPath("$.reasonForLeaving").value(DEFAULT_REASON_FOR_LEAVING.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingWorkHistory() throws Exception {
        // Get the workHistory
        restWorkHistoryMockMvc.perform(get("/api/workHistorys/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateWorkHistory() throws Exception {
        // Initialize the database
        workHistoryRepository.saveAndFlush(workHistory);

		int databaseSizeBeforeUpdate = workHistoryRepository.findAll().size();

        // Update the workHistory
        workHistory.setCompany(UPDATED_COMPANY);
        workHistory.setTitle(UPDATED_TITLE);
        workHistory.setStartDate(UPDATED_START_DATE);
        workHistory.setEndDate(UPDATED_END_DATE);
        workHistory.setStartingCompensation(UPDATED_STARTING_COMPENSATION);
        workHistory.setEndingCompensation(UPDATED_ENDING_COMPENSATION);
        workHistory.setCompensationType(UPDATED_COMPENSATION_TYPE);
        workHistory.setSupervisor(UPDATED_SUPERVISOR);
        workHistory.setSupervisorTitle(UPDATED_SUPERVISOR_TITLE);
        workHistory.setSupervisorPhone(UPDATED_SUPERVISOR_PHONE);
        workHistory.setDuties(UPDATED_DUTIES);
        workHistory.setReasonForLeaving(UPDATED_REASON_FOR_LEAVING);

        restWorkHistoryMockMvc.perform(put("/api/workHistorys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(workHistory)))
                .andExpect(status().isOk());

        // Validate the WorkHistory in the database
        List<WorkHistory> workHistorys = workHistoryRepository.findAll();
        assertThat(workHistorys).hasSize(databaseSizeBeforeUpdate);
        WorkHistory testWorkHistory = workHistorys.get(workHistorys.size() - 1);
        assertThat(testWorkHistory.getCompany()).isEqualTo(UPDATED_COMPANY);
        assertThat(testWorkHistory.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testWorkHistory.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testWorkHistory.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testWorkHistory.getStartingCompensation()).isEqualTo(UPDATED_STARTING_COMPENSATION);
        assertThat(testWorkHistory.getEndingCompensation()).isEqualTo(UPDATED_ENDING_COMPENSATION);
        assertThat(testWorkHistory.getCompensationType()).isEqualTo(UPDATED_COMPENSATION_TYPE);
        assertThat(testWorkHistory.getSupervisor()).isEqualTo(UPDATED_SUPERVISOR);
        assertThat(testWorkHistory.getSupervisorTitle()).isEqualTo(UPDATED_SUPERVISOR_TITLE);
        assertThat(testWorkHistory.getSupervisorPhone()).isEqualTo(UPDATED_SUPERVISOR_PHONE);
        assertThat(testWorkHistory.getDuties()).isEqualTo(UPDATED_DUTIES);
        assertThat(testWorkHistory.getReasonForLeaving()).isEqualTo(UPDATED_REASON_FOR_LEAVING);
    }

    @Test
    @Transactional
    public void deleteWorkHistory() throws Exception {
        // Initialize the database
        workHistoryRepository.saveAndFlush(workHistory);

		int databaseSizeBeforeDelete = workHistoryRepository.findAll().size();

        // Get the workHistory
        restWorkHistoryMockMvc.perform(delete("/api/workHistorys/{id}", workHistory.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<WorkHistory> workHistorys = workHistoryRepository.findAll();
        assertThat(workHistorys).hasSize(databaseSizeBeforeDelete - 1);
    }
}
