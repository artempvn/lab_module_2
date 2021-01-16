package com.epam.esm.service.impl;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.GetParameter;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.CertificateBadRequestException;
import com.epam.esm.exception.CertificateNotFoundException;
import com.epam.esm.service.CertificateService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
public class CertificateServiceImpl implements CertificateService {

  public static final int ONE_UPDATED_ROW = 1;
  private final CertificateDao certificateDao;
  private final TagDao tagDao;

  public CertificateServiceImpl(CertificateDao certificateDao, TagDao tagDao) {
    this.certificateDao = certificateDao;
    this.tagDao = tagDao;
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
  public Certificate create(Certificate certificate) {
    Certificate createdCertificate = certificateDao.create(certificate);
    createdCertificate.setTags(certificate.getTags());
    addTagsToDb(createdCertificate);
    return createdCertificate;
  }

  @Override
  public Certificate read(long id) {
    Optional<Certificate> certificate = certificateDao.read(id);
    certificate.ifPresent(
        actualCertificate -> actualCertificate.setTags(certificateDao.readCertificateTags(id)));
    return certificate.orElseThrow(
        () ->
            new CertificateNotFoundException("There is no certificate in db with id = " + id, id));
  }

  @Override
  public List<Certificate> readAll(GetParameter parameter) {
    List<Certificate> certificates = certificateDao.readAll(parameter);
    for (Certificate certificate : certificates) {
      certificate.setTags(certificateDao.readCertificateTags(certificate.getId()));
    }
    return certificates;
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
  public Certificate updatePatch(Certificate certificate) {
    int numberOfUpdatedRows = certificateDao.updatePatch(certificate);
    if (numberOfUpdatedRows != ONE_UPDATED_ROW) {
      throw new CertificateBadRequestException(
          "There is no certificate in db with id = " + certificate.getId(), certificate.getId());
    }
    return certificate;
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
  public Certificate updatePut(Certificate certificate) {
    int numberOfUpdatedRows = certificateDao.update(certificate);
    if (numberOfUpdatedRows != ONE_UPDATED_ROW) {
      throw new CertificateBadRequestException(
          "There is no certificate in db with id = " + certificate.getId(), certificate.getId());
    }
    certificateDao.deleteCertificateTagsByCertificateId(certificate.getId());
    addTagsToDb(certificate);
    return certificate;
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
  public void delete(long id) {
    certificateDao.deleteCertificateTagsByCertificateId(id);
    int numberOfUpdatedRows = certificateDao.delete(id);
    if (numberOfUpdatedRows != ONE_UPDATED_ROW) {
      throw new CertificateBadRequestException("There is no certificate in db with id = " + id, id);
    }
  }

  void addTagsToDb(Certificate certificate) {
    List<Tag> tags = certificate.getTags();
    if (tags != null) {
      for (Tag tag : tags) {
        Optional<Tag> existedTag = tagDao.read(tag.getName());
        long tagId = existedTag.map(Tag::getId).orElseGet(() -> tagDao.create(tag).getId());
        certificateDao.addTag(tagId, certificate.getId());
      }
    }
  }
}
