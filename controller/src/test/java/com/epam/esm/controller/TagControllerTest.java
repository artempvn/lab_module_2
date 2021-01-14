package com.epam.esm.controller;

import com.epam.esm.config.TestConfig;
import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.Tag;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
@WebAppConfiguration
class TagControllerTest {
  public static final int FIRST_ID = 1;
  MockMvc mockMvc;
  @Autowired TagDao tagDao;
  @Autowired TagController tagController;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.openMocks(this);
    mockMvc = MockMvcBuilders.standaloneSetup(tagController).build();
  }

  @Test
  void readTagPositiveStatusCheck() throws Exception {
    Tag tag1 = givenExistingTag1();
    tagDao.create(tag1);

    mockMvc.perform(get("/tags/{id}", tag1.getId())).andExpect(status().isOk());
  }

  @Test
  void readTagPositiveValueCheck() throws Exception {
    Tag tag1 = givenExistingTag1();
    tagDao.create(tag1);

    mockMvc
        .perform(get("/tags/{id}", tag1.getId()))
        .andExpect(jsonPath("$.id").value(tag1.getId()))
        .andExpect(jsonPath("$.name").value(tag1.getName()));
  }

  @Test
  void readTagNegativeStatusCheck() throws Exception {
    Tag tag1 = givenExistingTag1();

    mockMvc.perform(get("/tags/{id}", tag1.getId())).andExpect(status().isNotFound());
  }

  @Test
  void readTagsStatusCheck() throws Exception {
    Tag tag1 = givenExistingTag1();
    Tag tag2 = givenExistingTag2();
    tagDao.create(tag1);
    tagDao.create(tag2);

    mockMvc.perform(get("/tags")).andExpect(status().isOk());
  }

  @Test
  void readTagsValueCheck() throws Exception {
    Tag tag1 = givenExistingTag1();
    Tag tag2 = givenExistingTag2();
    tagDao.create(tag1);
    tagDao.create(tag2);

    mockMvc
        .perform(get("/tags"))
        .andExpect(content().json(new ObjectMapper().writeValueAsString(List.of(tag1, tag2))));
  }

  @Test
  void createTagStatusCheck() throws Exception {
    Tag tag1 = givenExistingTag1();
    tag1.setId(null);

    mockMvc
        .perform(
            post("/tags")
                .content(new ObjectMapper().writeValueAsString(tag1))
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isCreated());
  }

  @Test
  void createTagValueCheck() throws Exception {
    Tag tag1 = givenExistingTag1();
    tag1.setId(null);

    mockMvc
        .perform(
            post("/tags")
                .content(new ObjectMapper().writeValueAsString(tag1))
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id").value(FIRST_ID))
        .andExpect(jsonPath("$.name").value(tag1.getName()));
  }

  @Test
  void deleteTagStatusCheck() throws Exception {
    Tag tag1 = givenExistingTag1();
    tagDao.create(tag1);

    mockMvc.perform(delete("/tags/{id}", tag1.getId())).andExpect(status().isNoContent());
  }

  @Test
  void deleteTagStatusCheckAfterRequest() throws Exception {
    Tag tag1 = givenExistingTag1();
    tagDao.create(tag1);

    mockMvc.perform(delete("/tags/{id}", tag1.getId()));

    mockMvc.perform(get("/tags/{id}", tag1.getId())).andExpect(status().isNotFound());
  }

  private static Tag givenExistingTag1() {
    return Tag.builder().id(1L).name("first tag").build();
  }

  private static Tag givenExistingTag2() {
    return Tag.builder().id(2L).name("second tag").build();
  }
}
