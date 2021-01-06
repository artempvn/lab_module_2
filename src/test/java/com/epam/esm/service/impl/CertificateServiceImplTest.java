package com.epam.esm.service.impl;

import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.CertificateService;
import com.epam.esm.service.impl.com.epam.esm.config.TestConfig;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
class CertificateServiceImplTest {

  @Autowired CertificateService certificateService;
  static Certificate certificate1;
  static Certificate certificate2;
  static Certificate certificate3;
  static Tag tag1;
  static Tag tag2;
  static Tag tag3;
  static LocalDateTime time1;
  static LocalDateTime time2;
  static LocalDateTime time3;
  static LocalDateTime time4;

  @BeforeAll
  static void setUp() {
    time1 = LocalDateTime.of(2020, 12, 25, 15, 0, 0);
    time2 = LocalDateTime.of(2020, 12, 30, 16, 30, 0);
    time3 = LocalDateTime.of(2021, 1, 5, 14, 0, 0);
    tag1 = new Tag(1, "first tag");
    tag2 = new Tag(2, "second tag");
    tag3 = new Tag(3, "third tag");
    certificate1 =
        new Certificate(
            1,
            "first certificate",
            "first description",
            1.33,
            5,
            time1,
            time2,
            Arrays.asList(tag1, tag2));
    certificate2 =
        new Certificate(
            2,
            "second certificate",
            "second description",
            2.33,
            10,
            time1,
            time3,
            Arrays.asList(tag2));
    certificate3 =
        new Certificate(
            3,
            "third certificate",
            "third description",
            3.33,
            15,
            time3,
            time3,
            Arrays.asList(tag2, tag3));
  }

  @Test
  void create() {
    Certificate someCertificate =
        new Certificate(
            -1,
            "third certificate",
            "third description",
            3.33,
            15,
            time3,
            time3,
            Arrays.asList(tag2, tag3));
    assertEquals(certificateService.create(someCertificate), certificate3);
  }

  @ParameterizedTest
  @MethodSource("readDataProvider")
  void read(long actualId, Certificate expected) {
    assertEquals(certificateService.read(actualId), expected);
  }

  static Stream<Arguments> readDataProvider() {
    return Stream.of(arguments(1, certificate1), arguments(6, null));
  }

  @Test
  void readAll() {
    assertEquals(certificateService.readAll(), Arrays.asList(certificate1, certificate2));
  }

  @Disabled("in progress")
  @Test
  void delete() {}

  @Disabled("in progress")
  @Test
  void update() {}
}
