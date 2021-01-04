package com.epam.esm.entity;

import java.util.Collections;
import java.util.List;

public class CertificateDto extends Certificate {

  private List<Tag> tags;

  public CertificateDto() {
    tags = Collections.emptyList();
  }

  public CertificateDto(Certificate certificate) {
    this();
    this.setId(certificate.getId());
    this.setName(certificate.getName());
    this.setDescription(certificate.getDescription());
    this.setDuration(certificate.getDuration());
    this.setCreateDate(certificate.getCreateDate());
    this.setLastUpdateDate(certificate.getLastUpdateDate());
    this.setPrice(certificate.getPrice());
  }

  public List<Tag> getTags() {
    return tags;
  }

  public void setTags(List<Tag> tags) {
    this.tags = tags;
  }
}
