package com.epam.esm.dao.impl;

import com.epam.esm.config.TestConfig;
import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
class TagDaoImplTest {

  @Autowired TagDao tagDao;

  @BeforeEach
  void setUp() {
    tagDao.create(takeTag1());
    tagDao.create(takeTag2());
  }

  @Test
  void create() {
    Tag expectedTag = takeTag3withoutId();
    Tag actualTag = tagDao.create(expectedTag);
    expectedTag.setId(3L);
    assertEquals(expectedTag, actualTag);
  }

  @ParameterizedTest
  @MethodSource("readDataProvider")
  void read(long actualId, Optional<Tag> expectedTag) {
    Optional<Tag> actualTag = tagDao.read(actualId);
    assertEquals(expectedTag, actualTag);
  }

  static Stream<Arguments> readDataProvider() {
    return Stream.of(arguments(1, Optional.of(takeTag1())), arguments(6, Optional.empty()));
  }

  @Test
  void readAll() {
    List<Tag> expectedList = List.of(takeTag1(), takeTag2());
    List<Tag> actualList = tagDao.readAll();
    assertEquals(expectedList, actualList);
  }

  @Test
  void delete() {
    tagDao.delete(1);
    Optional<Tag> actualTag = tagDao.read(1);
    assertTrue(actualTag.isEmpty());
  }

  @ParameterizedTest
  @MethodSource("readTagByNameDataProvider")
  void readTest(Tag wantedTag, Optional<Tag> expectedTag) {
    Optional<Tag> actualTag = tagDao.read(wantedTag);
    assertEquals(expectedTag, actualTag);
  }

  static Stream<Arguments> readTagByNameDataProvider() {
    return Stream.of(
        arguments(takeTag1(), Optional.of(takeTag1())),
        arguments(takeTag3withoutId(), Optional.empty()));
  }

  private static Tag takeTag1() {
    return Tag.builder().id(1L).name("first tag").build();
  }

  private static Tag takeTag2() {
    return Tag.builder().id(2L).name("second tag").build();
  }

  private static Tag takeTag3withoutId() {
    return Tag.builder().name("third tag").build();
  }
}
