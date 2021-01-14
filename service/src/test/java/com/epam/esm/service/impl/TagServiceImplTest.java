package com.epam.esm.service.impl;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.TagService;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.*;

class TagServiceImplTest {

  public static final long TAG_ID = 1L;
  TagDao tagDao = mock(TagDao.class);
  CertificateDao certificateDao = mock(CertificateDao.class);
  TagService tagService = new TagServiceImpl(tagDao, certificateDao);

  @Test
  void createTagDaoReadInvocation() {
    Tag tag = givenTag();
    when(tagDao.read(tag.getName())).thenReturn(Optional.of(tag));

    tagService.create(tag);

    verify(tagDao).read(tag.getName());
  }

  @Test
  void createIfExistedTagDaoCreateInvocation() {
    Tag tag = givenTag();
    when(tagDao.read(tag.getName())).thenReturn(Optional.of(tag));

    tagService.create(tag);

    verify(tagDao, never()).create(any());
  }

  @Test
  void createIfExistedTagDaoReadInvocation() {
    Tag tag = givenTag();
    when(tagDao.read(tag.getName())).thenReturn(Optional.of(tag));

    tagService.create(tag);

    verify(tagDao).read(tag.getName());
  }

  @Test
  void createIfNotExistedTagDaoCreateInvocation() {
    Tag tag = givenTag();
    when(tagDao.readAll()).thenReturn(Collections.emptyList());
    when(tagDao.read(tag.getName())).thenReturn(Optional.empty());

    tagService.create(tag);

    verify(tagDao).create(tag);
  }

  @Test
  void read() {
    tagService.read(TAG_ID);

    verify(tagDao).read(anyLong());
  }

  @Test
  void readAll() {
    tagService.readAll();

    verify(tagDao).readAll();
  }

  @Test
  void deleteCertificateDaodeleteCertificateTagsByTagIdInvocation() {
    tagService.delete(TAG_ID);

    verify(certificateDao).deleteCertificateTagsByTagId(TAG_ID);
  }

  @Test
  void deleteTagDaoDeleteInvocation() {
    tagService.delete(TAG_ID);

    verify(tagDao).delete(TAG_ID);
  }

  private static Tag givenTag() {
    return Tag.builder().id(TAG_ID).name("first tag").build();
  }
}
