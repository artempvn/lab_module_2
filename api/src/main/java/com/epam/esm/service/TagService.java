package com.epam.esm.service;

import com.epam.esm.entity.Tag;

import java.util.List;
import java.util.Optional;

public interface TagService {

  Tag create(Tag tag);

  Optional<Tag> read(long id);

  List<Tag> readAll();

  void delete(long id);
}
