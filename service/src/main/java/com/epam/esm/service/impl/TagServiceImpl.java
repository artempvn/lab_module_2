package com.epam.esm.service.impl;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.TagService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
public class TagServiceImpl implements TagService {

  private final TagDao tagDao;
  private final CertificateDao certificateDao;

  public TagServiceImpl(TagDao tagDao, CertificateDao certificateDao) {
    this.tagDao = tagDao;
    this.certificateDao = certificateDao;
  }

  @Override
  public Tag create(Tag inputTag) {
    List<Tag> tags = tagDao.readAll();
    boolean wasAddedBefore =
        tags.stream()
            .anyMatch(existedTag -> existedTag.getName().equalsIgnoreCase(inputTag.getName()));
    Tag tag;
    if (wasAddedBefore) {
      tag = tagDao.read(inputTag).get();
    } else {
      tag = tagDao.create(inputTag);
    }
    return tag;
  }

  @Override
  public Optional<Tag> read(long id) {
    return tagDao.read(id);
  }

  @Override
  public List<Tag> readAll() {
    return tagDao.readAll();
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
  public void delete(long id) {
    certificateDao.deleteBondingTagsByTagId(id);
    tagDao.delete(id);
  }
}
