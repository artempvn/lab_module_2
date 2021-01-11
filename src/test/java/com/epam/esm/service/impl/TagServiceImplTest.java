package com.epam.esm.service.impl;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.TagService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

class TagServiceImplTest {

  TagDao tagDao = Mockito.mock(TagDao.class);
  CertificateDao certificateDao = Mockito.mock(CertificateDao.class);
  TagService tagService = new TagServiceImpl(tagDao, certificateDao);

  @Test
  void createIfExisted() {
    Tag tag = takeTag1();
    Mockito.when(tagDao.readAll()).thenReturn(List.of(tag));
    Mockito.when(tagDao.read(tag)).thenReturn(Optional.of(tag));
    tagService.create(tag);
    Mockito.verify(tagDao, Mockito.never()).create(Mockito.any());
    Mockito.verify(tagDao).read(tag);
  }

  @Test
  void createIfNotExisted() {
    Tag tag = takeTag1();
    Mockito.when(tagDao.readAll()).thenReturn(Collections.emptyList());
    Mockito.when(tagDao.create(tag)).thenReturn(tag);
    tagService.create(tag);
    Mockito.verify(tagDao).create(tag);
    Mockito.verify(tagDao, Mockito.never()).read(Mockito.any());
  }

  @Test
  void read() {
    tagService.read(1L);
    Mockito.verify(tagDao).read(Mockito.anyLong());
  }

  @Test
  void readAll() {
    tagService.readAll();
    Mockito.verify(tagDao).readAll();
  }

  @Test
  void delete() {
    tagService.delete(1L);
    Mockito.verify(certificateDao).deleteBondingTagsByTagId(1L);
    Mockito.verify(tagDao).delete(1L);
  }

  private static Tag takeTag1() {
    return Tag.builder().id(1L).name("first tag").build();
  }
}
