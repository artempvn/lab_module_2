package com.epam.esm.dao.impl;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Tag;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@Component
public class CertificateDaoImpl implements CertificateDao {
  private final JdbcTemplate jdbcTemplate;

  public CertificateDaoImpl(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  @Override
  public Certificate create(Certificate certificate) {
    KeyHolder keyHolder = new GeneratedKeyHolder();
    jdbcTemplate.update(
        connection -> {
          PreparedStatement ps =
              connection.prepareStatement(
                  "INSERT INTO gift_certificates(name,description,price,duration, create_date,last_update_date) VALUES (?,?,?,?,?,?);",
                  Statement.RETURN_GENERATED_KEYS);
          ps.setString(1, certificate.getName());
          ps.setString(2, certificate.getDescription());
          ps.setDouble(3, certificate.getPrice());
          ps.setInt(4, certificate.getDuration());
          ps.setObject(5, certificate.getCreateDate());
          ps.setObject(6, certificate.getLastUpdateDate());
          return ps;
        },
        keyHolder);
    certificate.setId(keyHolder.getKey().longValue());
    return certificate;
  }

  @Override
  public Optional<Certificate> read(long id) {
    return jdbcTemplate
        .queryForStream(
            "SELECT id,name,description,price,duration, create_date,last_update_date FROM gift_certificates WHERE id=?",
            (rs, rowNum) -> {
              Certificate certificate = new Certificate();
              certificate.setId(rs.getLong(1));
              certificate.setName(rs.getString(2));
              certificate.setDescription(rs.getString(3));
              certificate.setPrice(rs.getDouble(4));
              certificate.setDuration(rs.getInt(5));
              certificate.setCreateDate(rs.getObject(6, LocalDateTime.class));
              certificate.setLastUpdateDate(rs.getObject(7, LocalDateTime.class));
              return certificate;
            },
            id)
        .findAny();
  }

  @Override
  public List<Certificate> readAll() {
    return jdbcTemplate.query(
        "SELECT id,name,description,price,duration, create_date,last_update_date FROM gift_certificates",
        (rs, rowNum) -> {
          Certificate certificate = new Certificate();
          certificate.setId(rs.getLong(1));
          certificate.setName(rs.getString(2));
          certificate.setDescription(rs.getString(3));
          certificate.setPrice(rs.getDouble(4));
          certificate.setDuration(rs.getInt(5));
          certificate.setCreateDate(rs.getObject(6, LocalDateTime.class));
          certificate.setLastUpdateDate(rs.getObject(7, LocalDateTime.class));
          return certificate;
        });
  }

  @Override
  public void update(Certificate certificate) {
    jdbcTemplate.update(
        "UPDATE gift_certificates SET name=?,description=?,price=?,duration=?, create_date=?,last_update_date=? WHERE id=?;",
        certificate.getName(),
        certificate.getDescription(),
        certificate.getPrice(),
        certificate.getDuration(),
        certificate.getCreateDate(),
        certificate.getLastUpdateDate(),
        certificate.getId());
  }

  @Override
  public void delete(long id) {
    jdbcTemplate.update("DELETE FROM gift_certificates WHERE id=?", id);
  }

  @Override
  public void addTag(long tagId, long certificateId) {
    jdbcTemplate.update(
        "INSERT INTO certificates_tags(tag_id,certificate_id) VALUES (?,?);", tagId, certificateId);
  }

  @Override
  public List<Tag> readTags(long certificateId) {
    return jdbcTemplate.query(
        "SELECT id,name FROM tag JOIN certificates_tags ON tag_id=id WHERE certificate_id=?;",
        new BeanPropertyRowMapper<>(Tag.class),
        certificateId);
  }
}
