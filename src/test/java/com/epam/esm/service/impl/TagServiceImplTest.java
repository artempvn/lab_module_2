package com.epam.esm.service.impl;

import com.epam.esm.config.TestConfig;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.TagService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
class TagServiceImplTest {
  @Autowired TagService tagService;
  static Tag tag1;
  static Tag tag2;
  static Tag tag3;
  static Tag tag4;
  static Tag tag5;

  @BeforeAll
  static void setUp() {
    tag1 = new Tag(1, "first tag");
    tag2 = new Tag(2, "second tag");
    tag3 = new Tag(3, "third tag");
    tag4 = new Tag(4, "fourth tag");
    tag5 = new Tag(5, "fifth tag");
  }

  @Test
  void create() {
    Tag someTag = new Tag(-1, "fifth tag");
    assertEquals(tagService.create(someTag), tag5);
  }

  @ParameterizedTest
  @MethodSource("readDataProvider")
  void read(long actualId, Tag expected) {
    assertEquals(tagService.read(actualId), expected);
  }

  static Stream<Arguments> readDataProvider() {
    return Stream.of(arguments(1, tag1), arguments(6, null));
  }

  @Test
  void readAll() {
    assertEquals(tagService.readAll(), Arrays.asList(tag1, tag2, tag3, tag4));
  }

  @Test
  void delete() {
    tagService.delete(4);
    assertEquals(tagService.readAll(), Arrays.asList(tag1, tag2, tag3));
  }
}
