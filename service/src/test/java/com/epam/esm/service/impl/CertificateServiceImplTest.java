package com.epam.esm.service.impl;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.ReflectionUtil;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

class CertificateServiceImplTest {

  public static final long ID = 1L;
  public static final int NO_UPDATED_ROWS = 0;
  public static final int ONE_UPDATED_ROW = 1;

  TagDao tagDao = mock(TagDao.class);
  CertificateDao certificateDao = mock(CertificateDao.class);
  CertificateServiceImpl certificateService =
      new CertificateServiceImpl(certificateDao, tagDao, new ReflectionUtil());

  @Test
  void createNoTagsCertificateDaoCreateInvocation() {
    when(certificateDao.create(any())).thenReturn(givenCertificate1());

    certificateService.create(givenCertificate1());

    verify(certificateDao).create(givenCertificate1());
  }

  @Test
  void createNoTagsTagDaoReadInvocation() {
    when(certificateDao.create(any())).thenReturn(givenCertificate1());

    certificateService.create(givenCertificate1());

    verify(tagDao, never()).read(any());
  }

  @Test
  void createNoTagsTagDaoCreateInvocation() {
    when(certificateDao.create(any())).thenReturn(givenCertificate1());

    certificateService.create(givenCertificate1());

    verify(tagDao, never()).create(any());
  }

  @Test
  void createNoTagsCertificateDaoAddTagInvocation() {
    when(certificateDao.create(any())).thenReturn(givenCertificate1());

    certificateService.create(givenCertificate1());

    verify(certificateDao, never()).addTag(anyLong(), anyLong());
  }

  @Test
  void createWithTagsExistedCertificateDaoCreateInvocation() {
    when(certificateDao.create(any())).thenReturn(givenCertificate2());
    when(tagDao.read(any())).thenReturn(Optional.of(takeTag1()));

    certificateService.create(givenCertificate1());

    verify(certificateDao).create(givenCertificate1());
  }

  @Test
  void createWithTagsExistedTagDaoReadInvocation() {
    when(certificateDao.create(any())).thenReturn(givenCertificate2());
    when(tagDao.read(any())).thenReturn(Optional.of(takeTag1()));

    certificateService.create(givenCertificate1());

    verify(tagDao).read(any());
  }

  @Test
  void createWithTagsExistedTagDaoCreateInvocation() {
    when(certificateDao.create(any())).thenReturn(givenCertificate2());
    when(tagDao.read(any())).thenReturn(Optional.of(takeTag1()));

    certificateService.create(givenCertificate1());

    verify(tagDao, never()).create(any());
  }

  @Test
  void createWithTagsExistedCertificateDaoAddTagInvocation() {
    when(certificateDao.create(any())).thenReturn(givenCertificate2());
    when(tagDao.read(any())).thenReturn(Optional.of(takeTag1()));

    certificateService.create(givenCertificate1());

    verify(certificateDao).addTag(anyLong(), anyLong());
  }

  @Test
  void createWithTagsNotExistedCertificateDaoCreateInvocation() {
    when(certificateDao.create(any())).thenReturn(givenCertificate2());
    when(tagDao.read(any())).thenReturn(Optional.empty());
    when(tagDao.create(any())).thenReturn(takeTag1());

    certificateService.create(givenCertificate1());

    verify(certificateDao).create(givenCertificate1());
  }

  @Test
  void createWithTagsNotExistedTagDaoReadInvocation() {
    when(certificateDao.create(any())).thenReturn(givenCertificate2());
    when(tagDao.read(any())).thenReturn(Optional.empty());
    when(tagDao.create(any())).thenReturn(takeTag1());

    certificateService.create(givenCertificate1());

    verify(tagDao).read(any());
  }

  @Test
  void createWithTagsNotExistedTagDaoCreateInvocation() {
    when(certificateDao.create(any())).thenReturn(givenCertificate2());
    when(tagDao.read(any())).thenReturn(Optional.empty());
    when(tagDao.create(any())).thenReturn(takeTag1());

    certificateService.create(givenCertificate1());

    verify(tagDao).create(any());
  }

