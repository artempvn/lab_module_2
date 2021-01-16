package com.epam.esm.service.impl;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.TagAction;
import com.epam.esm.exception.CertificateBadRequestException;
import com.epam.esm.exception.ResourceBadRequestException;
import com.epam.esm.exception.TagBadRequestException;
import com.epam.esm.exception.TagNotFoundException;
import com.epam.esm.service.TagService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
public class TagServiceImpl implements TagService {
  public static final int ONE_UPDATED_ROW = 1;

  private final TagDao tagDao;
  private final CertificateDao certificateDao;

  public TagServiceImpl(TagDao tagDao, CertificateDao certificateDao) {
    this.tagDao = tagDao;
    this.certificateDao = certificateDao;
  }

  @Override
  public Tag create(Tag inputTag) {
    Optional<Tag> existingTag = tagDao.read(inputTag.getName());
    return existingTag.orElseGet(() -> tagDao.create(inputTag));
  }

  @Override
  public Tag read(long id) {
    Optional<Tag> tag = tagDao.read(id);
    return tag.orElseThrow(
        () -> new TagNotFoundException("There is no tag in db with id = " + id, id));
  }

  @Override
  public List<Tag> readAll() {
    return tagDao.readAll();
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
  public void delete(long id) {
    certificateDao.deleteCertificateTagsByTagId(id);
    int numberOfUpdatedRows = tagDao.delete(id);
    if (numberOfUpdatedRows != ONE_UPDATED_ROW) {
      throw new TagBadRequestException("There is no tag in db with id = " + id, id);
    }
  }

  @Override
  public void processTagAction(TagAction action) {
    long tagId = action.getTagId();
    long certificateId = action.getCertificateId();
    switch (action.getType()) {
      case ADD:
        if (tagDao.read(tagId).isEmpty()) {
          throw new TagBadRequestException("There is no tag in db with id = " + tagId, tagId);
        }
        if (certificateDao.read(certificateId).isEmpty()) {
          throw new CertificateBadRequestException(
              "There is no certificate in db with id = " + certificateId, certificateId);
        }
        certificateDao.addTag(tagId, certificateId);
        break;
      case REMOVE:
        int numberOfRemovedRows = certificateDao.removeTag(tagId, certificateId);
        if (numberOfRemovedRows == 0) {
          throw new ResourceBadRequestException(
              "There is no tag id = " + tagId + " in certificate id = " + certificateId,
              tagId,
              certificateId);
        }
        break;
    }
  }
}
