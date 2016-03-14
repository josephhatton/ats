package com.crescendo.ats.web.rest;

import com.crescendo.ats.Application;
import com.crescendo.ats.domain.SkillCategory;
import com.crescendo.ats.repository.SkillCategoryRepository;
import com.crescendo.ats.repository.search.SkillCategorySearchRepository;

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
 * Test class for the SkillCategoryResource REST controller.
 *
 * @see SkillCategoryResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class SkillCategoryResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    @Inject
    private SkillCategoryRepository skillCategoryRepository;

    @Inject
    private SkillCategorySearchRepository skillCategorySearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restSkillCategoryMockMvc;

    private SkillCategory skillCategory;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SkillCategoryResource skillCategoryResource = new SkillCategoryResource();
        ReflectionTestUtils.setField(skillCategoryResource, "skillCategorySearchRepository", skillCategorySearchRepository);
        ReflectionTestUtils.setField(skillCategoryResource, "skillCategoryRepository", skillCategoryRepository);
        this.restSkillCategoryMockMvc = MockMvcBuilders.standaloneSetup(skillCategoryResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        skillCategory = new SkillCategory();
        skillCategory.setName(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createSkillCategory() throws Exception {
        int databaseSizeBeforeCreate = skillCategoryRepository.findAll().size();

        // Create the SkillCategory

        restSkillCategoryMockMvc.perform(post("/api/skillCategorys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(skillCategory)))
                .andExpect(status().isCreated());

        // Validate the SkillCategory in the database
        List<SkillCategory> skillCategorys = skillCategoryRepository.findAll();
        assertThat(skillCategorys).hasSize(databaseSizeBeforeCreate + 1);
        SkillCategory testSkillCategory = skillCategorys.get(skillCategorys.size() - 1);
        assertThat(testSkillCategory.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = skillCategoryRepository.findAll().size();
        // set the field null
        skillCategory.setName(null);

        // Create the SkillCategory, which fails.

        restSkillCategoryMockMvc.perform(post("/api/skillCategorys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(skillCategory)))
                .andExpect(status().isBadRequest());

        List<SkillCategory> skillCategorys = skillCategoryRepository.findAll();
        assertThat(skillCategorys).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSkillCategorys() throws Exception {
        // Initialize the database
        skillCategoryRepository.saveAndFlush(skillCategory);

        // Get all the skillCategorys
        restSkillCategoryMockMvc.perform(get("/api/skillCategorys?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(skillCategory.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getSkillCategory() throws Exception {
        // Initialize the database
        skillCategoryRepository.saveAndFlush(skillCategory);

        // Get the skillCategory
        restSkillCategoryMockMvc.perform(get("/api/skillCategorys/{id}", skillCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(skillCategory.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSkillCategory() throws Exception {
        // Get the skillCategory
        restSkillCategoryMockMvc.perform(get("/api/skillCategorys/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSkillCategory() throws Exception {
        // Initialize the database
        skillCategoryRepository.saveAndFlush(skillCategory);

		int databaseSizeBeforeUpdate = skillCategoryRepository.findAll().size();

        // Update the skillCategory
        skillCategory.setName(UPDATED_NAME);

        restSkillCategoryMockMvc.perform(put("/api/skillCategorys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(skillCategory)))
                .andExpect(status().isOk());

        // Validate the SkillCategory in the database
        List<SkillCategory> skillCategorys = skillCategoryRepository.findAll();
        assertThat(skillCategorys).hasSize(databaseSizeBeforeUpdate);
        SkillCategory testSkillCategory = skillCategorys.get(skillCategorys.size() - 1);
        assertThat(testSkillCategory.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void deleteSkillCategory() throws Exception {
        // Initialize the database
        skillCategoryRepository.saveAndFlush(skillCategory);

		int databaseSizeBeforeDelete = skillCategoryRepository.findAll().size();

        // Get the skillCategory
        restSkillCategoryMockMvc.perform(delete("/api/skillCategorys/{id}", skillCategory.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<SkillCategory> skillCategorys = skillCategoryRepository.findAll();
        assertThat(skillCategorys).hasSize(databaseSizeBeforeDelete - 1);
    }
}