  @Test
  void createWithTagsNotExistedCertificateDaoAddTagInvocation() {
    when(certificateDao.create(any())).thenReturn(givenCertificate2());
    when(tagDao.read(any())).thenReturn(Optional.empty());
    when(tagDao.create(any())).thenReturn(takeTag1());

    certificateService.create(givenCertificate1());

    verify(certificateDao).addTag(anyLong(), anyLong());
  }

  @Test
  void readExistedCertificateDaoReadInvocation() {
    when(certificateDao.read(anyLong())).thenReturn(Optional.of(givenCertificate1()));

    certificateService.read(ID);

    verify(certificateDao).read(ID);
  }

  @Test
  void readExistedCertificateDaoReadCertificateTagsInvocation() {
    when(certificateDao.read(anyLong())).thenReturn(Optional.of(givenCertificate1()));

    certificateService.read(ID);

    verify(certificateDao).readCertificateTags(ID);
  }

  @Test
  void readNotExistedCertificateDaoReadInvocation() {
    when(certificateDao.read(anyLong())).thenReturn(Optional.empty());

    certificateService.read(ID);

    verify(certificateDao).read(ID);
  }

  @Test
  void readNotExistedCertificateDaoReadCertificateTagsInvocation() {
    when(certificateDao.read(anyLong())).thenReturn(Optional.empty());

    certificateService.read(ID);

    verify(certificateDao, never()).readCertificateTags(ID);
  }

  @Test
  void readAllCertificateDaoReadAllInvocation() {
    when(certificateDao.readAll(any())).thenReturn(List.of(givenCertificate1()));

    certificateService.readAll(any());

    verify(certificateDao).readAll(any());
  }

  @Test
  void readAllCertificateDaoReadCertificateTagsInvocation() {
    when(certificateDao.readAll(any())).thenReturn(List.of(givenCertificate1()));

    certificateService.readAll(any());

    verify(certificateDao).readCertificateTags(anyLong());
  }

  @Test
  void updateCertificateDaoUpdatePatchInvocation() {
    when(certificateDao.updatePatch(any())).thenReturn(NO_UPDATED_ROWS);

    certificateService.updatePatch(givenCertificate1());

    verify(certificateDao).updatePatch(any());
  }

  @Test
  void updateNotExistedCertificateDaoDeleteCertificateTagsByCertificateIdInvocation() {
    when(certificateDao.updatePatch(any())).thenReturn(NO_UPDATED_ROWS);

    certificateService.updatePatch(givenCertificate1());

    verify(certificateDao, never()).deleteCertificateTagsByCertificateId(anyLong());
  }

  @Test
  void updateNotExistedCertificateDaoReadCertificateTagsInvocation() {
    when(certificateDao.updatePatch(any())).thenReturn(NO_UPDATED_ROWS);

    certificateService.updatePatch(givenCertificate1());

    verify(certificateDao, never()).readCertificateTags(anyLong());
  }

  @Test
  void updateExistedWithTagsCertificateDaoDeleteCertificateTagsByCertificateIdInvocation() {
    when(certificateDao.updatePatch(any())).thenReturn(ONE_UPDATED_ROW);
    when(tagDao.read(any())).thenReturn(Optional.of(takeTag1()));

    certificateService.updatePatch(givenCertificate2());

    verify(certificateDao).deleteCertificateTagsByCertificateId(anyLong());
  }

  @Test
  void updateExistedWithTagsCertificateDaoReadCertificateTagsInvocation() {
    when(certificateDao.updatePatch(any())).thenReturn(ONE_UPDATED_ROW);
    when(tagDao.read(any())).thenReturn(Optional.of(takeTag1()));

    certificateService.updatePatch(givenCertificate2());

    verify(certificateDao, never()).readCertificateTags(anyLong());
  }

  @Test
  void updateExistedNoTagsCertificateDaodeleteCertificateTagsByCertificateIdInvocation() {
    when(certificateDao.updatePatch(any())).thenReturn(ONE_UPDATED_ROW);

    certificateService.updatePatch(givenCertificate3());

    verify(certificateDao, never()).deleteCertificateTagsByCertificateId(anyLong());
  }

