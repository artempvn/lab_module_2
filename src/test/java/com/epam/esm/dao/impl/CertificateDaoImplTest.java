package com.epam.esm.dao.impl;

import com.epam.esm.config.TestConfig;
import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.Certificate;
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

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
class CertificateDaoImplTest {

  @Autowired CertificateDao certificateDao;
  @Autowired TagDao tagDao;

  @BeforeEach
  void setUp() {
    certificateDao.create(takeCertificate1());
    certificateDao.create(takeCertificate2());
    tagDao.create(takeTag1());
    tagDao.create(takeTag2());
    certificateDao.addTag(1, 2);
    certificateDao.addTag(2, 2);
  }

  @Test
  void create() {
    Certificate expectedCertificate = takeCertificate3withoutId();
    Certificate actualCertificate = certificateDao.create(expectedCertificate);
    expectedCertificate.setId(3L);
    assertEquals(expectedCertificate, actualCertificate);
  }

  @ParameterizedTest
  @MethodSource("readDataProvider")
  void read(long actualId, Optional<Certificate> expectedCertificate) {
    Optional<Certificate> actualCertificate = certificateDao.read(actualId);
    assertEquals(expectedCertificate, actualCertificate);
  }

  static Stream<Arguments> readDataProvider() {
    return Stream.of(arguments(1, Optional.of(takeCertificate1())), arguments(6, Optional.empty()));
  }

  @Test
  void readAll() {
    List<Certificate> expectedList = List.of(takeCertificate1(), takeCertificate2());
    List<Certificate> actualList = certificateDao.readAll();
    assertEquals(expectedList, actualList);
  }

  @Test
  void update() {
    Certificate expectedCertificate = takeCertificate1();
    expectedCertificate.setDescription("updated description");
    certificateDao.update(expectedCertificate);
    Certificate actualCertificate = certificateDao.read(1).get();
    assertEquals(expectedCertificate, actualCertificate);
  }

  @Test
  void delete() {
    certificateDao.delete(1);
    Optional<Certificate> actualCertificate = certificateDao.read(1);
    assertTrue(actualCertificate.isEmpty());
  }

  @Test
  void readTags() {
    List<Tag> expectedTags = List.of(takeTag1(), takeTag2());
    List<Tag> actualTags = certificateDao.readBondingTags(2);
    assertEquals(expectedTags, actualTags);
  }

  @Test
  void addTag() {
    List<Tag> expectedTags = List.of(takeTag1());
    certificateDao.addTag(1, 1);
    List<Tag> actualTags = certificateDao.readBondingTags(1);
    assertEquals(expectedTags, actualTags);
  }

  @Test
  void deleteBondingTagsByTagId() {
    List<Tag> expectedTags = List.of(takeTag1());
    certificateDao.deleteBondingTagsByTagId(2);
    List<Tag> actualTags = certificateDao.readBondingTags(2);
    assertEquals(expectedTags, actualTags);
  }

  @Test
  void deleteBondingTagsByCertificateId() {
    List<Tag> expectedTags = Collections.emptyList();
    certificateDao.deleteBondingTagsByCertificateId(2);
    List<Tag> actualTags = certificateDao.readBondingTags(2);
    assertEquals(expectedTags, actualTags);
  }

  private static Certificate takeCertificate1() {
    return Certificate.builder()
        .id(1L)
        .name("first certificate")
        .description("first description")
        .price(1.33)
        .duration(5)
        .createDate(LocalDateTime.of(2020, 12, 25, 15, 0, 0))
        .lastUpdateDate(LocalDateTime.of(2020, 12, 30, 16, 30, 0))
        .build();
  }

  private static Certificate takeCertificate2() {
    return Certificate.builder()
        .id(2L)
        .name("second certificate")
        .description("second description")
        .price(2.33)
        .duration(10)
        .createDate(LocalDateTime.of(2020, 12, 25, 15, 0, 0))
        .lastUpdateDate(LocalDateTime.of(2021, 1, 5, 14, 0, 0))
        .build();
  }

  private static Certificate takeCertificate3withoutId() {
    return Certificate.builder()
        .name("third certificate")
        .description("third description")
        .price(3.33)
        .duration(15)
        .createDate(LocalDateTime.of(2021, 1, 5, 14, 0, 0))
        .lastUpdateDate(LocalDateTime.of(2021, 1, 5, 14, 0, 0))
        .build();
  }

  private static Tag takeTag1() {
    return Tag.builder().id(1L).name("first tag").build();
  }

  private static Tag takeTag2() {
    return Tag.builder().id(2L).name("second tag").build();
  }
}
