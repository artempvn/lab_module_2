package com.epam.esm.dao;

import com.epam.esm.entity.Tag;

import java.util.List;
import java.util.Optional;

public interface TagDao {

  Tag create(Tag tag);

  Optional<Tag> read(long id);

  List<Tag> readAll();

  void delete(long id);

  Optional<Tag> read(String name);
}
