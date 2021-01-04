package com.epam.esm.dao.impl;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Types;
import java.util.List;
import java.util.Optional;

@Component
public class TagDaoImpl implements TagDao {

  @Autowired private JdbcTemplate jdbcTemplate;

  @Override
  public Tag create(Tag tag) {
    KeyHolder keyHolder = new GeneratedKeyHolder();
    jdbcTemplate.update(
        connection -> {
          PreparedStatement ps =
              connection.prepareStatement(
                  "INSERT INTO tag(name) VALUES (?);", Statement.RETURN_GENERATED_KEYS);
          ps.setString(1, tag.getName());
          return ps;
        },
        keyHolder);
    tag.setId(((BigInteger) keyHolder.getKey()).longValue());
    return tag;
  }

  @Override
  public Tag read(long id) {
    return jdbcTemplate.queryForObject(
        "SELECT id,name FROM tag WHERE id=?",
        new Object[] {id},
        new int[] {Types.BIGINT},
        new BeanPropertyRowMapper<>(Tag.class));
  }

  @Override
  public List<Tag> readAll() {
    return jdbcTemplate.query("SELECT id,name FROM tag", new BeanPropertyRowMapper<>(Tag.class));
  }

  @Override
  public void delete(long id) {
    jdbcTemplate.update("DELETE FROM tag WHERE id=?", id);
  }

  @Override
  public Optional<Tag> read(Tag tag) {
    return jdbcTemplate
        .queryForStream(
            "SELECT id,name FROM tag WHERE name=?",
            new BeanPropertyRowMapper<>(Tag.class),
            tag.getName())
        .findAny();
  }
}