  @Test
  void updateExistedNoTagsCertificateDaoReadCertificateTagsInvocation() {
    when(certificateDao.updatePatch(any())).thenReturn(ONE_UPDATED_ROW);

    certificateService.updatePatch(givenCertificate3());

    verify(certificateDao).readCertificateTags(anyLong());
  }

  @Test
  void updatePut() {
    when(certificateDao.update(any())).thenReturn(ONE_UPDATED_ROW);

    certificateService.updatePut(givenCertificate1());

    verify(certificateDao).update(any());
  }

  @Test
  void updatePutNotExistedCertificateDaoDeleteCertificateTagsByCertificateIdInvocation() {
    when(certificateDao.update(any())).thenReturn(NO_UPDATED_ROWS);

    certificateService.updatePut(givenCertificate1());

    verify(certificateDao, never()).deleteCertificateTagsByCertificateId(anyLong());
  }

  @Test
  void updatePutExistedCertificateDaoDeleteCertificateTagsByCertificateIdInvocation() {
    when(certificateDao.update(any())).thenReturn(ONE_UPDATED_ROW);

    certificateService.updatePut(givenCertificate1());

    verify(certificateDao).deleteCertificateTagsByCertificateId(anyLong());
  }

  @Test
  void deleteCertificateDaoDeleteCertificateTagsByCertificateIdInvocation() {
    certificateService.delete(ID);

    verify(certificateDao).deleteCertificateTagsByCertificateId(ID);
  }

  @Test
  void deleteCertificateDaoDeleteInvocation() {
    certificateService.delete(ID);

    verify(certificateDao).delete(ID);
  }

  @Test
  void addTagsToDbNoTagsTagDaoRead() {

    certificateService.addTagsToDb(givenCertificate1());

    verify(tagDao, never()).read(any());
  }

  @Test
  void addTagsToDbNoTagsTagDaoCreate() {

    certificateService.addTagsToDb(givenCertificate1());

    verify(tagDao, never()).create(any());
  }

  @Test
  void addTagsToDbNoTagsCertificateDaoAddTag() {

    certificateService.addTagsToDb(givenCertificate1());

    verify(certificateDao, never()).addTag(anyLong(), anyLong());
  }

  @Test
  void addTagsToDbWithTagTagDaoRead() {
    when(tagDao.read(any())).thenReturn(Optional.of(takeTag1()));

    certificateService.addTagsToDb(givenCertificate2());

    verify(tagDao).read(any());
  }

  @Test
  void addTagsToDbWithExistedTagTagDaoCreate() {
    when(tagDao.read(any())).thenReturn(Optional.of(takeTag1()));

    certificateService.addTagsToDb(givenCertificate1());

    verify(tagDao, never()).create(any());
  }

  @Test
  void addTagsToDbWithTagCertificateDaoAddTag() {
    when(tagDao.read(any())).thenReturn(Optional.of(takeTag1()));

    certificateService.addTagsToDb(givenCertificate2());

    verify(certificateDao).addTag(anyLong(), anyLong());
  }

  @Test
  void addTagsToDbWithUnexistedTagTagDaoCreate() {
    when(tagDao.read(any())).thenReturn(Optional.empty());
    when(tagDao.create(any())).thenReturn(givenTag());

    certificateService.addTagsToDb(givenCertificate2());

    verify(tagDao).create(any());
  }

  private static Tag takeTag1() {
    return Tag.builder().id(1L).name("first tag").build();
  }

  private static Certificate givenCertificate1() {
    return Certificate.builder()
        .id(ID)
        .name("first certificate")
        .description("first description")
        .price(1.33)
        .duration(5)
        .createDate(LocalDateTime.of(2020, 12, 25, 15, 0, 0))
        .lastUpdateDate(LocalDateTime.of(2020, 12, 30, 16, 30, 0))
        .build();
  }

  private static Certificate givenCertificate2() {
    return Certificate.builder().id(ID).duration(20).tags(List.of(new Tag())).build();
  }

  private static Certificate givenCertificate3() {
    return Certificate.builder().id(ID).duration(20).build();
  }

  private static Tag givenTag() {
    return Tag.builder().id(ID).build();
  }
}
