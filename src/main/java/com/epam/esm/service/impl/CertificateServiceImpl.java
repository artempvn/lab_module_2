package com.epam.esm.service.impl;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.CertificateDto;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.CertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class CertificateServiceImpl implements CertificateService {

  @Autowired CertificateDao certificateDao;
  @Autowired TagDao tagDao;

  @Override
  @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
  public void create(CertificateDto certificateDto) {
    long certificateId = certificateDao.create((Certificate) certificateDto).getId();
    List<Tag> tags = certificateDto.getTags();
    tags.forEach(
        tag -> {
          Optional<Tag> existedTag = tagDao.read(tag);
          long tagId =
              (existedTag.isEmpty()) ? tagDao.create(tag).getId() : existedTag.get().getId();
          certificateDao.addTag(tagId, certificateId);
        });
  }

  @Override
  public CertificateDto read(long id) {
    CertificateDto certificateDto = new CertificateDto(certificateDao.read(id));
    certificateDto.setTags(certificateDao.readTags(id));
    return certificateDto;
  }

  @Override
  public List<CertificateDto> readAll() {
    List<CertificateDto> certificates = new ArrayList<>();
    certificateDao
        .readAll()
        .forEach(
            certificate -> {
              CertificateDto dto = new CertificateDto(certificate);
              dto.setTags(certificateDao.readTags(dto.getId()));
              certificates.add(dto);
            });
    return certificates;
  }

  @Override
  public void update(long id, CertificateDto certificateDto) {
    // TODO check empty fields & tags
    certificateDto.setId(id);
    List<Tag> tags = certificateDto.getTags();
    tags.forEach(
        tag -> {
          Optional<Tag> existedTag = tagDao.read(tag);
          long tagId =
              (existedTag.isEmpty()) ? tagDao.create(tag).getId() : existedTag.get().getId();
          certificateDao.addTag(tagId, id);
        });
    certificateDao.update(certificateDto);
  }

  @Override
  public void delete(long id) {
    // TODO delete tag dependency
    certificateDao.delete(id);
  }
}
