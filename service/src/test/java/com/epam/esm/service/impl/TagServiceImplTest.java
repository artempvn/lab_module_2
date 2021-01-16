package com.epam.esm.service.impl;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.TagAction;
import com.epam.esm.exception.CertificateBadRequestException;
import com.epam.esm.exception.ResourceBadRequestException;
import com.epam.esm.exception.TagBadRequestException;
import com.epam.esm.exception.TagNotFoundException;
import com.epam.esm.service.TagService;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class TagServiceImplTest {

  public static final long TAG_ID = 1L;
  public static final long CERTIFICATE_ID = 1L;
  public static final int ONE_DELETED_ROW = 1;
  public static final int NO_DELETED_ROW = 0;
  TagDao tagDao = mock(TagDao.class);
  CertificateDao certificateDao = mock(CertificateDao.class);
  TagService tagService = new TagServiceImpl(tagDao, certificateDao);

  @Test
  void createTagDaoReadInvocation() {
    Tag tag = Tag.builder().id(TAG_ID).name("first tag").build();
    when(tagDao.read(tag.getName())).thenReturn(Optional.of(tag));

    tagService.create(tag);

    verify(tagDao).read(tag.getName());
  }

  @Test
  void createIfExistedTagDaoCreateInvocation() {
    Tag tag = Tag.builder().id(TAG_ID).name("first tag").build();
    when(tagDao.read(tag.getName())).thenReturn(Optional.of(tag));

    tagService.create(tag);

    verify(tagDao, never()).create(any());
  }

  @Test
  void createIfExistedTagDaoReadInvocation() {
    Tag tag = Tag.builder().id(TAG_ID).name("first tag").build();
    when(tagDao.read(tag.getName())).thenReturn(Optional.of(tag));

    tagService.create(tag);

    verify(tagDao).read(tag.getName());
  }

  @Test
  void createIfNotExistedTagDaoCreateInvocation() {
    Tag tag = Tag.builder().id(TAG_ID).name("first tag").build();
    when(tagDao.readAll()).thenReturn(Collections.emptyList());
    when(tagDao.read(tag.getName())).thenReturn(Optional.empty());

    tagService.create(tag);

    verify(tagDao).create(tag);
  }

  @Test
  void read() {
    Tag tag = Tag.builder().id(TAG_ID).name("first tag").build();
    when(tagDao.read(anyLong())).thenReturn(Optional.of(tag));

    tagService.read(TAG_ID);

    verify(tagDao).read(anyLong());
  }

  @Test
  void readException() {
    assertThrows(TagNotFoundException.class, () -> tagService.read(TAG_ID));
  }

  @Test
  void readAll() {
    tagService.readAll();

    verify(tagDao).readAll();
  }

  @Test
  void deleteCertificateDaoDeleteCertificateTagsByTagIdInvocation() {
    when(tagDao.delete(anyLong())).thenReturn(ONE_DELETED_ROW);

    tagService.delete(TAG_ID);

    verify(certificateDao).deleteCertificateTagsByTagId(TAG_ID);
  }

  @Test
  void deleteTagDaoDeleteInvocation() {
    when(tagDao.delete(anyLong())).thenReturn(ONE_DELETED_ROW);

    tagService.delete(TAG_ID);

    verify(tagDao).delete(TAG_ID);
  }

  @Test
  void deleteTagDaoDeleteException() {
    when(tagDao.delete(anyLong())).thenReturn(NO_DELETED_ROW);

    assertThrows(TagBadRequestException.class, () -> tagService.delete(TAG_ID));
  }

  @Test
  void processTagActionAddTagDaoReadInvocation() {
    Tag tag = Tag.builder().id(TAG_ID).name("first tag").build();
    Certificate certificate = Certificate.builder().id(CERTIFICATE_ID).build();
    TagAction tagAction = new TagAction(TagAction.ActionType.ADD, certificate.getId(), tag.getId());
    when(tagDao.read(anyLong())).thenReturn(Optional.of(tag));
    when(certificateDao.read(anyLong())).thenReturn(Optional.of(certificate));

    tagService.processTagAction(tagAction);

    verify(tagDao).read(tag.getId());
  }

  @Test
  void processTagActionAddCertificateDaoReadInvocation() {
    Tag tag = Tag.builder().id(TAG_ID).name("first tag").build();
    Certificate certificate = Certificate.builder().id(CERTIFICATE_ID).build();
    TagAction tagAction = new TagAction(TagAction.ActionType.ADD, certificate.getId(), tag.getId());
    when(tagDao.read(anyLong())).thenReturn(Optional.of(tag));
    when(certificateDao.read(anyLong())).thenReturn(Optional.of(certificate));

    tagService.processTagAction(tagAction);

    verify(certificateDao).read(certificate.getId());
  }

  @Test
  void processTagActionAddCertificateDaoAddTagInvocation() {
    Tag tag = Tag.builder().id(TAG_ID).name("first tag").build();
    Certificate certificate = Certificate.builder().id(CERTIFICATE_ID).build();
    TagAction tagAction = new TagAction(TagAction.ActionType.ADD, certificate.getId(), tag.getId());
    when(tagDao.read(anyLong())).thenReturn(Optional.of(tag));
    when(certificateDao.read(anyLong())).thenReturn(Optional.of(certificate));

    tagService.processTagAction(tagAction);

    verify(certificateDao).addTag(tag.getId(), certificate.getId());
  }

  @Test
  void processTagActionAddTagDaoReadException() {
    Tag tag = Tag.builder().id(TAG_ID).name("first tag").build();
    Certificate certificate = Certificate.builder().id(CERTIFICATE_ID).build();
    TagAction tagAction = new TagAction(TagAction.ActionType.ADD, certificate.getId(), tag.getId());
    when(tagDao.read(anyLong())).thenReturn(Optional.empty());
    when(certificateDao.read(anyLong())).thenReturn(Optional.of(certificate));

    assertThrows(TagBadRequestException.class, () -> tagService.processTagAction(tagAction));
  }

  @Test
  void processTagActionAddCertificateDaoReadException() {
    Tag tag = Tag.builder().id(TAG_ID).name("first tag").build();
    Certificate certificate = Certificate.builder().id(CERTIFICATE_ID).build();
    TagAction tagAction = new TagAction(TagAction.ActionType.ADD, certificate.getId(), tag.getId());
    when(tagDao.read(anyLong())).thenReturn(Optional.of(tag));
    when(certificateDao.read(anyLong())).thenReturn(Optional.empty());

    assertThrows(
        CertificateBadRequestException.class, () -> tagService.processTagAction(tagAction));
  }

  @Test
  void processTagActionRemoveCertificateDaoRemoveTagInvocation() {
    Tag tag = Tag.builder().id(TAG_ID).name("first tag").build();
    Certificate certificate = Certificate.builder().id(CERTIFICATE_ID).build();
    TagAction tagAction =
        new TagAction(TagAction.ActionType.REMOVE, certificate.getId(), tag.getId());
    when(certificateDao.removeTag(anyLong(), anyLong())).thenReturn(ONE_DELETED_ROW);

    tagService.processTagAction(tagAction);

    verify(certificateDao).removeTag(tag.getId(), certificate.getId());
  }

  @Test
  void processTagActionRemoveCertificateDaoRemoveTagException() {
    Tag tag = Tag.builder().id(TAG_ID).name("first tag").build();
    Certificate certificate = Certificate.builder().id(CERTIFICATE_ID).build();
    TagAction tagAction =
        new TagAction(TagAction.ActionType.REMOVE, certificate.getId(), tag.getId());
    when(certificateDao.removeTag(anyLong(), anyLong())).thenReturn(NO_DELETED_ROW);

    assertThrows(ResourceBadRequestException.class, () -> tagService.processTagAction(tagAction));
  }
}
