package com.epam.esm.service.impl;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.CertificateService;
import com.epam.esm.util.ReflectionUtil;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

class CertificateServiceImplTest {

  TagDao tagDao = Mockito.mock(TagDao.class);
  CertificateDao certificateDao = Mockito.mock(CertificateDao.class);
  CertificateService certificateService =
      new CertificateServiceImpl(certificateDao, tagDao, new ReflectionUtil());

  @Test
  void createNoTags() {
    Mockito.when(certificateDao.create(Mockito.any())).thenReturn(takeCertificate1());
    certificateService.create(takeCertificate1());
    Mockito.verify(certificateDao).create(takeCertificate1());
    Mockito.verify(tagDao, Mockito.never()).read(Mockito.any());
    Mockito.verify(tagDao, Mockito.never()).create(Mockito.any());
    Mockito.verify(certificateDao, Mockito.never()).addTag(Mockito.anyLong(), Mockito.anyLong());
  }

  @Test
  void createWithTagsExisted() {
    Mockito.when(certificateDao.create(Mockito.any())).thenReturn(takeCertificate2());
    Mockito.when(tagDao.read(Mockito.any())).thenReturn(Optional.of(takeTag1()));
    certificateService.create(takeCertificate1());
    Mockito.verify(certificateDao).create(takeCertificate1());
    Mockito.verify(tagDao).read(Mockito.any());
    Mockito.verify(tagDao, Mockito.never()).create(Mockito.any());
    Mockito.verify(certificateDao).addTag(Mockito.anyLong(), Mockito.anyLong());
  }

  @Test
  void createWithTagsNotExisted() {
    Mockito.when(certificateDao.create(Mockito.any())).thenReturn(takeCertificate2());
    Mockito.when(tagDao.read(Mockito.any())).thenReturn(Optional.empty());
    Mockito.when(tagDao.create(Mockito.any())).thenReturn(takeTag1());
    certificateService.create(takeCertificate1());
    Mockito.verify(certificateDao).create(takeCertificate1());
    Mockito.verify(tagDao).read(Mockito.any());
    Mockito.verify(tagDao).create(Mockito.any());
    Mockito.verify(certificateDao).addTag(Mockito.anyLong(), Mockito.anyLong());
  }

  @Test
  void readExisted() {
    Mockito.when(certificateDao.read(Mockito.anyLong()))
        .thenReturn(Optional.of(takeCertificate1()));
    certificateService.read(1L);
    Mockito.verify(certificateDao).read(1L);
    Mockito.verify(certificateDao).readBondingTags(1L);
  }

  @Test
  void readNotExisted() {
    Mockito.when(certificateDao.read(Mockito.anyLong())).thenReturn(Optional.empty());
    certificateService.read(1L);
    Mockito.verify(certificateDao).read(1L);
    Mockito.verify(certificateDao, Mockito.never()).readBondingTags(1L);
  }

  @Test
  void readAll() {
    Mockito.when(certificateDao.readAll()).thenReturn(List.of(takeCertificate1()));
    certificateService.readAll();
    Mockito.verify(certificateDao).readAll();
    Mockito.verify(certificateDao).readBondingTags(Mockito.anyLong());
  }

  @Test
  void updateNotExisted() {
    Mockito.when(certificateDao.read(1L)).thenReturn(Optional.empty());
    certificateService.update(1, takeCertificate1());
    Mockito.verify(certificateDao).read(1);
    Mockito.verify(certificateDao, Mockito.never()).update(takeCertificate1());
    Mockito.verify(certificateDao, Mockito.never())
        .deleteBondingTagsByCertificateId(Mockito.anyLong());
    Mockito.verify(tagDao, Mockito.never()).read(Mockito.any());
    Mockito.verify(tagDao, Mockito.never()).create(Mockito.any());
    Mockito.verify(certificateDao, Mockito.never()).addTag(Mockito.anyLong(), Mockito.anyLong());
    Mockito.verify(certificateDao, Mockito.never()).readBondingTags(Mockito.anyLong());
  }

  @Test
  void updateExistedWithTags() {
    Mockito.when(certificateDao.read(1L)).thenReturn(Optional.of(takeCertificate1()));
    Mockito.when(tagDao.read(Mockito.any())).thenReturn(Optional.of(takeTag1()));
    certificateService.update(1, takeCertificate2());
    Mockito.verify(certificateDao).read(1);
    Mockito.verify(certificateDao).update(Mockito.any());
    Mockito.verify(certificateDao).deleteBondingTagsByCertificateId(Mockito.anyLong());
    Mockito.verify(tagDao).read(Mockito.any());
    Mockito.verify(tagDao, Mockito.never()).create(Mockito.any());
    Mockito.verify(certificateDao).addTag(Mockito.anyLong(), Mockito.anyLong());
    Mockito.verify(certificateDao, Mockito.never()).readBondingTags(Mockito.anyLong());
  }

  @Test
  void updateExistedNoTags() {
    Mockito.when(certificateDao.read(1L)).thenReturn(Optional.of(takeCertificate1()));
    certificateService.update(1, takeCertificate3());
    Mockito.verify(certificateDao).read(1);
    Mockito.verify(certificateDao).update(Mockito.any());
    Mockito.verify(certificateDao, Mockito.never())
        .deleteBondingTagsByCertificateId(Mockito.anyLong());
    Mockito.verify(tagDao, Mockito.never()).read(Mockito.any());
    Mockito.verify(tagDao, Mockito.never()).create(Mockito.any());
    Mockito.verify(certificateDao, Mockito.never()).addTag(Mockito.anyLong(), Mockito.anyLong());
    Mockito.verify(certificateDao).readBondingTags(Mockito.anyLong());
  }

  @Test
  void delete() {
    certificateService.delete(1L);
    Mockito.verify(certificateDao).deleteBondingTagsByCertificateId(1L);
    Mockito.verify(certificateDao).delete(1L);
  }

  private static Tag takeTag1() {
    return Tag.builder().id(1L).name("first tag").build();
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
    return Certificate.builder().id(1L).duration(20).tags(List.of(new Tag())).build();
  }

  private static Certificate takeCertificate3() {
    return Certificate.builder().id(1L).duration(20).build();
  }
}
